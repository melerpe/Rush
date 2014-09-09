package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.model.Position;
import net.rush.protocol.Packet;
import net.rush.protocol.utils.MetaParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SpawnMobPacket extends Packet {

	private int entityId;
	private byte entityType;
	private int x;
	private int y;
	private int z;
	private byte yaw;
	private byte pitch;
	private byte headYaw;
	private short velocityX;
	private short velocityY;
	private short velocityZ;
	private MetaParam<?>[] metadata;

	public SpawnMobPacket(int entityId, byte entityType, Position pos, byte yaw, byte pitch, byte headYaw, Position velocity, MetaParam<?>[] metadata) {
		super();
		this.entityId = entityId;
		this.entityType = entityType;
		x = (int) pos.x;
		y = (int) pos.y;
		z = (int) pos.z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.headYaw = headYaw;
		velocityX = (short) velocity.x;
		velocityY = (short) velocity.y;
		velocityZ = (short) velocity.z;
		this.metadata = metadata;
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		if(compat)
			out.writeInt(entityId);
		else
			writeVarInt(entityId, out);
		out.writeByte(entityType);
		writePositionAllIntegers(x, y, z, out);
		out.writeByte(pitch);
		out.writeByte(headYaw);
		out.writeByte(yaw);
		out.writeShort(velocityX);
		out.writeShort(velocityY);
		out.writeShort(velocityZ);
		writeMetadata(out, metadata);
	}
}
