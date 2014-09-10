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
public class SpawnPaintingPacket extends Packet {

	private int entityId;
	private String title;
	private int x;
	private int y;
	private int z;
	private int direction;

	@Override
	public void write(ByteBuf out) throws IOException {
		if(compat)
			out.writeInt(entityId);
		else
			writeVarInt(entityId, out);
		writeString(title, out, compat);
		writePositionAllIntegers(x, y, z, out);
		out.writeInt(direction);
	}
}
