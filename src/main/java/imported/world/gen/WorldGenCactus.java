package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.world.World;

public class WorldGenCactus extends WorldGenerator {

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		for (int tries = 0; tries < 10; ++tries) {
			int xPos = x + random.nextInt(8) - random.nextInt(8);
			int yPos = y + random.nextInt(4) - random.nextInt(4);
			int zPos = z + random.nextInt(8) - random.nextInt(8);

			if (world.getTypeId(xPos, yPos, zPos) == 0) {
				int pieceCount = 1 + random.nextInt(random.nextInt(3) + 1);
				for (int piece = 0; piece < pieceCount; ++piece)
					if (Block.CACTUS.canPlaceBlockAt(world, xPos, yPos + piece, zPos))
						world.setTypeId(xPos, yPos + piece, zPos, Block.CACTUS.id, true);
			}
		}

		return true;
	}
}
