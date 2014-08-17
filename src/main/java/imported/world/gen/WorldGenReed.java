package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.world.World;

public class WorldGenReed extends WorldGenerator {

	public WorldGenReed() {
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		for (int l = 0; l < 20; ++l) {
			int xPos = x + random.nextInt(4) - random.nextInt(4);
			int yPos = y;
			int zPos = z + random.nextInt(4) - random.nextInt(4);

			if (world.getTypeId(xPos, y, zPos) == 0 && (world.getMaterial(xPos - 1, y - 1, zPos) == Material.WATER || world.getMaterial(xPos + 1, y - 1, zPos) == Material.WATER || 
					world.getMaterial(xPos, y - 1, zPos - 1) == Material.WATER || world.getMaterial(xPos, y - 1, zPos + 1) == Material.WATER)) {
				int height = 2 + random.nextInt(random.nextInt(3) + 1);
				for (int piece = 0; piece < height; ++piece) {
					//if (Block.SUGAR_CANE_BLOCK.canPlaceBlockAt(world, x, y + piece, z))
					if(canPlaceAt(world, x, y + piece, z))
						world.setTypeId(xPos, yPos + piece, zPos, Block.SUGAR_CANE_BLOCK.id, false);
				}
			}
		}

		return true;
	}


	public boolean canPlaceAt(World world, int x, int y, int z) {
		int blockId = world.getTypeId(x, y - 1, z);

		return blockId != Block.GRASS.id && blockId != Block.DIRT.id 
				? false : world.getMaterial(x - 1, y - 1, z) == Material.WATER 
				? true : world.getMaterial(x + 1, y - 1, z) == Material.WATER 
				? true : world.getMaterial(x, y - 1, z - 1) == Material.WATER 
				? true : world.getMaterial(x, y - 1, z + 1) == Material.WATER;
	}
}
