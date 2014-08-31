package org.bukkit.craftbukkit.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment extends Enchantment {

	private final int id, maxLevel, startLevel;
	private final EnchantmentTarget target;
	
    public CraftEnchantment(int id, int maxLevel, int startLevel, EnchantmentTarget target) {
        super(id);
        
        this.id = id;
        this.maxLevel = maxLevel;
        this.startLevel = startLevel;
        this.target = target;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getStartLevel() {
        return startLevel;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return target;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true; // TODO mcenchant.canEnchant(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public String getName() {
        switch (id) {
        case 0:
            return "PROTECTION_ENVIRONMENTAL";
        case 1:
            return "PROTECTION_FIRE";
        case 2:
            return "PROTECTION_FALL";
        case 3:
            return "PROTECTION_EXPLOSIONS";
        case 4:
            return "PROTECTION_PROJECTILE";
        case 5:
            return "OXYGEN";
        case 6:
            return "WATER_WORKER";
        case 7:
            return "THORNS";
        case 16:
            return "DAMAGE_ALL";
        case 17:
            return "DAMAGE_UNDEAD";
        case 18:
            return "DAMAGE_ARTHROPODS";
        case 19:
            return "KNOCKBACK";
        case 20:
            return "FIRE_ASPECT";
        case 21:
            return "LOOT_BONUS_MOBS";
        case 32:
            return "DIG_SPEED";
        case 33:
            return "SILK_TOUCH";
        case 34:
            return "DURABILITY";
        case 35:
            return "LOOT_BONUS_BLOCKS";
        case 48:
            return "ARROW_DAMAGE";
        case 49:
            return "ARROW_KNOCKBACK";
        case 50:
            return "ARROW_FIRE";
        case 51:
            return "ARROW_INFINITE";
        case 61:
            return "LUCK";
        case 62:
            return "LURE";
        default:
            return "UNKNOWN_ENCHANT_" + id;
        }
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        if (other instanceof EnchantmentWrapper)
            other = ((EnchantmentWrapper) other).getEnchantment();
        
        if (!(other instanceof CraftEnchantment)) 
            return false;
        
        CraftEnchantment ench = (CraftEnchantment) other;
        return false; // TODO !mcenchant.ConflictsWith(ench.target);
    }
}
