package net.rush.protocol.legacy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;
import java.util.logging.Level;

import net.rush.Server;
import net.rush.protocol.Packet;

/**
 * This class decodes (read) incoming connections (in this case - packets).
 * @author kangarko
 */
public class LegacyDecoder extends ReplayingDecoder<ByteBuf> {

	//private int previousOpcode = -1;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			if(in.readableBytes() == 0)
				return;

			int opCode = in.readUnsignedByte();

			Packet packet = Packet.createPacket(opCode);
			packet.setCompat(true);
			packet.readCompat(in);

			//if (in.readableBytes() != 0) {
			//	throw new RuntimeException("Did not read all bytes from " + packet + " ID " + opCode + ", previous opcode: " + previousOpcode + ", bytes left: " + in.readableBytes());
			//}

			//previousOpcode = opCode;

			out.add(packet);
		} catch (Throwable t) {
			Server.getServer().getLogger().log(Level.SEVERE, "Error on legacy packet read", t);
		}
	}

}
