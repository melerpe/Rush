package net.rush.model;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Material;

@Getter
@Setter
public class ItemStack {

	public int id;
	public int count;
	public int damage;

	/**
	 * The ItemStack's NBT byte array storing data (enchantments, etc).
	 */
	private final byte[] data;

	/**
	 * Creates a single ItemStack with no damage.
	 * @param id The ItemStack id.
	 */
	public ItemStack(int id) {
		this(id, 1);
	}

	/**
	 * Creates an ItemStack with no damage.
	 * @param id The id.
	 * @param count The number of ItemStacks within the stack.
	 */
	public ItemStack(int id, int count) {
		this(id, count, 0);
	}

	/**
	 * Creates an ItemStack with the specified count and damage. Generally ItemStacks that
	 * can be damaged cannot be stacked so the count should be one.
	 * @param id The id.
	 * @param count The number of ItemStacks within the stack.
	 * @param damage The damage.
	 */
	public ItemStack(int id, int count, int damage) {
		this(id, count, damage, null);
	}

	/**
	 * Creates an ItemStack with the specified count, damage, data length and data. Generally ItemStacks that
	 * can be damaged cannot be stacked so the count should be one.
	 * @param id The id.
	 * @param count The number of ItemStacks within the stack.
	 * @param damage The damage.
	 */
	public ItemStack(int id, int count, int damage, byte[] data) {
		this.id = id;
		this.count = count;
		this.damage = damage;
		this.data = data;
	}

	public boolean doMaterialsMatch(ItemStack is) {
		return this.id == is.id && this.damage == is.damage;
	}

	public boolean doItemsMatch(ItemStack is) {
		return this.id == is.id && this.damage == is.damage && this.count == is.count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + damage;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemStack other = (ItemStack) obj;
		if (count != other.count)
			return false;
		if (damage != other.damage)
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return Material.getMaterial(id) + "x" + count +  (damage != 0 ? "@" + damage : "");
		//return String.format("ItemStack [id=%s,count=%d,damage=%d])", id, count, damage);
	}

	@Override
	public ItemStack clone() {
		return new ItemStack(id, count, damage, data);
	}
}

