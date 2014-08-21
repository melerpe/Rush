package net.rush.model.item;

import net.rush.model.Block;
import net.rush.model.Item;
import net.rush.model.ItemStack;
import net.rush.model.Player;
import net.rush.world.World;

public class ItemPlaceable extends Item {

	private int blockId;

	public ItemPlaceable(int id, int blocktype) {
		super(id);
		blockId = blocktype;
	}

	@Override
	public boolean onItemUse(ItemStack item, Player player, World world, int x, int y, int z, int direction, float xOffset, float yOffset, float zOffset) {
		if (direction != 1)
			return false;
		
		Block block = Block.byId[blockId];
		
		if (block.canPlaceBlockAt(world, x, y + 1, z)) {
			world.setType(x, y + 1, z, blockId, true);
			//--item.count;
		
			
			world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, block.sound.getPlaceSound(), block.sound.getVolume(), block.sound.getPitch());
			return true;
		}
		return false;

	}
}
