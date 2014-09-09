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
public class TimeUpdatePacket extends Packet {

	private long worldAge;
	private long time;

	@Override
	public void read(ByteBuf input) throws IOException {
		worldAge = input.readLong();
		time = input.readLong();
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeLong(worldAge);
		output.writeLong(time);
	}
}
