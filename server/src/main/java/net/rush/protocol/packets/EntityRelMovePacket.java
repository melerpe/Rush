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
public class EntityRelMovePacket extends Packet {

	private int entityId;
	private byte diffX;
	private byte diffY;
	private byte diffZ;

	private boolean onGround;

	/**
	 * @deprecated implement onGround
	 */
	public EntityRelMovePacket(int entityId, byte diffX, byte diffY, byte diffZ) {
		this(entityId, diffX, diffY, diffZ, true);
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeInt(entityId);
		out.writeByte(diffX);
		out.writeByte(diffY);
		out.writeByte(diffZ);
		if(!compat && protocol > 22)
			out.writeBoolean(onGround);
	}
}
