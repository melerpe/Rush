package net.rush.model.block;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.Item;
import net.rush.model.Material;
import net.rush.world.World;

public class BlockSugarCane extends Block {

	public BlockSugarCane(int id) {
		super(id, Material.PLANT);
		setTickRandomly(true);
	}

	@Override
	public boolean canPlaceBlockAt(World w, int x, int y, int z) {
		int rootBlock = w.getTypeId(x, y - 1, z);
		if(rootBlock == Block.SUGAR_CANE_BLOCK.id)
			return true;		
		if(rootBlock == Block.GRASS.id || rootBlock == Block.DIRT.id || rootBlock == Block.SAND.id)
			return w.getMaterial(x - 1, y - 1, z) == Material.WATER ? true : w.getMaterial(x + 1, y - 1, z) == Material.WATER ? true : w.getMaterial(x, y - 1, z - 1) == Material.WATER ? true : w.getMaterial(x, y - 1, z + 1) == Material.WATER;
		return false;
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
			world.setAir(x, y, z);
		}
	}

	@Override
	public int idDropped() {
		return Item.SUGAR_CANE.id;
	}
}
