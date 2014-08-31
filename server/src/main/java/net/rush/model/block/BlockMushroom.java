package net.rush.model.block;

import java.util.Random;

import net.rush.model.Block;
import net.rush.world.World;

public class BlockMushroom extends BlockFlower {

	public BlockMushroom(int id) {
		super(id);
		float f = 0.2F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		setTickRandomly(true);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		if (y < 0 || y >= world.getMaxHeight())
			return false;
		else {
			int supportingBlock = world.getType(x, y - 1, z);
			return supportingBlock == Block.MYCEL.id/* || world.getBlockLight(x, y, z) < 13*/;
		}
	}

	/*public boolean fertilizeMushroom(World world, int i, int j, int k, Random random) {
		int l = world.getBlockData(i, j, k);
		world.setTypeId(i, j, k, 0, true);
		WorldGenBigMushroom bigMushroom = null;
		if (id == Block.BROWN_MUSHROOM.id)
			bigMushroom = new WorldGenBigMushroom(0);
		else if (id == Block.RED_MUSHROOM.id)
			bigMushroom = new WorldGenBigMushroom(1);
		if (bigMushroom == null || !bigMushroom.generate(world, random, i, j, k)) {
			world.setTypeAndData(i, j, k, id, l, true);
			return false;
		} else
			return true;
	}*/

	@Override
	public void tick(World world, int x, int y, int z, Random rand) {
		if (rand.nextInt(25) != 0) 
			return;
		
		byte radius = 4;
		int maxMushrooms = 5;

		for (int i = x - radius; i <= x + radius; i++)
			for (int j = z - radius; j <= z + radius; j++)
				for (int k = y - 1; k <= y + 1; k++)
					if (world.getType(i, k, j) == id && --maxMushrooms <= 0)
						return;

		int xPos = x + rand.nextInt(3) - 1;
		int yPos = y + rand.nextInt(2) - rand.nextInt(2);
		int zPos = z + rand.nextInt(3) - 1;
		for (int i = 0; i < 4; i++) {
			if (world.isAir(xPos, yPos, zPos) && canPlaceBlockAt(world, xPos, yPos, zPos)) {
				x = xPos;
				y = yPos;
				z = zPos;
			}
			xPos = x + rand.nextInt(3) - 1;
			yPos = y + rand.nextInt(2) - rand.nextInt(2);
			zPos = z + rand.nextInt(3) - 1;
		}

		if (world.isAir(xPos, yPos, zPos) && canPlaceBlockAt(world, xPos, yPos, zPos))
			world.setTypeWithNotify(xPos, yPos, zPos, id, true);
	}
}
