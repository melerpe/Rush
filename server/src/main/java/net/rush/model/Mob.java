package net.rush.model;

import net.rush.protocol.Packet;
import net.rush.protocol.packets.EntityLookAndRelMovePacket;
import net.rush.protocol.packets.EntityLookPacket;
import net.rush.protocol.packets.EntityRelMovePacket;
import net.rush.protocol.packets.EntityTeleportPacket;
import net.rush.task.WorldThreadAI;
import net.rush.world.World;

import org.bukkit.entity.EntityType;

/**
 * A Mob is a {@link Player} or {@link LivingEntity}.
.
 */
public abstract class Mob extends Entity {

	protected final WorldThreadAI threadAi;
	
	/**
	 * Creates a mob within the specified world.
	 * @param world The world.
	 */
	public Mob(World world, EntityType type) {
		super(world, type);
		
		threadAi = new WorldThreadAI();
		world.spawnEntity(this);
	}
	
	@Override
	public Packet createUpdateMessage() {		
		if(position == null)
			throw new NullPointerException("Entity position is null!");
		
		boolean moved = !position.equals(previousPosition);
		boolean rotated = !rotation.equals(previousRotation);
		
		int x = position.getPixelX();
		int y = position.getPixelY();
		int z = position.getPixelZ();

		int dx = x - previousPosition.getPixelX();
		int dy = y - previousPosition.getPixelY();
		int dz = z - previousPosition.getPixelZ();

		boolean teleport = dx > Byte.MAX_VALUE || dy > Byte.MAX_VALUE || dz > Byte.MAX_VALUE || dx < Byte.MIN_VALUE || dy < Byte.MIN_VALUE || dz < Byte.MIN_VALUE;

		int yaw = rotation.getIntYaw();
		int pitch = rotation.getIntPitch();

		if (moved && teleport) {
			return new EntityTeleportPacket(id, x, y, z, yaw, pitch);
		} else if (moved && rotated) {
			return new EntityLookAndRelMovePacket(id, (byte)dx, (byte)dy, (byte)dz, (byte)yaw, (byte)pitch);
		} else if (moved) {
			return new EntityRelMovePacket(id, (byte)dx, (byte)dy, (byte)dz);
		} else if (rotated) {
			return new EntityLookPacket(id, (byte)yaw, (byte)pitch);
		}

		return null;
	}
}

