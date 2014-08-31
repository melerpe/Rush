package net.rush.model.item;

import net.rush.model.Item;

public class ItemCoal extends Item {
	
	public ItemCoal(int id) {
		super(id);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	/*@Override
	public String getName() {
		return is.getDamage() == 1 ? "item.charcoal" : "item.coal";
	}*/
}
