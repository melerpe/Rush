package imported;

import java.util.Random;

import net.rush.world.AlphaWorldGenerator;
import net.rush.world.World;

public class MapGenBase {

	protected int a = 8;
	protected Random rand = new Random();

	public MapGenBase() {
	}

	public void initDecoration(AlphaWorldGenerator generator, World world, int chunkX, int chunkZ, byte[] blockArray) {
		int k = a;

		rand.setSeed(world.seed);
		long xNoise = rand.nextLong() / 2L * 2L + 1L;
		long zNoise = rand.nextLong() / 2L * 2L + 1L;

		for (int xPos = chunkX - k; xPos <= chunkX + k; ++xPos)
			for (int zPos = chunkZ - k; zPos <= chunkZ + k; ++zPos) {
				rand.setSeed(xPos * xNoise + zPos * zNoise ^ world.seed);
				this.decorate(world, xPos, zPos, chunkX, chunkZ, blockArray);
			}
	}

	protected void decorate(World world, int xPos, int zPos, int chunkX, int chunkZ, byte[] blockArray) {
	}
}
