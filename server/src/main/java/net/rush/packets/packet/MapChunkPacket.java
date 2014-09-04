package net.rush.packets.packet;

import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;
import java.util.zip.Deflater;

import net.rush.chunk.Chunk;
import net.rush.packets.Packet;
import net.rush.packets.serialization.Serialize;
import net.rush.packets.serialization.Type;
import net.rush.util.BlockDebreakifier;

public class MapChunkPacket extends Packet {

	public MapChunkPacket() {
	}

	@Serialize(type = Type.INT, order = 0)
	private int x;
	@Serialize(type = Type.INT, order = 1)
	private int z;
	@Serialize(type = Type.BOOL, order = 2)
	private boolean groundUpContinuous;
	@Serialize(type = Type.UNSIGNED_SHORT, order = 3)
	private int primaryBitMap;
	@Serialize(type = Type.UNSIGNED_SHORT, order = 4)
	private int addBitMap;
	@Serialize(type = Type.INT, order = 5)
	private int compressedSize;
	@Serialize(type = Type.BYTE_ARRAY, order = 6, moreInfo = 5)
	private byte[] compressedChunkData;
	
	private Chunk ch;

	public MapChunkPacket(Chunk chunk) {
		super();
		this.x = chunk.getX();
		this.z = chunk.getZ();
		this.groundUpContinuous = true;
		this.primaryBitMap = 0xFFFF;
		this.addBitMap = 0;

		this.ch = chunk;
		byte[] data = serializeTileData();

		this.compressedSize = data.length;
		compressedChunkData = data;
	}

	public int getOpcode() {
		return 0x33;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public boolean getGroundUpContinuous() {
		return groundUpContinuous;
	}

	public int getPrimaryBitMap() {
		return primaryBitMap;
	}

	public int getAddBitMap() {
		return addBitMap;
	}

	public int getCompressedSize() {
		return compressedSize;
	}

	public byte[] getCompressedChunkData() {
		return compressedChunkData;
	}

	public String getToStringDescription() {
		return String.format("x=\"%d\",z=\"%d\",groundUpContinuous=\"%b\",primaryBitMap=\"%d\",addBitMap=\"%d\",compressedSize=\"%d\",chunkData=byte[%d]", x, z, groundUpContinuous, primaryBitMap, addBitMap, compressedSize,
				compressedChunkData.length);
	}

	/*@Override
	public void read17(ByteBufInputStream input) throws IOException {
		x = input.readInt();
		z = input.readInt();
		groundUpContinuous = input.readBoolean();
		primaryBitMap = input.readUnsignedShort();
		addBitMap = input.readUnsignedShort();
		compressedSize = input.readInt();

		byte[] bytes = new byte[5];
		input.readFully(bytes);

		compressedChunkData = bytes;

		/*if (buildBuffer.length < compressedSize) {
		    buildBuffer = new byte[compressedSize];
		}

		input.readFully(buildBuffer, 0, compressedSize);
		int i = 0;

		int j;

		for (j = 0; j < 16; ++j) {
		    i += this.primaryBitMap >> j & 1;
		}

		j = 12288 * i;
		if (this.groundUpContinuous) {
		    j += 256;
		}

		this.compressedChunkData = new byte[j];
		Inflater inflater = new Inflater();

		inflater.setInput(buildBuffer, 0, compressedSize);

		try {
		    inflater.inflate(this.compressedChunkData);
		} catch (DataFormatException dataformatexception) {
		    throw new IOException("Bad compressed data format");
		} finally {
		    inflater.end();
		}*/
	//}

	@Override
	public void write17(ByteBufOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeInt(z);
		output.writeBoolean(groundUpContinuous);
		output.writeShort(primaryBitMap);

		if (protocol < 27) {
			output.writeShort(addBitMap);
			output.writeInt(compressedSize);
			output.write(serializeTileData());
		} else {
			writeVarInt(compressedSize, output);
			output.write(serializeTileData());
		}
	}

	private byte[] serializeTileData() {
		// (types + metaData + blocklight + skylight + add) * 16 vanilla-chunks + biome
		byte[] data = new byte[(4096 + 2048 + 2048 + 2048 + 0) * 16 + 256];

		int pos = ch.types.length;

		// types
		if (protocol < 27)
			System.arraycopy(ch.types, 0, data, 0, ch.types.length);
		else
			for (int i = 0; i < ch.types.length; i += 2) {
				byte type = ch.types[i];
				data[pos++] = (byte) BlockDebreakifier.getCorrectedData(type, ch.metaData[i]);
			}

		if (pos != ch.types.length)
			throw new IllegalStateException("Illegal pos: " + pos + " vs " + ch.types.length);

		// metadata
		for (int i = 0; i < ch.metaData.length; i += 2) {
			byte meta1 = ch.metaData[i];
			byte meta2 = ch.metaData[i + 1];
			data[pos++] = (byte) ((meta2 << 4) | meta1);
		}

		// skylight
		for (int i = 0; i < ch.skyLight.length; i += 2) {
			byte light1 = 15;//skyLight[i];
			byte light2 = 15;//skyLight[i + 1];
			data[pos++] = (byte) ((light2 << 4) | light1);
		}

		// blocklight
		for (int i = 0; i < ch.blockLight.length; i += 2) {
			byte light1 = 15;//blockLight[i];
			byte light2 = 15;//blockLight[i + 1];
			data[pos++] = (byte) ((light2 << 4) | light1);
		}

		// biome
		for (int i = 0; i < 256; i++)
			data[pos++] = 4; // biome data, just set it to forest

		if (pos != data.length)
			throw new IllegalStateException("Illegal Pos: " + pos + " vs " + data.length);

		//if (protocol < 27) {
			// we are done, now compress it
			Deflater deflater = new Deflater(Deflater.BEST_SPEED);
			deflater.setInput(data);
			deflater.finish();

			byte[] compressed = new byte[data.length];
			int length = deflater.deflate(compressed);

			deflater.end();

			byte[] realCompressed = new byte[length];

			for (int i = 0; i < length; i++)
				realCompressed[i] = compressed[i];

			return realCompressed;
		//}

		//return data;
	}
}
