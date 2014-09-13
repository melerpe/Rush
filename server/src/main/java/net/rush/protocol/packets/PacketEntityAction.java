package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PacketEntityAction extends Packet {

	public static enum ActionType {
		ACTION_CROUCH(1),
		ACTION_UNCROUCH(2),
		ACTION_LEAVE_BED(3),
		START_SPRINTING(4),
		STOP_SPRINTING(5);
	
		@Getter
		int id;
		ActionType(int id) {
			this.id = id;
		}
		
		public static ActionType fromId(int id) {
			for (ActionType type : values())
				if(type.getId() == id)
					return type;
			throw new NullPointerException("Unknown action type ID " + id);
		}
	}

	private int entityId;
	private ActionType action;
	private int horseJumpBoost;

	@Override
	public void read(ByteBuf in) throws IOException {
		if (compat || protocol < 16) {
			entityId = in.readInt();
			action = ActionType.fromId(in.readByte());
			horseJumpBoost = in.readInt();
		} else {
			entityId = readVarInt(in);
			action = ActionType.fromId((byte) (in.readUnsignedByte() + 1));
			horseJumpBoost = readVarInt(in);
		}
	}
}
