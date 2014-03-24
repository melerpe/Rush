package net.rush.packets.packet.impl;

import net.rush.packets.packet.HandshakePacket;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class HandshakePacketImpl extends AbstractPacket implements HandshakePacket {
    @Serialize(type = Type.STRING, order = 0)
    private final String messageOrUsername;

    public HandshakePacketImpl(String messageOrName) {
        super();
        this.messageOrUsername = messageOrName;
    }

    @Override
    public String getMessageOrUsername() {
        return messageOrUsername;
    }

    @Override
    public int getOpcode() {
        return 2;
    }

    @Override
    public String getToStringDescription() {
        return String.format("message=\"%s\"", messageOrUsername);
    }
}
