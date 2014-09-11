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
			yOrStance = input.readDouble();
			stanceOrY = input.readDouble();
		} else {
			//yOrStance = input.readDouble();
			yOrStance = /*yOrStance*/input.readDouble() + 1.62;
		}
		z = input.readDouble();
		yaw = input.readFloat();
		pitch = input.readFloat();
		onGround = input.readBoolean();
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeDouble(x);
		output.writeDouble(yOrStance); //feet height ??
		if (compat)
			output.writeDouble(stanceOrY); // head height ??
		output.writeDouble(z);
		output.writeFloat(yaw);
		output.writeFloat(pitch);
		output.writeBoolean(onGround);
	}
}
