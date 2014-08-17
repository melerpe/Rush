package net.rush.model.block;

import net.rush.world.World;


public class BlockSapling extends BlockFlower {

	public static final String[] saplingTypes = new String[] { "oak", "spruce", "birch", "jungle" };

	public BlockSapling(int id) {
		super(id);
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int direction, float xOffset, float yOffset, float zOffset, int metadata) {
		return metadata & 3;
	}
	
	@Override
	public int damageDropped(int damage) {
		return damage & 3;
	}
}
