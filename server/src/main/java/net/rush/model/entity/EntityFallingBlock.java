// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst

package net.rush.model.entity;

import net.rush.model.Entity;
import net.rush.protocol.Packet;
import net.rush.protocol.packets.PacketEntityLookRelMove;
import net.rush.protocol.packets.PacketEntityLook;
import net.rush.protocol.packets.PacketEntityRelMove;
import net.rush.protocol.packets.PacketEntityTeleport;
import net.rush.protocol.packets.PacketSpawnObject;
import net.rush.protocol.packets.PacketSpawnObject.ObjectType;
import net.rush.util.MathHelper;
import net.rush.world.World;

import org.bukkit.entity.EntityType;

public class EntityFallingBlock extends Entity {

	public int blockId;
	public int fallTime = 0;

	public EntityFallingBlock(World world) {
		super(world, EntityType.FALLING_BLOCK);
	}

	public EntityFallingBlock(World world, double x, double y, double z, int blockId) {
		super(world, EntityType.FALLING_BLOCK);
		this.blockId = blockId;
		setPosition(x, y, z);
	}

	private boolean onGround(int x, int y, int z) {
		return world.getType(MathHelper.floor_double(x + motionX), MathHelper.floor_double(y + motionY), MathHelper.floor_double(z + motionZ)) != 0;
	}

	@Override
	public void pulse() {
		super.pulse();

		if (blockId == 0) {
			destroy();
			return;
		}
		fallTime++;
		motionY -= 0.039999999105930328D;
		//moveEntity(motionX, motionY, motionZ);
		motionX *= 0.98000001907348633D;
		motionY *= 0.98000001907348633D;
		motionZ *= 0.98000001907348633D;
		
		int x = MathHelper.floor_double(getPosition().x);
		int y = MathHelper.floor_double(getPosition().y);
		int z = MathHelper.floor_double(getPosition().z);
		
		// This removes the block in place of the falling block.
		if (fallTime == 1 && world.getType(x, y, z) == blockId)
			world.setAir(x, y, z);
		
		if (onGround(x, y, z)) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
			motionY *= -0.5D;
			
			// TODO Drop when fall on torches, etc.
			world.setType(x, y, z, blockId, true);
			destroy();
		} else if (fallTime > 100) {
			world.dropItem(getPosition().x, getPosition().y, getPosition().z, blockId, 1, 0);
			destroy();
		}
	}

	@Override
	public Packet createSpawnMessage() {
		return new PacketSpawnObject(this, ObjectType.FALLING_BLOCK, blockId, motionX, motionY, motionZ);
	}

	@Override
	public Packet createUpdateMessage() {
		if(position == null)
			throw new NullPointerException("Entity position is null!");

		setX(getPosition().x + motionX);
		setY(getPosition().y + motionY);
		setZ(getPosition().z + motionZ);

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
			return new PacketEntityTeleport(id, x, y, z, yaw, pitch);
		} else if (moved && rotated) {
			return new PacketEntityLookRelMove(id, (byte)dx, (byte)dy, (byte)dz, (byte)yaw, (byte)pitch);
		} else if (moved) {
			return new PacketEntityRelMove(id, (byte)dx, (byte)dy, (byte)dz);
		} else if (rotated) {
			return new PacketEntityLook(id, (byte)yaw, (byte)pitch);
		}

		return null;
	}
}
