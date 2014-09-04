package net.rush.packets.packet;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

import net.rush.model.Position;
import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;

public class BlockBreakAnimationPacket extends Packet {

	public BlockBreakAnimationPacket() {
	}

	@Serialize(type = Type.INT, order = 0)
	private int entityId;
	@Serialize(type = Type.INT, order = 1)
	private int x;
	@Serialize(type = Type.INT, order = 2)
	private int y;
	@Serialize(type = Type.INT, order = 3)
	private int z;
	@Serialize(type = Type.BYTE, order = 4)
	private byte stage;

	public BlockBreakAnimationPacket(int entityId, Position pos, byte stage) {
		super();
		this.entityId = entityId;
		x = (int) pos.x;
		y = (int) pos.y;
		z = (int) pos.z;
		this.stage = stage;
	}

	public int getOpcode() {
		return 0x37;
	}

	public int getEntityId() {
		return entityId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public byte getStage() {
		return stage;
	}

	public String getToStringDescription() {
		return String.format("entityId=\"%d\",x=\"%d\",y=%d,z=%d,stage=%d", entityId, x, y, z, stage);
	}

	@Override
	public void read17(ByteBufInputStream input) throws IOException {
		entityId = input.readUnsignedByte();
		if (protocol < 16) {
			x = input.readInt();
			y = input.readUnsignedByte();
			z = input.readInt();
		} else {
			Position pos = readPosition(input);
			x = pos.integerX();
			y = pos.integerY();
			z = pos.integerZ();
		}
		stage = (byte) input.readUnsignedByte();
	}

	@Override
	public void write17(ByteBufOutputStream output) throws IOException {
		writeVarInt(entityId, output);
		 if (protocol < 16) {
			 output.writeInt(x);
			 output.writeShort(y);
			 output.writeInt(z);
		 } else {
			 writePosition(output, x, y, z);
		 }
		output.writeByte(stage);
	}
}
