package net.rush.model.block;

import net.rush.model.Block;
import net.rush.model.Material;

public class BlockAir extends Block {

	public BlockAir(int id) {
		super(id, Material.AIR);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}
