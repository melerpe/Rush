package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.protocol.utils.RotationUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class EntityTeleportPacket extends Packet {

	private int entityId;
	private int x;
	private int y;
	private int z;
	private float yaw;
	private float pitch;

	private boolean onGround;
	
	/**
	 * @deprecated implement onGround
	 */
	public EntityTeleportPacket(int entityId, int x, int y, int z, float yaw, float pitch) {
		this(entityId, x, y, z, yaw, pitch, true);
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		if (compat || protocol < 16) 
			out.writeInt(entityId);
		else 
			writeByteInteger(out, entityId);
		writePositionAllIntegers(x, y, z, out);
		out.writeByte(RotationUtils.floatToByte(yaw));
		out.writeByte(RotationUtils.floatToByte(pitch));
		if (!compat && protocol >= 22) 
			out.writeBoolean(onGround);
	}
}
