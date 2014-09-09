package net.rush.protocol;

import java.util.logging.Level;

import lombok.Setter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.rush.PacketLogger;
import net.rush.Server;
import net.rush.protocol.packets.Packet17LoginSuccess;
import net.rush.protocol.utils.PacketUtils;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	@Setter
	private ProtocolNew protocol;

	public PacketEncoder(ProtocolNew protocol) {
		this.protocol = protocol;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {		
		String p = packet.getClass().getSimpleName();
		
		try {
			int clientProtocol = ctx.pipeline().get(MinecraftHandler.class).session.getClientVersion().getProtocol();

			PacketUtils.writeVarInt(protocol.TO_CLIENT.getId(packet.getClass()), out);

			packet.setProtocol(clientProtocol);
			packet.write(out);
			
			if(!p.contains("Map"))
				System.out.println("Now Writing Packet ID " + packet);

			if (packet instanceof Packet17LoginSuccess)
				setProtocol(ctx, ProtocolNew.GAME);

			PacketLogger.submitWrite(packet, clientProtocol, false);
		} catch (Throwable t) {
			Server.getServer().getLogger().log(Level.SEVERE, "Error while writing packet " + packet.getClass().getSimpleName(), t);
		}
	}

	public void setProtocol(ChannelHandlerContext channel, ProtocolNew prot) {
		channel.pipeline().get(PacketDecoder.class).setProtocol(prot);
		channel.pipeline().get(PacketEncoder.class).setProtocol(prot);
	}
}
