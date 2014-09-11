package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.GameMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.util.JsonUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PlayerListItemPacket extends Packet {

	private String playerName;
	private boolean onlineStatus;
	private int ping;

	private GameMode gamemode;
	private ListTypeAction action;

	public PlayerListItemPacket(String playerName, GameMode gamemode, boolean onlineStatus, int ping) {
		this.playerName = playerName;
		this.onlineStatus = onlineStatus;
		this.ping = ping;

		this.gamemode = gamemode;
		this.action = ListTypeAction.ADD_PLAYER;
	}

	public static enum ListTypeAction {
		ADD_PLAYER(0),
		UPDATE_GAMEMODE(1),
		UPDATE_LATENCY(2),
		UPDATE_DISPLAY_NAME(3),
		REMOVE_PLAYER(4);

		int id;
		private ListTypeAction(int id) {
			this.id = id;
		}
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		if(compat || protocol < 22) {
			writeString(playerName, out, compat);
			out.writeBoolean(onlineStatus);
			out.writeShort(ping);
		} else {
			writeVarInt(action.id, out);
			writeVarInt(1, out);
			writeUuid(out, UUID.fromString("0-0-0-0-0"));

			switch (action) {
			case ADD_PLAYER:
				writeString(playerName, out, false);
				writeVarInt(0, out); // 0 properties
				writeVarInt(gamemode.getValue(), out);
				writeVarInt(ping, out);
				out.writeBoolean(onlineStatus);
				if(onlineStatus)
					writeString(JsonUtils.plainMessageToJson(playerName), out, false);
				break;
			case UPDATE_GAMEMODE:
				writeVarInt(gamemode.getValue(), out);
				break;
			case UPDATE_LATENCY:
				writeVarInt(ping, out);
				break;
			case UPDATE_DISPLAY_NAME:
				out.writeBoolean(onlineStatus);
				if(onlineStatus)
					writeString(JsonUtils.plainMessageToJson(playerName), out, false);
				break;
			case REMOVE_PLAYER:
				break;
			}
		}
	}
}
