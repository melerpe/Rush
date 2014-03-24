package net.rush.packets.packet.impl;

import net.rush.packets.packet.DestroyEntityPacket;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class DestroyEntityPacketImpl extends AbstractPacket implements DestroyEntityPacket {
    @Serialize(type = Type.INT, order = 0)
    private final int entityId;

    public DestroyEntityPacketImpl(int entityId) {
        super();
        this.entityId = entityId;
    }

    @Override
    public int getOpcode() {
        return 0x1D;
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public String getToStringDescription() {
        return String.format("entityId=\"%d\"", entityId);
    }
}
