package net.rush.model.block;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.world.World;

public class BlockCacti extends Block {

	public BlockCacti(int id) {
		super(id, Material.PLANT);
	}

	@Override
	public boolean canPlaceBlockAt(World w, int x, int y, int z) {
		return (w.getTypeId(x, y - 1, z) == Block.SAND.id || w.getTypeId(x, y - 1, z) == Block.CACTUS.id) && w.isAir(x - 1, y, z) && w.isAir(x + 1, y, z) && w.isAir(x, y, z - 1) && w.isAir(x, y, z + 1);
	}

	@Override
	public void tick(World world, int x, int y, int z, Random rand) {
		checkIfCanStay(world, x, y, z);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockId) {
		checkIfCanStay(world, x, y, z);
	}
	
	private void checkIfCanStay(World world, int x, int y, int z) {
		if (!canPlaceBlockAt(world, x, y - 1, z)) {
			dropBlock(world, x, y, z, world.getBlockData(x, y, z), 0);
			world.setTypeWithNotify(x, y, z, 0, true);
		}
	}
}
