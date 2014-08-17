package imported.world.gen;

import java.util.Random;

import net.rush.world.World;

public abstract class WorldGenerator {

	protected WorldGenerator() {
	}

	public abstract boolean generate(World world, Random random, int x, int y, int z);

	public void a(double d0, double d1, double d2) {
	}
}
