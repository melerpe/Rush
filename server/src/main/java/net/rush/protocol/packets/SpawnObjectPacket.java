package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.rush.model.Entity;
import net.rush.protocol.Packet;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SpawnObjectPacket extends Packet {

	public static enum ObjectType {
		BOAT(1),
		/** Use EntityMetadataPacket to send item type and data.*/
		ITEM(2),
		MINECART(10),	
		/** @deprecated unused since 1.6.x  */
		STORAGE_MINECART(11),
		/** @deprecated unused since 1.6.x  */
		POWERED_MINECART(12),
		ACTIVATED_TNT(50),
		ENDER_CRYSTAL(51),
		ARROW(61),
		SNOWBALL(62),
		EGG(63),
		FIRE_CHARGE(64),
		THROWN_ENDERPARL(65),
		WITHER_SKULL(66),
		FALLING_BLOCK(70),
		ITEM_FRAME(71),
		EYE_OF_ENDER(72),
		THROWN_POTION(73),
		FALLING_DRAGON_EGG(74),
		THROWN_EXP_BOTTLE(75),
		FISHING_BOAT(90);
		
		@Getter
		int id;
		ObjectType(int id) {
			this.id = id;
		}
		
		public static ObjectType fromId(int id) {
			for(ObjectType type : values())
				if(type.getId() == id)
					return type;
			throw new NullPointerException("Unknown object type ID " + id);
		}
	}

	private int entityId;
	private ObjectType type;
	private int x;
	private int y;
	private int z;
	private int pitch;
	private int yaw;
	private int throwerId;
	private int speedX;
	private int speedY;
	private int speedZ;

	/** To prevent typos, use inbuilt types ids. */
	public SpawnObjectPacket(Entity en, ObjectType type) {
		this(en, type, 0, 0, 0, 0);
	}

	/** To prevent typos, use inbuilt types ids. */
	public SpawnObjectPacket(Entity en, ObjectType type, int throwerId, double speedX, double speedY, double speedZ) {
		super();
		this.entityId = en.getId();
		this.type = type;
		this.x = en.getPosition().getPixelX();
		this.y =  en.getPosition().getPixelY();
		this.z = en.getPosition().getPixelZ();		
		this.pitch = en.getRotation().getIntPitch();
		this.yaw = en.getRotation().getIntYaw();

		this.throwerId = throwerId;

		if (throwerId == 0)
			return;

		double limit = 3.9;

		if (speedX < -limit)
			speedX = -limit;

		if (speedY < -limit)
			speedY = -limit;

		if (speedZ < -limit)
			speedZ = -limit;

		if (speedX > limit)
			speedX = limit;

		if (speedY > limit)
			speedY = limit;

		if (speedZ > limit)
			speedZ = limit;

		this.speedX = (int) (speedX * 8000);
		this.speedY = (int) (speedY * 8000);
		this.speedZ = (int) (speedZ * 8000);

	}

	@Override
	public void write(ByteBuf out) throws IOException {
		if(compat)
			out.writeInt(entityId);
		else
			writeVarInt(entityId, out);
		out.writeByte(type.getId());
		writePositionAllIntegers(x, y, z, out);
		out.writeByte(pitch);
		out.writeByte(yaw);
		out.writeInt(throwerId);

		if(throwerId > 0) {
			out.writeShort(speedX);
			out.writeShort(speedY);
			out.writeShort(speedZ);
		}
	}
}
