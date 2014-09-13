package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Packet17PingTime extends Packet {
	
	public long time;

	@Override
	public void read(ByteBuf input) throws IOException {
		time = input.readLong();
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeLong(time);
	}

}
