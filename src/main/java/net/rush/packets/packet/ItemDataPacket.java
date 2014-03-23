package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface ItemDataPacket extends Packet {
    short getItemType();
    short getItemId();
    byte getDataLength();
    byte[] getData();
}
