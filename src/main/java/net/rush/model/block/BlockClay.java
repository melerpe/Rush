package net.rush.model.block;

import net.rush.model.Block;
import net.rush.model.Item;
import net.rush.model.Material;

public class BlockClay extends Block {

	public BlockClay(int id) {
		super(id, Material.CLAY);
	}

	@Override
	public int idDropped() {
		return Item.CLAY_BALL.id;
	}

	@Override
	public int quantityDropped() {
		return 4;
	}
}
