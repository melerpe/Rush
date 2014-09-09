package net.rush.model;

import net.rush.protocol.utils.MetaParam;
import net.rush.util.StringUtils;
import net.rush.world.World;

import org.bukkit.entity.EntityType;

public class EntityTameable extends EntityAgeable {

	protected EntityTameable(World world, EntityType type) {
		super(world, type);
	}
	
	// METADATA START
	
	// TODO is sitting, is tame
	
	public String getOwner() {
		return ((String)getMetadata(17).getValue());
	}
	
	public void setOwner(String owner) {
		setMetadata(new MetaParam<String>(MetaParam.TYPE_STRING, 17, StringUtils.colorize(owner)));
	}	
	
	// METADATA END

}
