package net.rush.packets.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class PluginMessagePacket extends Packet {

	public PluginMessagePacket() {
	}

	@Serialize(type = Type.STRING, order = 0)
	private String channel = "";
	@Serialize(type = Type.SHORT, order = 1)
	private short length = -1;
	@Serialize(type = Type.BYTE_ARRAY, order = 2, moreInfo = 1)
	private byte[] data;

	public PluginMessagePacket(String channel, byte[] data) {
		this(channel, (short) data.length, data);
	}
	
	public PluginMessagePacket(String channel, short length, byte[] data) {
		super();
		this.channel = channel;
		this.length = length;
		this.data = data;
	}

	public int getOpcode() {
		return 0xFA;
	}

	public String getChannel() {
		return channel;
	}

	public short getLength() {
		return length;
	}

	public byte[] getData() {
		return data;
	}

	public String getToStringDescription() {
		return "channel=" + channel + ", length=" + length + ", data=byte[" +  (data != null ? data.length : 0) + "]";
	}

	@Override
	public void read17(ByteBufInputStream input) throws IOException {
		channel = readString(input, 999999999, false);

		if (protocol < 29)
			length = input.readShort();
		else
			length = (short) input.available(); // TODO readableBytes() ?

		byte[] bytes = new byte[1];
		input.readFully(bytes);
		data = bytes;
	}

	@Override
	public void write17(ByteBufOutputStream output) throws IOException {
		writeString(channel, output, false);

		if (protocol < 29)
			output.writeShort(this.data.length);
		
		if (protocol >= 47 && channel.equals("MC|Brand")) {
			writeString(new String(data, "UTF-8"), output, false);
			return;
		}
		
		output.write(this.data);
		
		if (protocol >= 29 && channel.equals("MC|AdvCdm"))
			output.writeBoolean(true);
	}
}
