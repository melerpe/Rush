package net.rush.packets.packet;

import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import net.rush.packets.Packet;

public class LoginSuccess extends Packet {

	private String uuid;
	private String name;

	public LoginSuccess() {
	}

	public LoginSuccess(String uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}

	@Override
	public String getToStringDescription() {
		return "uuid=" + uuid + ",name=" + name;
	}

	public int getOpcode() {
		return 2;
	}

	@Override
	public void write17(ByteBufOutputStream output) throws IOException {
		writeString(uuid, output, false);
		writeString(name, output, false);
	}

}
