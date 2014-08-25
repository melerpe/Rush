package net.rush.world;

import net.rush.chunk.Chunk;
import net.rush.chunk.ChunkCoords;
import net.rush.model.Block;

/**
 * A simple {@link WorldGenerator} used to generate a "flat grass" world.
 */
public class FlatGrassWorldGenerator implements WorldGenerator {
	
	@Override
	public Chunk generate(World world, int chunkX, int chunkZ) {
		Chunk chunk = new Chunk(new ChunkCoords(chunkX, chunkZ));
		for (int x = 0; x < Chunk.WIDTH; x++) {
			for (int z = 0; z < Chunk.HEIGHT; z++) {
				for (int y = 0; y < Chunk.DEPTH; y++) {
					int id = 0;
					
					if (y == 60)
						id = Block.GRASS.id;
					else if (y >= 55 && y < 60)
						id = Block.DIRT.id;
					else if (y == 0)
						id = Block.BEDROCK.id;
					else if (y < 55)
						id = Block.STONE.id;

					chunk.setTypeAndData(x, y, z, id, 0);
					chunk.setBlockLight(x, y, z, 0);
					chunk.setSkyLight(x, y, z, 15);
				}
			}
		}
		return chunk;
	}

}

