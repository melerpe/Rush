package net.rush.protocol.legacy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.logging.Level;

import net.rush.Server;
import net.rush.protocol.MinecraftHandler;
import net.rush.protocol.Packet;

/**
 * This class decodes (read) incoming connections (in this case - packets).
 * @author kangarko
 */
public class LegacyDecoder extends MessageToMessageDecoder<ByteBuf> {

	//private int previousOpcode = -1;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			if(in.readableBytes() == 0)
				return;

			int clientProtocol = ctx.pipeline().get(MinecraftHandler.class).session.getClientVersion().getProtocol();
			int opCode = in.readUnsignedByte();

			Packet packet = Packet.createPacket(opCode);
			
			if(!packet.getClass().getSimpleName().contains("OnGround"))
				System.out.println("<<<< READING PACKET: " + packet);
			
			packet.setProtocol(clientProtocol);
			packet.setCompat(true);
			packet.readCompat(in);

			/*if (previousOpcode != - 1 && in.readableBytes() != 0) {
				throw new RuntimeException("Did not read all bytes from " + packet + " ID " + opCode + ", previous opcode: " + previousOpcode + ", bytes left: " + in.readableBytes());
			}

			previousOpcode = opCode;*/

			out.add(packet);
		} catch (Throwable t) {
			Server.getServer().getLogger().log(Level.SEVERE, "Error on legacy packet read", t);
			System.out.println("-!- CRITICAL PACKET ERROR ! -!- SYSTEM SHUTDOWN -!-");
			System.exit(0);
		}
	}

}
