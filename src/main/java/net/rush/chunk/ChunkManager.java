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

	public int currChunkX = -999999999;
	public int currChunkZ = -999999999;
	public Chunk currChunk;

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
	/*public Chunk getChunk(int x, int z) {
		ChunkCoords key = new ChunkCoords(x, z);
		Chunk chunk = chunks.get(key);

		if(chunk == null && decorated != null)
			return decorated;

		if (chunk == null) {
			try {
				chunk = service.read(x, z);
			} catch (IOException e) {
				chunk = null;
			}

			if (chunk == null) 
				chunk = generator.generate(world, x, z);

			chunks.put(key, chunk);
		}

		return chunk;
	}*/
	
	public Chunk getChunk(int x, int z) {
		if (currChunk != null /*&& x == currChunkX && z == currChunkZ*/)
			return currChunk;

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
			
			//if (!chunk.terrainPopulated && this.chunkExist(x + 1, z + 1) && this.chunkExist(x, z + 1) && this.chunkExist(x + 1, z))
			currChunk = chunk;
			this.populate(chunk, x, z);
			currChunk = null;
			
			/*if (this.chunkExist(x - 1, z) && !this.getChunk(x - 1, z).terrainPopulated && this.chunkExist(x - 1, z + 1) && this.chunkExist(x, z + 1) && this.chunkExist(x - 1, z))
				this.populate(chunk,x - 1, z);

			if (this.chunkExist(x, z - 1) && !this.getChunk(x, z - 1).terrainPopulated && this.chunkExist(x + 1, z - 1) && this.chunkExist(x, z - 1) && this.chunkExist(x + 1, z))
				this.populate(chunk, x, z - 1);

			if (this.chunkExist(x - 1, z - 1) && !this.getChunk(x - 1, z - 1).terrainPopulated && this.chunkExist(x - 1, z - 1) && this.chunkExist(x, z - 1) && this.chunkExist(x - 1, z))
				this.populate(chunk, x - 1, z - 1);*/
		}
		
		/*currChunkX = x;
		currChunkZ = z;
		currChunk = chunk;*/
		return chunk;

	}
	
	private void populate(Chunk chunk, int x, int z) {
		if(!chunk.terrainPopulated) {
			//System.out.println("Generating: x: " + x + " z: " + z);
			chunk.terrainPopulated = true;
			generator.populate(x, z);
		}
	}

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

