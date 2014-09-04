package net.rush.packets.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import net.rush.packets.Packet;

public class PingTime extends Packet {
	
	public long time;

	public PingTime() {
	}

	public PingTime(long time) {
		this.time = time;
	}

	@Override
	public String getToStringDescription() {
		return "time=" + time;
	}

	public int getOpcode() {
		return 1;
	}

	@Override
	public void read17(ByteBufInputStream input) throws IOException {
		time = input.readLong();
	}

	@Override
	public void write17(ByteBufOutputStream output) throws IOException {
		output.writeLong(time);
	}

}
