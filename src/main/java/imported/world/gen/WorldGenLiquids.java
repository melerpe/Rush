package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.world.World;

public class WorldGenLiquids extends WorldGenerator {

	private int liquidId;

	public WorldGenLiquids(int liquidId) {
		this.liquidId = liquidId;
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		if (world.getType(x, y + 1, z) != Block.STONE.id)
			return false;
		else if (world.getType(x, y - 1, z) != Block.STONE.id)
			return false;
		else if (world.getType(x, y, z) != 0 && world.getType(x, y, z) != Block.STONE.id)
			return false;

		int stoneProb = 0;

		if (world.getType(x - 1, y, z) == Block.STONE.id)
			++stoneProb;

		if (world.getType(x + 1, y, z) == Block.STONE.id)
			++stoneProb;

		if (world.getType(x, y, z - 1) == Block.STONE.id)
			++stoneProb;

		if (world.getType(x, y, z + 1) == Block.STONE.id)
			++stoneProb;

		int nearProb = 0;

		if (world.getType(x - 1, y, z) == 0)
			++nearProb;

		if (world.getType(x + 1, y, z) == 0)
			++nearProb;

		if (world.getType(x, y, z - 1) == 0)
			++nearProb;

		if (world.getType(x, y, z + 1) == 0)
			++nearProb;

		if (stoneProb == 3 && nearProb == 1)
			world.setType(x, y, z, liquidId, false);

		return true;
	}
}
