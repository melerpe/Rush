package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.world.World;

public class WorldGenReed extends WorldGenerator {

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		for (int l = 0; l < 20; ++l) {
			int xPos = x + random.nextInt(4) - random.nextInt(4);
			int yPos = y;
			int zPos = z + random.nextInt(4) - random.nextInt(4);
			int height = 2 + random.nextInt(random.nextInt(3) + 1);

			for (int piece = 0; piece < height; ++piece)
				if (Block.SUGAR_CANE_BLOCK.canPlaceBlockAt(world, xPos, yPos + piece, zPos) && !isSurroundedWith(world, xPos, yPos + piece + 1, zPos, Material.WATER)
						&& !isSurroundedWith(world, xPos, yPos + piece, zPos, Material.WATER))
					world.setType(xPos, yPos + piece, zPos, Block.SUGAR_CANE_BLOCK.id, false);		
		}

		return true;
	}
	
	private boolean isSurroundedWith(World w, int x, int y, int z, Material material) {
		if (w.getMaterial(x - 1, y, z) == material || w.getMaterial(x + 1, y, z) == material || w.getMaterial(x, y, z - 1) == material || w.getMaterial(x, y, z + 1) == material)
			return true;
		return false;
	}
}
