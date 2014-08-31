package net.rush.model.block;

import net.rush.model.Block;
import net.rush.model.Material;

public class BlockFluids extends Block {
	
	public BlockFluids(int id) {
        super(id, id == 8 || id == 9 ? Material.WATER : Material.LAVA);
    }
}
