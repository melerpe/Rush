package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface EntityActionPacket extends Packet {
	
	byte CROUCH = 1;
	byte UNCROUCH = 2;
	byte LEAVE_BED = 3;
	byte START_SPRINTING = 4;
	byte STOP_SPRINTING = 5;
	
    int getEntityId();
    byte getActionId();
}
