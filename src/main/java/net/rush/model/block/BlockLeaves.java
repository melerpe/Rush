package net.rush.model.block;

import net.rush.model.Block;
import net.rush.model.Material;


/**
 * @deprecated needs proper implementation
 */
public class BlockLeaves extends Block {

	public BlockLeaves(int id) {
		super(id, Material.LEAVES);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}
