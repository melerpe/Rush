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
public class AnimationPacket extends Packet {

	public static enum AnimType { 
		SWING_ARM(1),
		DAMAGE_ANIMATION(2),
		BED_LEAVE(3),
		EAT_FOOD(5),
		CRITICAL_EFFECT(6),
		MAGIC_CRITICAL_EFFECT(7),
		UNKNOWN(102),
		CROUNCH(104),
		UNCROUNCH(105);
		
		@Getter
		private int id;		
		AnimType(int id) {
			this.id = id;
		}
		
		public static AnimType fromId(int id) {
			for(AnimType type : values())
				if(type.getId() == id)
					return type;
			throw new NullPointerException("Unknown animation of id " + id);
		}
	}

	private int entityId;
	private AnimType animation;

	@Override
	public void readCompat(ByteBuf in) throws IOException {
		entityId = in.readInt();
		animation = AnimType.fromId(in.readByte());
	}

	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		out.writeInt(entityId);
		out.writeByte(animation.getId());
	}

	@Override
	public void read(ByteBuf in) throws IOException {
		if (protocol < 16) {
			entityId = in.readInt();
			animation = AnimType.fromId(in.readByte());
		} else {
			animation = AnimType.SWING_ARM;
		}
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		writeVarInt(entityId, out);
		out.writeByte(toNewId(animation.getId()));
	}

	private int toNewId(int id) {
		if(id == 2)
			return 1;
		if(id == 1)
			return 0;
		return id-= 2;
	}
}
