package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class PacketPlayerLookAndPosition extends PacketPlayerOnGround {
	
	public PacketPlayerLookAndPosition(double x, double yOrStance, double stanceOrY, double z, float yaw, float pitch, boolean onGround) {
		super(onGround);
		this.x = x;
		this.yOrStance = yOrStance;
		this.stanceOrY = stanceOrY;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

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
