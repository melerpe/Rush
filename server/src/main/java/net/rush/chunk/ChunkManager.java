package net.rush.chunk;

import java.io.IOException;
import java.util.HashMap;

import net.rush.io.ChunkIoService;
import net.rush.world.World;
import net.rush.world.WorldGenerator;

/**
 * A class which manages the {@link Chunk}s currently loaded in memory.

 */
public final class ChunkManager {

	/**
	 * The chunk I/O service used to read chunks from the disk and write them to
	 * the disk.
	 */
	private final ChunkIoService service;

	private final World world;

	/**
	 * The world generator used to generate new chunks.
	 */
	public final WorldGenerator generator;

	/**
	 * A map of chunks currently loaded in memory.
	 */
	private final HashMap<ChunkCoords, Chunk> chunks = new HashMap<ChunkCoords, Chunk>();

	/** @deprecated limits object populating to one chunk, e.g. trees cannot grow out of the chunk */
	public static boolean decorating = false;
	/** @deprecated limits object populating to one chunk, e.g. trees cannot grow out of the chunk */
	public static Chunk ch = null;

	/**
	 * Creates a new chunk manager with the specified I/O service and world
	 * generator.
	 * @param service The I/O service.
	 * @param generator The world generator.
	 */
	public ChunkManager(World world, ChunkIoService service, WorldGenerator generator) {
		this.service = service;
		this.world = world;
		this.generator = generator;
	}

	/**
	 * Gets the chunk at the specified X and Z coordinates, loading it from the
	 * disk or generating it if necessary.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 * @return The chunk.
	 */
	public Chunk getChunk(int x, int z) {
		ChunkCoords key = new ChunkCoords(x, z);
		Chunk chunk = chunks.get(key);

		if (chunk == null) {
			try {
				chunk = service.read(x, z);
			} catch (IOException e) {
				chunk = null;
			}

			if (chunk == null) 
				chunk = generator.generate(world, x, z);
			
			chunks.put(key, chunk);
			
			if(decorating && ch != null)
				return ch;
				
			if(!decorating && !chunk.terrainPopulated) {
				chunk.terrainPopulated = true;
				ch = chunk;
				
				generator.populate(x, z);
			}
		}

		return chunk;
	}

	/*public Chunk getChunk(int x, int z) {
		ChunkCoords key = new ChunkCoords(x, z);
		Chunk chunk = chunks.get(key);

		if (chunk == null) {
			try {
				chunk = service.read(x, z);
			} catch (IOException e) {
				chunk = null;
			}

			if (chunk == null)
				chunk = generator.generate(world, x, z);

			chunks.put(key, chunk);

			if(decorating)
				return chunk;

			decorating = true;
			generator.populate(x, z);
			decorating = false;

		}
		return chunk;

	}*/

	public boolean chunkExists(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if (maxY >= 0 && minY < 256) {
			minX >>= 4;
		minZ >>= 4;
		maxX >>= 4;
		maxZ >>= 4;

		for (int x = minX; x <= maxX; ++x) {
			for (int z = minZ; z <= maxZ; ++z) {
				if (!chunkExist(x, z))
					return false;
			}
		}

		return true;
		}
		return false;
	}

	public boolean chunkExist(int x, int z) {
		Chunk chunk = chunks.get(new ChunkCoords(x, z));		

		if (chunk == null) {
			try {
				chunk = service.read(x, z);
			} catch (IOException e) {
			}
		}

		if (chunk == null)
			return false;
		else
			return true;
	}

	/**
	 * Saves all chunks loaded.
	 * @throws IOException if an I/O error occurs.
	 */
	public void saveAll() throws IOException {
		for (Chunk chunk: chunks.values())
			service.write(chunk.getX(), chunk.getZ(), chunk);
	}

}

