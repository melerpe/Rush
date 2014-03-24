package net.rush.chunk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.rush.io.ChunkIoService;
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

	/**
	 * The world generator used to generate new chunks.
	 */
	private final WorldGenerator generator;

	/**
	 * A map of chunks currently loaded in memory.
	 */
	private final Map<ChunkCoords, Chunk> chunks = new HashMap<ChunkCoords, Chunk>();

	/**
	 * Creates a new chunk manager with the specified I/O service and world
	 * generator.
	 * @param service The I/O service.
	 * @param generator The world generator.
	 */
	public ChunkManager(ChunkIoService service, WorldGenerator generator) {
		this.service = service;
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

			if (chunk == null) {
				chunk = generator.generate(x, z);
			}

			chunks.put(key, chunk);
		}
		return chunk;
	}

	/**
	 * Saves all chunks loaded.
	 * @throws IOException if an I/O error occurs.
	 */
	public void saveAll() throws IOException {
		for (Chunk chunk: chunks.values()) {
			service.write(chunk.getX(), chunk.getZ(), chunk);
		}
	}

}
