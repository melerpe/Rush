package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.protocol.Packet;
import net.rush.util.BlockDebreakifier;
import net.rush.world.World;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class BlockChangePacket extends Packet {

	private int x;
	private int y;
	private int z;
	private int blockType;
	private int blockMetadata;

	public BlockChangePacket(int x, int y, int z, World world) {
		this(x, y, z, world.getType(x, y, z), world.getBlockData(x, y, z));
	}
	
	@Override
	public void writeCompat(ByteBuf out) throws IOException {
		writePositionYByte(x, y, z, out);
		out.writeShort(blockType);
		out.writeByte(blockMetadata);
	}

	@Override
	public void write(ByteBuf out) throws IOException {
		if (protocol < 25) {
			writePositionYByte(x, y, z, out);
			writeVarInt(blockType, out);
			out.writeByte(blockMetadata);
		} else {
			writePosition18(out, x, y, z);
			blockMetadata = (byte) BlockDebreakifier.getCorrectedData(blockType, blockMetadata);
			writeByteInteger(out, (blockType << 4) | blockMetadata);
		}
	}
}
