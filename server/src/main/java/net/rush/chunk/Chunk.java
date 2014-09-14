package net.rush.chunk;

import java.util.zip.Deflater;

import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.protocol.Packet;
import net.rush.protocol.packets.PacketMapChunk;
import net.rush.util.BlockDebreakifier;

/**
 * Represents a chunk of the map.
 */
public final class Chunk {

	/**
	 * The dimensions of a chunk.
	 */
	public static final int WIDTH = 16, HEIGHT = 16, DEPTH = 256;
	public static final int SIZE = WIDTH * HEIGHT * DEPTH;	

	private final ChunkCoords coords;

	/**
	 * The data in this chunk representing all of the blocks and their state.
	 */
	public final byte[] types, metaData, skyLight, blockLight;

	public boolean terrainPopulated = false;

	/**
	 * Creates a new chunk with a specified X and Z coordinate.
	 */
	public Chunk(ChunkCoords coords) {
		this(coords, new byte[SIZE]);
	}

	/**
	 * Creates a new chunk with a specified X and Z coordinate and provided block array.
	 */
	public Chunk(ChunkCoords coords, byte[] types) {
		this.coords = coords;
		this.types = types;
		this.metaData = new byte[SIZE];
		this.skyLight = new byte[SIZE];
		this.blockLight = new byte[SIZE];
	}

	public int getX() {
		return coords.x;
	}

	public int getZ() {
		return coords.z;
	}

	/**
	 * Gets the type of a block within this chunk.
	 */
	public int getType(int x, int y, int z) {
		return types[coordToIndex(x, y, z)];
	}

	/**
	 * Sets the types of all blocks within the chunk.
	 * @param types The array of all block types.
	 */
	public void setTypes(byte[] types) {
		if (types.length != WIDTH * HEIGHT * DEPTH)
			throw new IllegalArgumentException("Lenght of types (" + types.length + ") != chunk dimensions (" + (WIDTH * HEIGHT * DEPTH) + ")");

		System.arraycopy(types, 0, this.types, 0, types.length);
	}

	/**
	 * Sets the type of a block within this chunk.
	 */
	public void setType(int x, int y, int z, int type) {
		if (type < 0)
			throw new IllegalArgumentException();

		types[coordToIndex(x, y, z)] = (byte) type;
	}

	/**
	 * Gets the metadata of a block within this chunk.
	 */
	public int getMetaData(int x, int y, int z) {
		return metaData[coordToIndex(x, y, z)];
	}

	/**
	 * Sets the metadata of a block within this chunk.
	 */
	public void setMetaData(int x, int y, int z, int metaData) {
		if (metaData < 0 || metaData > 15)
			throw new IllegalArgumentException("Metadata must be between 0 and 15");

		this.metaData[coordToIndex(x, y, z)] = (byte) metaData;
	}

	/**
	 * Gets the sky light level of a block within this chunk.
	 */
	public int getSkyLight(int x, int y, int z) {
		return skyLight[coordToIndex(x, y, z)];
	}

	/**
	 * Sets the sky light level of a block within this chunk.
	 */
	public void setSkyLight(int x, int y, int z, int skyLight) {
		if (skyLight < 0 || skyLight > 15)
			throw new IllegalArgumentException("Skylight must be between 0 and 15");

		this.skyLight[coordToIndex(x, y, z)] = (byte) skyLight;
	}

	/**
	 * Gets the block light level of a block within this chunk.
	 */
	public int getBlockLight(int x, int y, int z) {
		return blockLight[coordToIndex(x, y, z)];
	}

	/**
	 * Sets the block light level of a block within this chunk.
	 */
	public void setBlockLight(int x, int y, int z, int blockLight) {
		if (blockLight < 0 || blockLight > 15)
			throw new IllegalArgumentException("Blocklight must be between 0 and 15");

		this.blockLight[coordToIndex(x, y, z)] = (byte) blockLight;
	}

	public void setTypeAndData(int x, int y, int z, int type, int data) {
		setType(x, y, z, type);
		setMetaData(x, y, z, data);
	}

	/**
	 * Creates a new {@link Packet} which can be sent to a client to stream this chunk to them.
	 */
	public Packet toMessage() {
		return new PacketMapChunk(this);
		//return new MapChunkPacketImpl(x * Chunk.WIDTH, z * Chunk.HEIGHT, 0, WIDTH, HEIGHT, DEPTH, serializeTileData());
	}

	/**
	 * Converts a three-dimensional coordinate to an index within the
	 * one-dimensional arrays.
	 * @return The index within the arrays.
	 */
	private int coordToIndex(int x, int y, int z) {
		if (x < 0 || z < 0 || y < 0 || x >= WIDTH || z >= HEIGHT || y >= DEPTH)
			throw new IndexOutOfBoundsException("Coords out of bound! x:" + x + ", z:" + z + ", y:" + y);

		return y << 8 | z << 4 | x;
	}	

	public int getTerrainHeight(int x, int z) {
		for (int y = DEPTH - 1; y > 0; --y) {
			int blockId = getType(x, y, z);

			if (blockId != Block.AIR.id)
				return y + 1;
		}
		return 0;
	}

	public Material getMaterial(int x, int y, int z) {
		int blockId = getType(x, y, z);

		return blockId == 0 ? Material.AIR : Block.byId[blockId].material;

	}

	public byte[] serializeTileData(boolean compat, int protocol) {
		// (types + metaData + blocklight + skylight + add) * 16 vanilla-chunks + biome
		byte[] data;

		int pos = types.length;

		if(compat || protocol < 24) {
			data = new byte[(4096 + 2048 + 2048 + 2048 + 0) * 16 + 256];
			// types
			System.arraycopy(types, 0, data, 0, types.length);

			if (pos != types.length)
				throw new IllegalStateException("Illegal pos: " + pos + " vs " + types.length);

			// metadata
			for (int i = 0; i < metaData.length; i += 2) {
				byte meta1 = metaData[i];
				byte meta2 = metaData[i + 1];
				data[pos++] = (byte) ((meta2 << 4) | meta1);
			}
		} else {
			data = new byte[(4096 + 2048 + 2048 + 2048 + 0) * 32 + 256];
			for (int i = 0; i < types.length; i++) {
				int id = types[i] & 0xFF;

				int px = i & 0xF;
				int py = (i >> 8) & 0xF;
				int pz = (i >> 4) & 0xF;

				int blockData = getMetaData(px, py, pz);

				//if (id == 90 && blockData == 0) {
				//	Blocks.PORTAL.updateShape(chunk.world, (chunk.locX << 4) + px, (l << 4) + py, (chunk.locZ << 4) + pz);
				//} else {
				blockData = BlockDebreakifier.getCorrectedData(id, blockData);
				//}
				int val = id << 4 | blockData;
				data[pos++] = (byte) (val & 0xFF);
				data[pos++] = (byte) ((val >> 8) & 0xFF );
			}
		}

		if(compat || protocol < 24) {
			// skylight TODO
			for (int i = 0; i < skyLight.length; i += 2) {
				byte light1 = 15; //skyLight[i];
				byte light2 = 15; //skyLight[i + 1];				
				data[pos++] = (byte) ((light2 << 4) | light1);
			}

			// blocklight TODO
			for (int i = 0; i < blockLight.length; i += 2) {
				byte light1 = 15; //blockLight[i];
				byte light2 = 15; //blockLight[i + 1];
				data[pos++] = (byte) ((light2 << 4) | light1);
			}


			// biome
			for (int i = 0; i < 256; i++)
				data[pos++] = 4; // TODO biome data, just set it to forest

			if (pos != data.length)
				throw new IllegalStateException("Illegal Pos: " + pos + " vs " + data.length);

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
		}
		return data;
	}
}

