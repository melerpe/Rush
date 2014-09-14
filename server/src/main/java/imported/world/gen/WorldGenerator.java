package imported.world.gen;

import java.util.Random;

import net.rush.world.World;

public abstract class WorldGenerator {

	protected Random rand = new Random();
	
	protected WorldGenerator() {
	}

	public abstract boolean generate(World world, Random rand, int x, int y, int z);

	public void setTreeGeneratorScale(double d0, double d1, double d2) {
	}
}
