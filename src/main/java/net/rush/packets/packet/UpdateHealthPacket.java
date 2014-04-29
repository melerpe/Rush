package net.rush.packets.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class UpdateHealthPacket extends Packet {
	public UpdateHealthPacket() {
		// TODO Auto-generated constructor stub
	}

	@Serialize(type = Type.FLOAT, order = 0)
	private float health;
	@Serialize(type = Type.SHORT, order = 1)
	private short food;
	@Serialize(type = Type.FLOAT, order = 2)
	private float saturation;

	public UpdateHealthPacket(float health, short food, float saturation) {
		super();
		this.health = health;
		this.food = food;
		this.saturation = saturation;
	}

	public int getOpcode() {
		return 0x08;
	}

	public float getHealth() {
		return health;
	}

	public short getFood() {
		return food;
	}

	public float getSaturation() {
		return saturation;
	}

	public String getToStringDescription() {
		return String.format("health=\"%d\",food=\"%d\",saturation=\"%d\"",
				health, food, saturation);
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
