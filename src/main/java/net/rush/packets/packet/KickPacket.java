package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface KickPacket extends Packet {
    String getReason();
}
