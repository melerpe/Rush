package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.util.enums.Dimension;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class LoginPacket extends Packet {

	private int entityId;
	private String worldType;
	private GameMode gameMode;
	private Dimension dimension;
	private Difficulty difficulty;
	private int worldHeight;
	private int maxPlayers;

	private boolean hardcore = false;


	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		out.writeInt(entityId);
		writeString(worldType, out, true);
		out.writeByte(gameMode.getValue());
		out.writeByte(dimension.getValue());
		out.writeByte(difficulty.getValue());
		out.writeByte(worldHeight);
		out.writeByte(maxPlayers);
	}
	
	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeInt(entityId);
		int gamemode = gameMode.getValue();

		if (hardcore)
			gamemode |= 8;

		out.writeByte(gamemode);

		out.writeByte(dimension.getValue());
		out.writeByte(difficulty.getValue());
		out.writeByte(maxPlayers);
		writeString(worldType == null ? "" : worldType, out, false);
		
		if (protocol >= 29)
			out.writeBoolean(false);
	}
}
