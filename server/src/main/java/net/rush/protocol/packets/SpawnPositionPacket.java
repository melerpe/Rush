package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.model.Position;
import net.rush.protocol.Packet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SpawnPositionPacket extends Packet {

	private int x;
	private int y;
	private int z;

	public SpawnPositionPacket(Position pos) {
		super();
		x = (int) pos.x;
		y = (int) pos.y;
		z = (int) pos.z;
	}

	@Override
	public void read(ByteBuf in) throws IOException {
		x = in.readInt();
		y = in.readInt();
		z = in.readInt();
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		if (compat || protocol < 16) {
			writePositionAllIntegers(x, y, z, out);
		} else
			writePosition18(out, x, y, z);
	}
}
