package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.world.World;

public class WorldGenDungeons extends WorldGenerator {

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		byte b0 = 3;
		int l = random.nextInt(2) + 2;
		int i1 = random.nextInt(2) + 2;
		int j1 = 0;

		int k1;
		int yPos;
		int xPos;

		for (k1 = x - l - 1; k1 <= x + l + 1; ++k1)
			for (yPos = y - 1; yPos <= y + b0 + 1; ++yPos)
				for (xPos = z - i1 - 1; xPos <= z + i1 + 1; ++xPos) {
					Material material = world.getMaterial(k1, yPos, xPos);

					if (yPos == y - 1 && !material.isSolid())
						return false;

					if (yPos == y + b0 + 1 && !material.isSolid())
						return false;

					if ((k1 == x - l - 1 || k1 == x + l + 1 || xPos == z - i1 - 1 || xPos == z + i1 + 1) && yPos == y && world.getType(k1, yPos, xPos) == 0 && world.getType(k1, yPos + 1, xPos) == 0)
						++j1;
				}

		if (j1 >= 1 && j1 <= 5) {
			for (k1 = x - l - 1; k1 <= x + l + 1; ++k1)
				for (yPos = y + b0; yPos >= y - 1; --yPos)
					for (xPos = z - i1 - 1; xPos <= z + i1 + 1; ++xPos)
						if (k1 != x - l - 1 && yPos != y - 1 && xPos != z - i1 - 1 && k1 != x + l + 1 && yPos != y + b0 + 1 && xPos != z + i1 + 1)
							world.setType(k1, yPos, xPos, 0, false);
						else if (yPos >= 0 && !world.getMaterial(k1, yPos - 1, xPos).isSolid())
							world.setType(k1, yPos, xPos, 0, false);
						else if (world.getMaterial(k1, yPos, xPos).isSolid())
							if (yPos == y - 1 && random.nextInt(4) != 0)
								world.setType(k1, yPos, xPos, Block.MOSSY_STONE.id, false);
							else
								world.setType(k1, yPos, xPos, Block.COBBLESTONE.id, false);

			k1 = 0;

			while (k1 < 2) {
				yPos = 0;

				while (true) {
					if (yPos < 3)
						spawnerRoom: {
							xPos = x + random.nextInt(l * 2 + 1) - l;
							int zPos = z + random.nextInt(i1 * 2 + 1) - i1;

							if (world.getType(xPos, y, zPos) == 0) {
								int chestLocation = 0;

								if (world.getMaterial(xPos - 1, y, zPos).isSolid())
									++chestLocation;

								if (world.getMaterial(xPos + 1, y, zPos).isSolid())
									++chestLocation;

								if (world.getMaterial(xPos, y, zPos - 1).isSolid())
									++chestLocation;

								if (world.getMaterial(xPos, y, zPos + 1).isSolid())
									++chestLocation;

								/*if (chestLocation == 1) {
									world.setType(xPos, y, zPos, Block.CHEST.id);
									TileEntityChest chest = (TileEntityChest) world.k(xPos, y, zPos);

									for (int count = 0; count < 8; ++count) {
										ItemStack itemstack = this.getRandomItem(random);

										if (itemstack != null)
											chest.a(random.nextInt(chest.a()), itemstack);
									}
									break spawnerRoom;
								}*/
							}

							++yPos;
							continue;
						}

					++k1;
					break;
				}
			}

			/*world.setType(x, z, y, Block.MOB_SPAWNER.id);
			TileEntityMobSpawner spawner = (TileEntityMobSpawner) world.addTileEntitySpawner(x, y, z);

			spawner.creature = getRandomCreature(random);*/
			return true;
		} else
			return false;
	}

	/*private ItemStack getRandomItem(Random random) {
		int r = random.nextInt(11);
		return r == 0 ? new ItemStack(Item.SADDLE) : r == 1 ? new ItemStack(Item.IRON_INGOT, random.nextInt(4) + 1) : r == 2 ? new ItemStack(Item.BREAD) : r == 3 ? new ItemStack(Item.WHEAT, random.nextInt(4) + 1) : r == 4 ? new ItemStack(Item.SULPHUR, random.nextInt(4) + 1) : r == 5 ? new ItemStack(Item.STRING, random.nextInt(4) + 1) : r == 6 ? new ItemStack(Item.BUCKET) : r == 7 && random.nextInt(100) == 0 ? new ItemStack(Item.GOLDEN_APPLE) : r == 8 && random.nextInt(2) == 0 ? new ItemStack(Item.REDSTONE, random.nextInt(4) + 1) : r == 9 && random.nextInt(10) == 0 ? new ItemStack(Item.byId[Item.GOLD_RECORD.id + random.nextInt(2)]) : null;
	}

	private String getRandomCreature(Random random) {
		int r = random.nextInt(4);
		return r == 0 ? "Skeleton" : r == 1 ? "Zombie" : r == 2 ? "Zombie" : r == 3 ? "Spider" : "";
	}*/
}
