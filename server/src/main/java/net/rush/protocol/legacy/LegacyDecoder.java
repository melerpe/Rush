package net.rush.protocol.legacy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.rush.protocol.MinecraftHandler;
import net.rush.protocol.Packet;

/**
 * This class decodes (read) incoming connections (in this case - packets).
 * @author kangarko
 */
public class LegacyDecoder extends MessageToMessageDecoder<ByteBuf> {

	private final Logger logger = Logger.getLogger("Minecraft");
	//private int previousOpcode = -1;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			if(in.readableBytes() == 0)
				return;
			
			int clientProtocol = ctx.pipeline().get(MinecraftHandler.class).session.getClientVersion().getProtocol();
			int opCode = in.readUnsignedByte();

			if(clientProtocol == 4)
				throw new RuntimeException("Invalid protocol " + clientProtocol);
			
			Packet packet = Packet.createPacket(opCode);

			String p = packet.getClass().getSimpleName();

			//logger.info("READING PACKET: " + packet);

			packet.setProtocol(clientProtocol);
			packet.setCompat(true);
			packet.readCompat(in);

			if(!p.contains("OnGround") && !p.contains("PlayerLook") && !p.contains("PlayerPosition"))
				logger.info("<<<< READED PACKET: " + packet);

			//if (previousOpcode != -1 && in.readableBytes() != 0)
			//	throw new RuntimeException("Didnt read all bytes from " + packet + " ID " + opCode + ", previous opcode: " + previousOpcode + ", bytes left: " + in.readableBytes() + ", protocol: " + clientProtocol);
			//previousOpcode = opCode;

			out.add(packet);
		} catch (IllegalStateException ex) {
			logger.severe(ex.getMessage());
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error on legacy packet read", t);
			logger.info("-!- CRITICAL PACKET ERROR ! -!- SYSTEM SHUTDOWN -!-");
			System.exit(0);
		}
	}

}
