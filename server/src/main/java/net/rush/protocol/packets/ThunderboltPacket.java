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
public class ThunderboltPacket extends Packet {

	private int entityId;
	private int thunderBoltId;
	private int x;
	private int y;
	private int z;

	@Override
	public void write(ByteBuf out) throws IOException {
		if(compat)
			out.writeInt(entityId);
		else
			writeVarInt(entityId, out);
		out.writeByte(thunderBoltId);
		out.writeInt(x * 32);
		out.writeInt(y * 32);
		out.writeInt(z * 32);
	}
}
