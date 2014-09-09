package net.rush.model.entity;

import net.rush.model.EntityAgeable;
import net.rush.protocol.utils.MetaParam;
import net.rush.world.World;

import org.bukkit.entity.EntityType;

public class Ocelot extends EntityAgeable {
	
	// TODO What are ocelot types?
	// Make a wrapper for that (or an ENUM).
	
	public Ocelot(World world) {
		super(world, EntityType.OCELOT);
	}
	
	// METADATA START
	
	public void setOcelotType(byte ocelotType) {
		setMetadata(new MetaParam<Byte>(MetaParam.TYPE_BYTE, 18, ocelotType));
	}
	
	public byte getOcelotType() {
		return ((Byte)getMetadata(18).getValue());
	}
	
	// METADATA END
}
