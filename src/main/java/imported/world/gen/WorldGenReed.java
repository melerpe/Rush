package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.block.BlockSugarCane;
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
				if (((BlockSugarCane) Block.SUGAR_CANE_BLOCK).canReedStay(world, x, yPos + piece - 1, z))
					world.setType(xPos, yPos + piece, zPos, Block.SUGAR_CANE_BLOCK.id, false);		
		}

		return true;
	}
}
