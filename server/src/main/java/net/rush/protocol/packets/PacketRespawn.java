package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PacketRespawn extends Packet {

	private int dimension;
	private byte difficulty;
	private byte gameMode;
	private short worldHeight;
	private String levelType;
	
	@Override
	public void read(ByteBuf in) throws IOException {
		dimension = in.readInt();
		if(compat) {
			difficulty = in.readByte();
			gameMode = in.readByte();
			worldHeight = in.readShort();
		} else {
			difficulty = (byte)in.readUnsignedByte();
			gameMode = (byte)in.readUnsignedByte();
		}
		levelType = readString(in, 65000, compat);
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeInt(dimension);
		out.writeByte(difficulty);
		out.writeByte(gameMode);
		if(compat)
			out.writeShort(worldHeight);
		writeString(levelType, out, compat);
	}

}
