package net.rush.packets.packet;

import net.rush.packets.Packet;

// also called "MapColumnAllocation". However, I prefer the name "PreChunk" :P
public interface PreChunkPacket extends Packet {
    int getX();

    int getZ();

    boolean getMode();
}
