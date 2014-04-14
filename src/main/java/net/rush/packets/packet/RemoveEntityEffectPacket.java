package net.rush.packets.packet;

import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class RemoveEntityEffectPacket extends Packet {
	@Serialize(type = Type.INT, order = 0)
	private final int entityId;
	@Serialize(type = Type.BYTE, order = 1)
	private final byte effectId;

	public RemoveEntityEffectPacket(int entityId, byte effectId) {
		super();
		this.entityId = entityId;
		this.effectId = effectId;
	}

	public int getOpcode() {
		return 0x2A;
	}

	public int getEntityId() {
		return entityId;
	}

	public byte getEffectId() {
		return effectId;
	}

	public String getToStringDescription() {
		return String.format("entityId=\"%d\",effect=\"%d\"", entityId, effectId);
	}
}
