package net.rush.model.entity;

import net.rush.model.EntityAgeable;
import net.rush.protocol.utils.MetaParam;
import net.rush.world.World;

import org.bukkit.entity.EntityType;

public class Blaze extends EntityAgeable {
	
	public Blaze(World world) {
		super(world, EntityType.BLAZE);
	}
	
	// METADATA START
	
	public void setOnFire(boolean onFire) {
		setMetadata(new MetaParam<Byte>(MetaParam.TYPE_BYTE, 16, (byte) (onFire ? 1 : 0)));
	}
	
	public boolean isOnFire() {
		return ((Byte)getMetadata(16).getValue()) == 1;
	}
	
	// METADATA END
}
