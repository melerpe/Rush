package net.rush.model.entity;

import java.util.HashMap;
import java.util.Map;

import net.rush.model.Entity;

import org.bukkit.entity.EntityType;

public class EntityRegistry {
	
	private static final Map<EntityType, Class<? extends Entity>> entities = new HashMap<>();
	
	static {
		registerEntity(EntityType.PIG, Pig.class);
		registerEntity(EntityType.BLAZE, Blaze.class);
		registerEntity(EntityType.ENDERMAN, Enderman.class);
		registerEntity(EntityType.OCELOT, Ocelot.class);
		registerEntity(EntityType.SPIDER, Spider.class);
		registerEntity(EntityType.VILLAGER, Villager.class);
		registerEntity(EntityType.WOLF, Wolf.class);
		registerEntity(EntityType.ZOMBIE, Zombie.class);
		registerEntity(EntityType.FALLING_BLOCK, EntityFallingBlock.class);
	}
	
	public static Class<? extends Entity> entityLookup(EntityType type) {
        Class<? extends Entity> clazz = entities.get(type);

        if (clazz == null)
            throw new NullPointerException("Unknown entity " + type.toString() + "! Is is implemented?");
        return clazz;
    }

    public static void registerEntity(EntityType type, Class<? extends Entity> clazz) {
        entities.put(type, clazz);
    }
}
