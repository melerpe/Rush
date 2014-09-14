package net.rush.model.block;

import net.rush.model.Block;
import net.rush.model.Item;
import net.rush.model.Material;

public class BlockOre extends Block {

	public BlockOre(int id) {
		super(id, Material.STONE);
		
		setStepSound(Sound.STONE);
	}

	@Override
	public int getDropId() {
		return id == Block.COAL_ORE.id ? Item.COAL.id : id == Block.DIAMOND_ORE.id ? Item.DIAMOND.id : id == Block.LAPIS_ORE.id ? Item.INK_SACK.id 
				: id == Block.EMERALD_ORE.id ? Item.EMERALD.id: id == Block.QUARTZ_ORE.id ? Item.QUARTZ.id : id;
	}

	@Override
	public int getDropCount() {
		return id == Block.LAPIS_ORE.id ? 4 + rand.nextInt(5) : 1;
	}
}
