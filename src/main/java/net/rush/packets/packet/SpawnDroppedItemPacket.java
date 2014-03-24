package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface SpawnDroppedItemPacket extends Packet {
    int getEntityId();
    short getItemId();
    byte getCount();
    short getDataValue();
    int getX();
    int getY();
    int getZ();
    byte getRotation();
    byte getPitch();
    byte getRoll();
}
