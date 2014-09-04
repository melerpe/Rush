package net.rush.packets.packet;

import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import net.rush.packets.Packet;

public class PacketLoginCompression extends Packet {
	
	private int threshold;

	public PacketLoginCompression(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public void write17(ByteBufOutputStream output) throws IOException {
		writeByteInteger(output, threshold);
	}

	@Override
	public String getToStringDescription() {
		return "threshold=" + threshold;
	}

	@Override
	public int getOpcode() {
		return 0x3;
	}
}