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
public class UseBedPacket extends Packet {

	private int entityId;
	private int x;
	private int y;
	private int z;

	@Override
	public void write(ByteBuf out) throws IOException {
		if (compat || protocol < 16) {
			out.writeInt(entityId);
			if(compat)
				out.writeByte(0); // unknown byte 0
			writePositionYByte(x, y, z, out);
		} else {
			writeByteInteger(out, entityId);
			writePosition18(out, x, y, z);
		}
	}
}
