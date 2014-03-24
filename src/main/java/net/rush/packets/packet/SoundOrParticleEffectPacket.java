package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface SoundOrParticleEffectPacket extends Packet {
    int getEffectId();
    int getX();
    byte getY();
    int getZ();
    int getData();
}
