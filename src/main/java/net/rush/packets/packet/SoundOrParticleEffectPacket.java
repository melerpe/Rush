package net.rush.packets.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class SoundOrParticleEffectPacket extends Packet {

	public SoundOrParticleEffectPacket() {
		// TODO Auto-generated constructor stub
	}

	public static final int CLICK2 = 1000;

	public static final int CLICK1 = 1001;

	public static final int BOW_FIRE = 1002;

	public static final int RECORD_PLAY = 1005;

	public static final int DIG_SOUND = 2001;

	@Serialize(type = Type.INT, order = 0)
	private int effectId;
	@Serialize(type = Type.INT, order = 1)
	private int x;
	@Serialize(type = Type.BYTE, order = 2)
	private byte y;
	@Serialize(type = Type.INT, order = 3)
	private int z;
	@Serialize(type = Type.INT, order = 4)
	private int data;
	@Serialize(type = Type.BOOL, order = 5)
	private boolean relativeVolume;

	public SoundOrParticleEffectPacket(int effectId, int x, byte y, int z,
			int data, boolean relativeVolume) {
		super();
		this.effectId = effectId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.data = data;
		this.relativeVolume = relativeVolume;
	}

	public int getOpcode() {
		return 0x3D;
	}

	public int getEffectId() {
		return effectId;
	}

	public int getX() {
		return x;
	}

	public byte getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getData() {
		return data;
	}

	public boolean getRelativeVolume() {
		return relativeVolume;
	}

	public String getToStringDescription() {
		return String.format(
				"effectId=\"%d\",x=\"%d\",y=\"%d\",z=\"%d\",data=\"%d\"",
				effectId, x, y, z, data);
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
