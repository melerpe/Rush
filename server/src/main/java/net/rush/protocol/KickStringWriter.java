package net.rush.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.rush.protocol.utils.PacketUtils;

public class KickStringWriter extends MessageToByteEncoder<String> {

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
		out.writeByte(0xFF); // KickPacket
		PacketUtils.writeString(msg, out, true);
	}
}
