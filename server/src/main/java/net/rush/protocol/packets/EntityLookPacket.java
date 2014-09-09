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
public class EntityLookPacket extends Packet {

	private int entityId;
	private int yaw;
	private int pitch;
	private boolean onGround;

	/**
	 * @deprecated implement onGround
	 */
	public EntityLookPacket(int entityId, int yaw, int pitch) {
		this(entityId, yaw, pitch, true);
	}
	
	@Override
	public void read(ByteBuf in) throws IOException {
		entityId = in.readInt();
		yaw = in.readByte();
		pitch = in.readByte();
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		out.writeInt(entityId);
		out.writeByte(yaw);
		out.writeByte(pitch);
		if (!compat && protocol >= 22)
			out.writeBoolean(onGround);
	}

}
