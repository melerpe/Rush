package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface UpdateHealthPacket extends Packet {
    short getHealth();
    short getFood();
    float getSaturation();
}
