package net.rush.packets.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class PlayerOnGroundPacket extends Packet {
	public PlayerOnGroundPacket() {
		// TODO Auto-generated constructor stub
	}

	@Serialize(type = Type.BOOL, order = 0)
	private boolean onGround;

	public PlayerOnGroundPacket(boolean onGround) {
		super();
		this.onGround = onGround;
	}

	public int getOpcode() {
		return 0x0A;
	}

	public boolean getOnGround() {
		return onGround;
	}

	public String getToStringDescription() {
		return String.format("onGround=\"%b\"", onGround);
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
