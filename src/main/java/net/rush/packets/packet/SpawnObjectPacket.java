package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface SpawnObjectPacket extends Packet {
    int getEntityId();
    byte getType();
    int getX();
    int getY();
    int getZ();
    int getFireballThrower();
    short getFireballSpeedX();
    short getFireballSpeedY();
    short getFireballSpeedZ();
}
