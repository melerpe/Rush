package net.rush.world;

import net.rush.chunk.Chunk;

/**
 * A {@link WorldGenerator} is used to populate new chunks which have just been
 * created.

 */
public interface WorldGenerator {

	/**
	 * Generates a new chunk.
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 */
	public Chunk generate(World world, int x, int z);
	
	/**
	 * Populates terrain (create trees, caves, flowers, ores, spaceships whatever).
	 * @param x The X coordinate.
	 * @param z The Z coordinate.
	 */
	public default void populate(int chunkX, int chunkZ) {
	}
}

