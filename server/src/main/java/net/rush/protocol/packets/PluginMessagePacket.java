package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PluginMessagePacket extends Packet {

	private String channel;
	private byte[] data;

	public PluginMessagePacket(String channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	}
	
	@Override
	public void read(ByteBuf in) throws IOException {
		channel = readString(in, Integer.MAX_VALUE, compat);

		if (compat || protocol < 29)
			data = new byte[in.readShort()];
		else
			data = new byte[in.readableBytes()];

		byte[] bytes = new byte[in.readableBytes()];
		in.readBytes(bytes);
		data = bytes;
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		writeString(channel, out, compat);

		if (compat || protocol < 29)
			out.writeShort(this.data.length);
		
		if (!compat && protocol >= 47 && channel.equals("MC|Brand")) {
			writeString(new String(data, "UTF-8"), out, false);
			return;
		}
		
		out.writeBytes(this.data);
		
		if (!compat && protocol >= 29 && channel.equals("MC|AdvCdm"))
			out.writeBoolean(true);
	}
}
