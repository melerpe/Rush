package net.rush.model.block;

import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.world.World;

public class BlockLeaves extends Block {

	public BlockLeaves(int id) {
		super(id, Material.LEAVES);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int idDropped() {
		return Block.SAPLING.id;
	}
	
	@Override
	public void dropBlock(World world, int x, int y, int z, int damage, int bonus) {
		dropBlockWithChance(world, x, y, z, damage, 0.08F, bonus);
	}
}
