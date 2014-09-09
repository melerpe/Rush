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
public class PlayerAbilitiesPacket extends Packet {

	private int flags;
	private float flySpeed;
	private float walkSpeed;

	@Override
	public void read(ByteBuf input) throws IOException {
		flags = input.readByte();
		flySpeed = input.readFloat();
		walkSpeed = input.readFloat();
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeByte(flags);
		output.writeFloat(flySpeed);
		output.writeFloat(walkSpeed);
	}
}
