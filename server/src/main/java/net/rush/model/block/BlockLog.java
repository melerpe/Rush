package net.rush.model.block;

import net.rush.model.Material;

public class BlockLog extends RotatableBlock {

	public static final String[] logTypes = new String[] { "oak", "spruce", "birch", "jungle" };

	public BlockLog(int id) {
		super(id, Material.WOOD);
	}

	public static int limitToValidMetadata(int blockRotation) {
		return blockRotation & 3;
	}
}
