package net.rush.protocol.legacy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.rush.protocol.MinecraftHandler;
import net.rush.protocol.Packet;

/**
 * This class encodes (write) incoming connections (in this case - packets).
 * @author kangarko
 */
public class LegacyEncoder extends MessageToByteEncoder<Packet> {

	private final Logger logger = Logger.getLogger("Minecraft");
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
		try {
			int clientProtocol = ctx.pipeline().get(MinecraftHandler.class).session.getClientVersion().getProtocol();

			if(clientProtocol == 4)			
				throw new RuntimeException("Invalid protocol " + clientProtocol);
			
			String n = packet.getClass().getSimpleName();			
			/*List<String> blackList = Arrays.asList("SpawnPositionPacket");			
			if(blackList.contains(n))
				return;*/			
			if(!n.contains("Map"))
				logger.info(">>>>>>>>> Writing up packet: " + packet);
			
			out.writeByte(packet.getId()); // opcode
			
			packet.setProtocol(clientProtocol);
			packet.setCompat(true);
			packet.writeCompat(out);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error while writing legacy packet " + packet.getClass().getSimpleName(), t);
		}
	}
}
