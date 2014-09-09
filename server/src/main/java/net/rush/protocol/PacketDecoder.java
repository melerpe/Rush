package net.rush.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.DataInputStream;
import java.util.List;
import java.util.logging.Level;

import lombok.Setter;
import net.rush.PacketLogger;
import net.rush.Server;
import net.rush.protocol.packets.HandshakePacket;

/**
 * Packet decoding class backed by a reusable {@link DataInputStream} which
 * backs the input {@link ByteBuf}. Reads an unsigned byte packet header and
 * then decodes the packet accordingly.
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Setter
	private ProtocolNew protocol;

	public PacketDecoder(ProtocolNew prot) {
		this.protocol = prot;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			if(in.readableBytes() == 0)
				return;

			int clientProtocol = ctx.pipeline().get(MinecraftHandler.class).session.getClientVersion().getProtocol();
			int opCode = Packet.readVarInt(in);

			Packet packet = protocol.TO_SERVER.createPacket(opCode);		

			packet.setProtocol(clientProtocol);
			packet.read(in);

			if (in.readableBytes() != 0)
				throw new RuntimeException("Did not read all bytes from packet " + packet.getClass() + " ID " + opCode);

			out.add(packet);

			if (packet instanceof HandshakePacket) {
				HandshakePacket handshake = (HandshakePacket) packet;

				switch (handshake.getState()) {
				case 1:
					setProtocol(ctx, ProtocolNew.STATUS);
					break;
				case 2:
					setProtocol(ctx, ProtocolNew.LOGIN);
					break;
				}
			}

			PacketLogger.submitWrite(packet, clientProtocol, true);
		} catch (Throwable t) {
			Server.getServer().getLogger().log(Level.SEVERE, "Error on packet read", t);
		}
	}

	public void setProtocol(ChannelHandlerContext channel, ProtocolNew prot) {
		channel.pipeline().get(PacketDecoder.class).setProtocol(prot);
		channel.pipeline().get(PacketEncoder.class).setProtocol(prot);
	}
}
