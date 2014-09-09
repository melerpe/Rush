package net.rush.model;

import java.util.HashSet;

import net.rush.chunk.ChunkCoords;
import net.rush.model.entity.ai.EntityAI;
import net.rush.protocol.Packet;
import net.rush.protocol.packets.AnimationPacket.AnimType;
import net.rush.protocol.packets.SpawnMobPacket;
import net.rush.protocol.utils.MetaParam;
import net.rush.world.World;

import org.bukkit.entity.EntityType;

/**
 * Represents a living entity.
 */
public class LivingEntity extends Mob {

	// TODO Horse, Bat, Sheep

	protected int health;
	protected int maxHealth;
	protected HashSet<EntityAI> aiTasks = new HashSet<EntityAI>();

	/**
	 * Creates a new living entity (e.g. zombie or a pig).
	 * @param world The world this living entity is in.
	 * @param type The type of living entity.
	 */
	protected LivingEntity(World world, EntityType type) {
		super(world, type);
		// Set health to prevent sending null metadata that crash client.
		setMetadata(new MetaParam<Float>(MetaParam.TYPE_FLOAT, 6, 20F), false);
		this.health = (int)20;
	}

	@Override
	public Packet createSpawnMessage() {
		if(position == null)
			throw new NullPointerException("Entity position is null!");

		int x = position.getPixelX();
		int y = position.getPixelY();
		int z = position.getPixelZ();
		int yaw = rotation.getIntYaw();
		int pitch = rotation.getIntPitch();	// FIXME byte headYaw?
		return new SpawnMobPacket(id, (byte)getType().getTypeId(), new Position(x, y, z), (byte)yaw, (byte)pitch, (byte)yaw, Position.ZERO, metadata.clone());
	}

	@Override
	public void pulse() {
		if(getWorld().activeChunks.contains(new ChunkCoords(getChunk().getX(), getChunk().getZ())))
			for(EntityAI task : aiTasks)
				threadAi.addToQueue(task);
	}

	/** On right click on the entity */
	public void onPlayerInteract(Player pl) {}

	/** On left click on the entity */
	public void onPlayerHit(Player pl) {
		pl.playAnimationOf(this.id, AnimType.DAMAGE_ANIMATION);
		pl.playSound(getHurtSound(), position);
	}

	public String getHurtSound() {
		return "mob." + getType().name().toLowerCase() + ".hurt";
	}

	public String getDeathSound() {
		return "mob." + getType().name().toLowerCase() + ".death";
	}

	// METADATA START

	public float getHealth() {
		return (Float) getMetadata(6).getValue();
	}

	public void setHealth(float health) {
		setMetadata(new MetaParam<Float>(MetaParam.TYPE_FLOAT, 6, health));
		this.health = (int)health;
	}

	public int getPotionEffectColor() {
		return (Integer) getMetadata(7).getValue();
	}

	public void setPotionEffectColor(int effectColor) {
		setMetadata(new MetaParam<Integer>(MetaParam.TYPE_INT, 7, effectColor));
	}

	public byte getIsPotionEffectAmbient() {
		return (Byte) getMetadata(8).getValue();
	}

	public void setIsPotionEffectAmbient(boolean ambient) {
		setMetadata(new MetaParam<Byte>(MetaParam.TYPE_BYTE, 8, ambient ? (byte)1 : (byte)0));
	}

	public byte getArrowsInEntity() {
		return (Byte) getMetadata(9).getValue();
	}

	public void setArrowsInEntity(int howManyArrows) {
		setMetadata(new MetaParam<Byte>(MetaParam.TYPE_BYTE, 9, (byte)howManyArrows));
	}

	public String getName() {
		return (String) getMetadata(10).getValue();
	}

	public void setName(String name) {
		setMetadata(new MetaParam<String>(MetaParam.TYPE_STRING, 10, name.replace("&", "§")));
	}

	public boolean getNameVisible() {
		return (Byte) getMetadata(11).getValue() == 1;
	}

	public void setNameVisible(boolean alwaysVisible) {
		setMetadata(new MetaParam<Byte>(MetaParam.TYPE_BYTE, 11, (byte)(alwaysVisible ? 1 : 0)));
	}

	// METADATA END
}

