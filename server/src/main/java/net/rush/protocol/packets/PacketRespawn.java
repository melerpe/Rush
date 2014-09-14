package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.util.enums.Dimension;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PacketRespawn extends Packet {

	private Dimension dimension;
	private Difficulty difficulty;
	private GameMode gameMode;
	private int worldHeight;
	private String levelType;
	
	@Override
	public void read(ByteBuf in) throws IOException {
		dimension = Dimension.fromId(in.readInt());
		if(compat) {
			difficulty = Difficulty.getByValue(in.readByte());
			gameMode = GameMode.getByValue(in.readByte());
			worldHeight = in.readShort();
		} else {
			difficulty = Difficulty.getByValue(in.readUnsignedByte());
			gameMode = GameMode.getByValue(in.readUnsignedByte());
		}
		levelType = readString(in, 65000, compat);
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeInt(dimension.getValue());
		out.writeByte(difficulty.getValue());
		out.writeByte(gameMode.getValue());
		if(compat)
			out.writeShort(worldHeight);
		writeString(levelType, out, compat);
	}

}
