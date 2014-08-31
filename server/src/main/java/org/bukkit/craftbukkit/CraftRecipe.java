package org.bukkit.craftbukkit;

import org.bukkit.inventory.Recipe;

public interface CraftRecipe extends Recipe {
    void addToCraftingManager();
}
