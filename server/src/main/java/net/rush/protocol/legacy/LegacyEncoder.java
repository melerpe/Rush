package net.rush.protocol.legacy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.rush.Server;
import net.rush.protocol.MinecraftHandler;
import net.rush.protocol.Packet;

/**
 * This class encodes (write) incoming connections (in this case - packets).
 * @author kangarko
 */
public class LegacyEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
		try {
			int clientProtocol = ctx.pipeline().get(MinecraftHandler.class).session.getClientVersion().getProtocol();

			String n = packet.getClass().getSimpleName();
			
			/*List<String> blackList = Arrays.asList("SpawnPositionPacket");
			
			if(blackList.contains(n))
				return;*/
			
			if(!n.contains("Map"))
				System.out.println(">>>>>>>>> Writing up packet: " + packet);
			
			out.writeByte(packet.getId()); // opcode
			
			packet.setProtocol(clientProtocol);
			packet.setCompat(true);
			packet.writeCompat(out);
		} catch (Throwable t) {
			Server.getServer().getLogger().log(Level.SEVERE, "Error while writing legacy packet " + packet.getClass().getSimpleName(), t);
		}
	}
}
