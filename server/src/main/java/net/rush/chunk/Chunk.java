package net.rush.chunk;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.zip.Deflater;

import net.rush.Server;
import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.model.Position;
import net.rush.protocol.Packet;
import net.rush.protocol.packets.MapChunkPacket;
import net.rush.world.World;

/**
 * Represents a chunk of the map.
 */
public final class Chunk {

	/**
	 * The dimensions of a chunk.
	 */
	public static final int WIDTH = 16, HEIGHT = 16, DEPTH = 256;
	public static final int SIZE = WIDTH * HEIGHT * DEPTH;	

	/**
	 * The coordinates of this chunk.
	 */
	private final ChunkCoords coords;

	/**
	 * The data in this chunk representing all of the blocks and their state.
	 */
	public final byte[] types, metaData, skyLight, blockLight;

	//@SuppressWarnings("unchecked")
	//private Set<Entity>[] entities = new TreeSet[DEPTH / 16];

	public Chunk(ChunkCoords coords) {
		this(coords, new byte[SIZE]);
	}

	public boolean terrainPopulated = false;

	/**
	 * Creates a new chunk with a specified X and Z coordinate.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 */
	public Chunk(ChunkCoords coords, byte[] types) {
		this.coords = coords;
		this.types = types;
		this.metaData = new byte[SIZE];
		this.skyLight = new byte[SIZE];
		this.blockLight = new byte[SIZE];

		//for (int i = 0; i < entities.length; i++)
		//	entities[i] = new TreeSet<Entity>();
	}

	/**
	 * Gets the X coordinate of this chunk.
	 * @return The X coordinate of this chunk.
	 */
	public int getX() {
		return coords.x;
	}

	/**
	 * Gets the Z coordinate of this chunk.
	 * @return The Z coordinate of this chunk.
	 */
	public int getZ() {
		return coords.z;
	}

	/**
	 * Gets the type of a block within this chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param y The Y coordinate.
	 * @return The type.
	 */
	public int getType(int x, int y, int z) {
		return types[coordToIndex(x, y, z)];
	}

	public World getWorld() {
		return Server.getServer().getWorld();
	}

	/**
	 * Sets the types of all tiles within the chunk.
	 * @param types The array of types.
	 */
	public void setTypes(byte[] types) {
		if (types.length != WIDTH * HEIGHT * DEPTH)
			throw new IllegalArgumentException("Lenght of types (" + types.length + ") != chunk dimensions (" + (WIDTH * HEIGHT * DEPTH) + ")");

		System.arraycopy(types, 0, this.types, 0, types.length);
	}

	/**
	 * Sets the type of a block within this chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param y The Y coordinate.
	 * @param type The type.
	 */
	public void setType(int x, int y, int z, int type) {
		if (type < 0)
			throw new IllegalArgumentException();

		types[coordToIndex(x, y, z)] = (byte) type;
	}

	/**
	 * Gets the metadata of a block within this chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param y The Y coordinate.
	 * @return The metadata.
	 */
	public int getMetaData(int x, int y, int z) {
		return metaData[coordToIndex(x, y, z)];
	}

	/**
	 * Sets the metadata of a block within this chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param y The Y coordinate.
	 * @param metaData The metadata.
	 */
	public void setMetaData(int x, int y, int z, int metaData) {
		if (metaData < 0 || metaData > 15)
			throw new IllegalArgumentException("Metadata must be between 0 and 15");

		this.metaData[coordToIndex(x, y, z)] = (byte) metaData;
	}

	/**
	 * Gets the sky light level of a block within this chunk.
	 * @param x The X coordinate.
	 * @param y The Z coordinate.
	 * @param z The Y coordinate.
	 * @return The sky light level.
	 */
	public int getSkyLight(int x, int y, int z) {
		return skyLight[coordToIndex(x, y, z)];
	}

	/**
	 * Sets the sky light level of a block within this chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param y The Y coordinate.
	 * @param skyLight The sky light level.
	 */
	public void setSkyLight(int x, int y, int z, int skyLight) {
		if (skyLight < 0 || skyLight > 15)
			throw new IllegalArgumentException("Skylight must be between 0 and 15");

		this.skyLight[coordToIndex(x, y, z)] = (byte) skyLight;
	}

	/**
	 * Gets the block light level of a block within this chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param y The Y coordinate.
	 * @return The block light level.
	 */
	public int getBlockLight(int x, int y, int z) {
		return blockLight[coordToIndex(x, y, z)];
	}

	/**
	 * Sets the block light level of a block within this chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param y The Y coordinate.
	 * @param blockLight The block light level.
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
	 * Creates a new {@link Packet} which can be sent to a client to stream
	 * this chunk to them.
	 * @return The {@link MapChunkPacket}.
	 */
	public Packet toMessage() {
		return new MapChunkPacket(this, coords.x, coords.z, true, 0xFFFF, 0);
		//return new MapChunkPacketImpl(x * Chunk.WIDTH, z * Chunk.HEIGHT, 0, WIDTH, HEIGHT, DEPTH, serializeTileData());
	}

	/**
	 * Converts a three-dimensional coordinate to an index within the
	 * one-dimensional arrays.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @param y The Y coordinate.
	 * @return The index within the arrays.
	 */
	private int coordToIndex(int x, int y, int z) {
		if (x < 0 || z < 0 || y < 0 || x >= WIDTH || z >= HEIGHT || y >= DEPTH)
			throw new IndexOutOfBoundsException("Coords out of bound! x:" + x + ", z:" + z + ", y:" + y);

		return y << 8 | z << 4 | x;
	}

	Set<Position> tickedBlocks = new HashSet<Position>();

	public void tickAllBlocks(World world, Random rand) {
		tickedBlocks.clear();

		for(int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < DEPTH; y++) {
				for(int z = 0; z < HEIGHT; z++) {
					int type = getType(x, y, z);

					if(type == 0)
						continue;

					Block block = Block.byId[type];

					if(block != null)
						if(!tickedBlocks.contains(block)) {
							tickedBlocks.add(new Position(x, y, z));
							//if(block.getTickRandomly())
							block.tick(world, x * 16, y, z * 16, rand);
						}
				}
			}
		}
	}

	/*public void addEntity(Entity en) {
		int posX = MathHelper.floor_double(en.getPosition().getX() / 16D);
		int posZ = MathHelper.floor_double(en.getPosition().getZ() / 16D);

		if (posX != getX() || posZ != getZ()) {
			System.out.println("Wrong location! " + en.getPosition());
			Thread.dumpStack();
		}

		int posY = MathHelper.floor_double(en.getPosition().getY() / 16D);

		if (posY < 0)
			posY = 0;

		if (posY >= entities.length)
			posY = entities.length - 1;

		en.chunkPosition = new Position(getX(), posY, getZ());
		entities[posY].add(en);
	}*/

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
		byte[] data = new byte[(4096 + 2048 + 2048 + 2048 + 0) * 16 + 256];

		int pos = types.length;

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

		// skylight
		for (int i = 0; i < skyLight.length; i += 2) {
			byte light1 = 15;//skyLight[i];
			byte light2 = 15;//skyLight[i + 1];
			data[pos++] = (byte) ((light2 << 4) | light1);
		}

		// blocklight
		for (int i = 0; i < blockLight.length; i += 2) {
			byte light1 = 15;//blockLight[i];
			byte light2 = 15;//blockLight[i + 1];
			data[pos++] = (byte) ((light2 << 4) | light1);
		}

		// biome
		for (int i = 0; i < 256; i++)
			data[pos++] = 4; // biome data, just set it to forest

		if (pos != data.length)
			throw new IllegalStateException("Illegal Pos: " + pos + " vs " + data.length);

		if(compat || protocol < 22) {
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

