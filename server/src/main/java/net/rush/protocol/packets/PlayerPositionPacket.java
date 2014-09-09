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
public class PlayerPositionPacket extends Packet {

	private double x;
	private double y;
	private double stance;
	private double z;
	private boolean onGround;

	@Override
	public void read(ByteBuf in) throws IOException {
		x = in.readDouble();
		if (compat || protocol < 16) {
			y = in.readDouble();
			stance = in.readDouble();
		} else {
			y = in.readDouble();
			stance+= 1.62;
		}
		z = in.readDouble();
		onGround = in.readBoolean();
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeDouble(x);
		out.writeDouble(y - (compat || protocol >= 16 ? 1.62 : 0));
		out.writeDouble(stance);
		out.writeDouble(z);
		if (compat || protocol < 16)
			out.writeBoolean(onGround);
		else
			out.writeByte(0);
	}
}
