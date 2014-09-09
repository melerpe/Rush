package net.rush.protocol.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.rush.chunk.Chunk;
import net.rush.protocol.Packet;
import net.rush.util.BlockDebreakifier;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MapChunkPacket extends Packet {

	private int x;
	private int z;
	private boolean groundUpContinuous;
	private int primaryBitMap;
	private int addBitMap;
	private byte[] compressedChunkData;

	/**
	 * @deprecated broken on 1.8
	 */
	private Chunk ch;

	public MapChunkPacket(Chunk ch, int x, int z, boolean groundUpContinuous, int primaryBitMap, int addBitMap) {
		super();
		this.ch = ch;
		this.x = x;
		this.z = z;
		this.groundUpContinuous = groundUpContinuous;
		this.primaryBitMap = primaryBitMap;
		this.addBitMap = addBitMap;
		this.compressedChunkData = ch.serializeTileData(compat, protocol);
	}

	@Override
	public void write(ByteBuf output) throws IOException {
		output.writeInt(x);
		output.writeInt(z);
		output.writeBoolean(groundUpContinuous);
		output.writeShort(primaryBitMap);

		if (compat || protocol < 27) {
			output.writeShort(addBitMap);
			output.writeInt(compressedChunkData.length);
			output.writeBytes(compressedChunkData);
		} else {
			writeVarInt(compressedChunkData.length, output);
			output.writeBytes(compressedChunkData);
		}
	}
}
