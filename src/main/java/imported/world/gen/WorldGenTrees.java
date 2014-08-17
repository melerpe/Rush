package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.world.World;

public class WorldGenTrees extends WorldGenerator {

	public WorldGenTrees() {
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		int height = random.nextInt(3) + 4;
		boolean canGrow = true;

		if (y >= 1 && y + height + 1 <= 128) {
			int trunkY;
			int trunkX;
			int trunkZ;
			int idOrPosX;

			for (trunkY = y; trunkY <= (y + 1 + height); ++trunkY) {
				byte length = 1;

				if (trunkY == y)
					length = 0;

				if (trunkY >= y + 1 + height - 2)
					length = 2;

				for (trunkX = x - length; trunkX <= x + length && canGrow; ++trunkX) {
					for (trunkZ = z - length; trunkZ <= z + length && canGrow; ++trunkZ) {
						if (trunkY >= 0 && trunkY < 128) {
							idOrPosX = world.getTypeId(trunkX, trunkY, trunkZ);

							//System.out.println("Block At x: " + trunkX + " y: " + trunkY + " z: " + trunkZ + " type " + Block.byId[idOrPosX].getName());
							//world.setTypeId(trunkX, trunkY + 1, trunkZ, Block.DIAMOND_ORE.id, true);

							if (idOrPosX != 0 && idOrPosX != Block.LEAVES.id)
								canGrow = false;
						} else {
							canGrow = false;
						}
					}
				}
			}

			if (!canGrow)
				return false;

			trunkY = world.getTypeId(x, y - 1, z);
			if ((trunkY == Block.GRASS.id || trunkY == Block.DIRT.id) && y < 128 - height - 1) {
				world.setTypeId(x, y - 1, z, Block.DIRT.id, false);

				int yPos2;

				for (yPos2 = y - 3 + height; yPos2 <= y + height; ++yPos2) {
					trunkX = yPos2 - (y + height);
					trunkZ = 1 - trunkX / 2;

					for (idOrPosX = x - trunkZ; idOrPosX <= x + trunkZ; ++idOrPosX) {
						int j2 = idOrPosX - x;

						for (int zPos2 = z - trunkZ; zPos2 <= z + trunkZ; ++zPos2) {
							int l2 = zPos2 - z;

							if ((Math.abs(j2) != trunkZ || Math.abs(l2) != trunkZ || random.nextInt(2) != 0 && trunkX != 0) && (world.getTypeId(idOrPosX, yPos2, zPos2) == Block.AIR.id
									|| world.getTypeId(idOrPosX, yPos2, zPos2) == Block.LEAVES.id) /*!Block.opaqueCubeLookup[world.getTypeId(idOrPosX, yPos2, zPos2)]*/)
								world.setTypeId(idOrPosX, yPos2, zPos2, Block.LEAVES.id, false);							
						}
					}
				}

				for (yPos2 = 0; yPos2 < height; ++yPos2) {
					trunkX = world.getTypeId(x, y + yPos2, z);
					if (trunkX == 0 || trunkX == Block.LEAVES.id) 
						world.setTypeId(x, y + yPos2, z, Block.LOG.id, false);
					
				}

				return true;
			} else
				return false;

		} else
			return false;
	}
}
