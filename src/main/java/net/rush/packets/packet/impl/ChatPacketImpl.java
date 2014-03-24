package net.rush.packets.packet.impl;

import net.rush.packets.packet.ChatPacket;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class ChatPacketImpl extends AbstractPacket implements ChatPacket {
	@Serialize(type = Type.STRING, order = 0)
	private final String message;
	
	public ChatPacketImpl(String message) {
		super();
		this.message = message;
	}

	@Override
	public int getOpcode() {
		return 0x03;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getToStringDescription() {
		return String.format("message=\"%s\"", message);
	}
}
