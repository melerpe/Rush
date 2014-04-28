package net.rush.packets.packet;

import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class KeepAlivePacket extends Packet {
	@Serialize(type = Type.INT, order = 0)
	private int token;

	public KeepAlivePacket() {}
	
	public KeepAlivePacket(int id) {
		super();
		token = id;
	}

	public int getOpcode() {
		return 0x00;
	}

	public int getToken() {
		return token;
	}

	public String getToStringDescription() {
		return String.format("id=\"%d\"", token);
	}
}
