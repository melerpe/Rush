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
public class PacketSteerVehicle extends Packet {

	private float sideways;
	private float forward;
	private boolean jump;
	private boolean unmount;

	@Override
	public void read(ByteBuf input) throws IOException {
		sideways = input.readFloat();
		forward = input.readFloat();
		if (compat || protocol < 16) {
			jump = input.readBoolean();
			unmount = input.readBoolean();
		} else {
			int flags = input.readUnsignedByte();
			jump = (flags & 0x1) != 0;
			unmount = (flags & 0x2) != 0;
		}
	}
}
