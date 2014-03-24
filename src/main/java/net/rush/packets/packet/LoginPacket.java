package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface LoginPacket extends Packet {
    int getProtocolVersionOrEntityId();

    String getUsername();

    String getLevelType();

    int getMode();

    int getDimension();

    byte getDifficulty();

    int getWorldHeight();

    int getMaxPlayers();
}
