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
public class BlockBreakAnimationPacket extends Packet {

	private int entityId;
	private int x;
	private int y;
	private int z;
	private int stage;

	public BlockBreakAnimationPacket(int entityId, Position pos, byte stage) {
		super();
		this.entityId = entityId;
		x = (int) pos.x;
		y = (int) pos.y;
		z = (int) pos.z;
		this.stage = stage;
	}

	@Override
	public void readCompat(ByteBuf in) throws IOException {
		entityId = in.readInt();
		x = in.readInt();
		y = in.readInt();
		z = in.readInt();
		stage = in.readByte();
	}

	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		out.writeInt(entityId);
		writePositionAllIntegers(x, y, z, out);
		out.writeByte(stage);
	}

	@Override
	public void read(ByteBuf in) throws IOException {
		entityId = in.readUnsignedByte();
		if (protocol < 16) {
			x = in.readInt();
			y = in.readUnsignedByte();
			z = in.readInt();
		} else {
			Position pos = readPosition18(in);
			x = pos.integerX();
			y = pos.integerY();
			z = pos.integerZ();
		}
		stage = (byte) in.readUnsignedByte();
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		writeVarInt(entityId, out);
		if (protocol < 16)
			writePositionYShort(x, y, z, out);
		else 
			writePosition18(out, x, y, z);
		out.writeByte(stage);
	}
}
