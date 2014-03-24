package net.rush.packets.packet.impl;

import net.rush.packets.packet.AttachEntityPacket;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class AttachEntityPacketImpl extends AbstractPacket implements AttachEntityPacket {
    @Serialize(type = Type.INT, order = 0)
    private final int entityId;
    @Serialize(type = Type.INT, order = 1)
    private final int vehicleId;

    public AttachEntityPacketImpl(int entityId, int vehicleId) {
        super();
        this.entityId = entityId;
        this.vehicleId = vehicleId;
    }

    @Override
    public int getOpcode() {
        return 0x27;
    }

    @Override
    public int getEntityId() {
        return entityId;
    }

    @Override
    public int getVehicleId() {
        return vehicleId;
    }

    @Override
    public String getToStringDescription() {
        return String.format("entityId=\"%d\",vehicleId=\"%d\"", entityId, vehicleId);
    }
}
