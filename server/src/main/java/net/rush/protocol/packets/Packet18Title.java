package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.util.JsonUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Packet18Title extends Packet {
	
	public static enum TitleAction {
		TITLE(0),
		SUBTITLE(1),
		TIMES(2),
		CLEAR(3),
		RESET(4);
		
		int id;
		TitleAction(int id) {
			this.id = id;
		}
	}
	
	public Packet18Title (TitleAction action, String text) {
		this.action = action;
		this.text = text;
	}
	
	public Packet18Title (TitleAction action, int fadeIn, int stay, int fadeOut) {
		this.action = action;
		this.fadeIn = fadeIn;
		this.stay = stay;
		this.fadeOut = fadeOut;
	}
	
	private TitleAction action;
	private String text;
	
	private int fadeIn;
	private int stay;
	private int fadeOut;

	@Override
	public void write(ByteBuf out) throws IOException {
		writeVarInt(action.id, out);
		
		switch (action) {
		case TITLE:
			writeString(JsonUtils.plainMessageToJson(text), out, false);
			break;
		case SUBTITLE:
			writeString(JsonUtils.plainMessageToJson(text), out, false);
			break;
		case TIMES:
			out.writeInt(fadeIn);
			out.writeInt(stay);
			out.writeInt(fadeOut);
			break;
		case CLEAR:
			break;
		case RESET:
			break;
		default:
			throw new RuntimeException("Unknown action ID" + action.id);
		}
	}
}