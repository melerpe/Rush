package net.rush.model;

import net.rush.protocol.utils.MetaParam;
import net.rush.world.World;

import org.bukkit.entity.EntityType;

public class EntityAgeable extends LivingEntity {

	protected EntityAgeable(World world, EntityType type) {
		super(world, type);
	}

	// METADATA START
	
	public boolean isBaby() {
		return (Integer) getMetadata(12).getValue() < 0;
	}
	
	public int getAge() {
		return (Integer) getMetadata(12).getValue();
	}

	public void setAge(int age) {
		setMetadata(new MetaParam<Integer>(MetaParam.TYPE_INT, 12, age));
	}

	// METADATA END
}
