package imported;

import java.util.Random;

import net.rush.world.World;

public class MapGenBase {

	protected int radius = 8;
	protected Random rand = new Random();

	public void initDecoration(World world, int chunkX, int chunkZ, byte[] blockArray) {
		rand.setSeed(world.seed);
		long xNoise = rand.nextLong() / 2L * 2L + 1L;
		long zNoise = rand.nextLong() / 2L * 2L + 1L;

		for (int xPos = chunkX - radius; xPos <= chunkX + radius; ++xPos)
			for (int zPos = chunkZ - radius; zPos <= chunkZ + radius; ++zPos) {
				rand.setSeed(xPos * xNoise + zPos * zNoise ^ world.seed);
				decorate(world, xPos, zPos, chunkX, chunkZ, blockArray);
			}
	}

	protected void decorate(World world, int xPos, int zPos, int chunkX, int chunkZ, byte[] blockArray) {
	}
}
