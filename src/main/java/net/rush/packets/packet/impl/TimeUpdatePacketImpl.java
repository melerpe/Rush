package net.rush.packets.packet.impl;

import net.rush.packets.packet.TimeUpdatePacket;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class TimeUpdatePacketImpl extends AbstractPacket implements TimeUpdatePacket {
    @Serialize(type = Type.LONG, order = 0)
    private final long time;

    public TimeUpdatePacketImpl(long time) {
        super();
        this.time = time;
    }

    @Override
    public int getOpcode() {
        return 0x04;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public String getToStringDescription() {
        return String.format("time=\"%d\"", time);
    }
}
