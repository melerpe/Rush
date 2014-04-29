package net.rush.packets.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class PlayerAbilitiesPacket extends Packet {
	public PlayerAbilitiesPacket() {
		// TODO Auto-generated constructor stub
	}

	@Serialize(type = Type.BYTE, order = 0)
	private byte flags;
	@Serialize(type = Type.FLOAT, order = 1)
	private float flySpeed;
	@Serialize(type = Type.FLOAT, order = 2)
	private float walkSpeed;

	public PlayerAbilitiesPacket(byte flags, float flySpeed, float walkSpeed) {
		super();
		this.flags = flags;
		this.flySpeed = flySpeed;
		this.walkSpeed = walkSpeed;
	}

	public int getOpcode() {
		return 0xCA;
	}

	public byte getFlags() {
		return flags;
	}

	public float getFlySpeed() {
		return flySpeed;
	}

	public float getWalkSpeed() {
		return walkSpeed;
	}

	public String getToStringDescription() {
		return String.format("flags=\"%b\",flySpeed=\"%b\",walkSpeed=\"%b\"",
				flags, flySpeed, walkSpeed);
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
