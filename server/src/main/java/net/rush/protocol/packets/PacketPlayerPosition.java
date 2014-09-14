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
public class PacketPlayerPosition extends PacketPlayerOnGround {
	
	public PacketPlayerPosition(double x, double y, double stance, double z, boolean onGround) {
		super(onGround);
		this.x = x;
		this.yOrStance = y;
		this.stanceOrY = stance;
		this.z = z;
	}

	@Override
	public void read(ByteBuf in) throws IOException {
		x = in.readDouble();
		if (compat || protocol < 16) {
			yOrStance = in.readDouble();
			stanceOrY = in.readDouble();
		} else {
			yOrStance = in.readDouble();
			stanceOrY+= 1.62;
		}
		z = in.readDouble();
		onGround = in.readBoolean();
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeDouble(x);
		out.writeDouble(yOrStance - (compat || protocol >= 16 ? 1.62 : 0));
		out.writeDouble(stanceOrY);
		out.writeDouble(z);
		if (compat || protocol < 16)
			out.writeBoolean(onGround);
		else
			out.writeByte(0);
	}
}
