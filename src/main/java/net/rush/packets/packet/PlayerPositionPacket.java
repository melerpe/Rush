package net.rush.packets.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class PlayerPositionPacket extends Packet {
	public PlayerPositionPacket() {
		// TODO Auto-generated constructor stub
	}

	@Serialize(type = Type.DOUBLE, order = 0)
	private double x;
	@Serialize(type = Type.DOUBLE, order = 1)
	private double y;
	@Serialize(type = Type.DOUBLE, order = 2)
	private double stance;
	@Serialize(type = Type.DOUBLE, order = 3)
	private double z;
	@Serialize(type = Type.BOOL, order = 4)
	private boolean onGround;

	public PlayerPositionPacket(double x, double y, double stance, double z,
			boolean onGround) {
		super();
		this.x = x;
		this.y = y;
		this.stance = stance;
		this.z = z;
		this.onGround = onGround;
	}

	public int getOpcode() {
		return 0x0B;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getStance() {
		return stance;
	}

	public double getZ() {
		return z;
	}

	public boolean getOnGround() {
		return onGround;
	}

	public String getToStringDescription() {
		return String.format(
				"x=\"%f\",y=\"%f\",stance=\"%f\",z=\"%f\",onGround=\"%b\"", x,
				y, stance, z, onGround);
	}

	@Override
	public void read18(ByteBufInputStream input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write18(ByteBufOutputStream output) {
		// TODO Auto-generated method stub

	}
}
