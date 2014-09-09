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
public class PlayerPositionAndLookPacket extends Packet {

	private double x;
	private double yOrStance;
	private double stanceOrY;
	private double z;
	private float yaw;
	private float pitch;
	private boolean onGround;

	@Override
	public void read(ByteBuf input) throws IOException {
		x = input.readDouble();
		if (compat || protocol < 16) {
			this.yOrStance = input.readDouble();
			stanceOrY = input.readDouble();
		} else {
			yOrStance = input.readDouble();
			stanceOrY = yOrStance + 1.62;
		}
		z = input.readDouble();
		yaw = input.readFloat();
		pitch = input.readFloat();
		onGround = input.readBoolean();
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		System.out.println("PlayerPositionAndLook Tested Only On 1.6.4");
		output.writeDouble(this.x);
		output.writeDouble(this.yOrStance); //feet height ??
		//output.writeDouble(1.62D); // head height ??
		output.writeDouble(this.z);
		output.writeFloat(this.yaw);
		output.writeFloat(this.pitch);
		output.writeBoolean(this.onGround);
	}
}
