package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.block.BlockFlower;
import net.rush.world.World;

public class WorldGenFlowers extends WorldGenerator {

	private int flowerId;

	public WorldGenFlowers(int flowerId) {
		this.flowerId = flowerId;
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		for (int tries = 0; tries < 64; ++tries) {
			int xPos = x + random.nextInt(8) - random.nextInt(8);
			int yPos = y + random.nextInt(4) - random.nextInt(4);
			int zPos = z + random.nextInt(8) - random.nextInt(8);
			if (world.getType(xPos, yPos, zPos) == 0 && ((BlockFlower) Block.byId[flowerId]).canPlaceBlockAt(world, xPos, yPos - 1, zPos))
				world.setType(xPos, yPos, zPos, flowerId, false);
		}

		return true;
	}
}
