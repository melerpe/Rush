package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class HandshakePacket extends Packet {

	private int protocolVer;
	private String username;
	private String host;
	private int port;

	// 1.7
	private int state;

	public HandshakePacket(int protocolVer, String username, String host, int port) {
		super();
		this.protocolVer = protocolVer;
		this.username = username;
		this.host = host;
		this.port = port;
	}

	@Override
	public void readCompat(ByteBuf in) throws IOException {
		protocolVer = in.readByte();
		username = readString(in, Integer.MAX_VALUE, true);
		host = readString(in, Integer.MAX_VALUE, true);
		port = in.readInt();
	}

	@Override
	public void read(ByteBuf in) throws IOException {
		protocolVer = readVarInt(in);
		username = readString(in, 0, false);
		port = in.readUnsignedShort();
		state = readVarInt(in);
	}
}
