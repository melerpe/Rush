package net.rush.packets.packet;

import net.rush.packets.Packet;

public interface PlayerAbilitiesPacket extends Packet {
    boolean getInvulnerability(); // not sure
    boolean getIsFlying();
    boolean getCanFly();
    boolean getInstantDestroy(); // not sure
}
