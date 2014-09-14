package org.bukkit.craftbukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.UnsafeValues;
import org.bukkit.inventory.ItemStack;

public final class CraftUnsafe implements UnsafeValues {
	public static final UnsafeValues INSTANCE = new CraftUnsafe();

	@Override
	public Material getMaterialFromInternalName(String name) {
		return Material.getMaterial(name);
	}

	@Override
	public List<String> tabCompleteInternalMaterialName(String token, List<String> completions) {
		return new ArrayList<>();
	}

	@Override
	public ItemStack modifyItemStack(ItemStack stack, String arguments) {
		return stack;
	}

	@Override
	public Statistic getStatisticFromInternalName(String name) {
		return Statistic.valueOf(name.toUpperCase());
	}

	@Override
	public Achievement getAchievementFromInternalName(String name) {
		return Achievement.valueOf(name.toUpperCase());
	}

	@Override
	public List<String> tabCompleteInternalStatisticOrAchievementName(String token, List<String> completions) {
		return new ArrayList<>();
	}

}
