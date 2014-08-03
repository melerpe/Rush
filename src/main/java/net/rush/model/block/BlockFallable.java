package net.rush.model.block;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.model.entity.EntityFallingBlock;
import net.rush.world.World;

public class BlockFallable extends Block {

	public BlockFallable(int id, Material mat) {
		super(id, mat);
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int direction, float xOffset, float yOffset, float zOffset, int metadata) {
		world.spawnEntity(new EntityFallingBlock(world, x + 0.5D, y + 0.5D, z + 0.5D, id));
		
		return super.onBlockPlaced(world, x, y, z, direction, xOffset, yOffset, zOffset, metadata);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.getTypeId(x, y, z) != Block.AIR;
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
		if (!canPlaceBlockAt(world, x, y - 1, z))
			world.spawnEntity(new EntityFallingBlock(world, x + 0.5D, y + 0.5D, z + 0.5D, id));
	}
}
