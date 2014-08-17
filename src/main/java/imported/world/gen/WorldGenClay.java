package imported.world.gen;

import java.util.Random;

import net.rush.model.Block;
import net.rush.model.Material;
import net.rush.util.MathHelper;
import net.rush.world.World;

public class WorldGenClay extends WorldGenerator {

	private int clayId;
	private int b;

	public WorldGenClay(int i) {
		clayId = Block.CLAY_BLOCK.id;
		b = i;
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		if (world.getMaterial(x, y, z) != Material.WATER)
			return false;

		float f = random.nextFloat() * 3.1415927F;
		double d0 = x + 8 + MathHelper.floor_float(f) * b / 8.0F;
		double d1 = x + 8 - MathHelper.floor_float(f) * b / 8.0F;
		double d2 = z + 8 + MathHelper.ceiling_float_int(f) * b / 8.0F;
		double d3 = z + 8 - MathHelper.ceiling_float_int(f) * b / 8.0F;
		double d4 = y + random.nextInt(3) + 2;
		double d5 = y + random.nextInt(3) + 2;

		for (int l = 0; l <= b; ++l) {
			double d6 = d0 + (d1 - d0) * l / b;
			double d7 = d4 + (d5 - d4) * l / b;
			double d8 = d2 + (d3 - d2) * l / b;
			double d9 = random.nextDouble() * b / 16.0D;
			double d10 = (MathHelper.floor_double(l * 3.1415927F / b) + 1.0F) * d9 + 1.0D;
			double d11 = (MathHelper.floor_double(l * 3.1415927F / b) + 1.0F) * d9 + 1.0D;

			for (int xPos = (int) (d6 - d10 / 2.0D); xPos <= (int) (d6 + d10 / 2.0D); ++xPos)
				for (int yPos = (int) (d7 - d11 / 2.0D); yPos <= (int) (d7 + d11 / 2.0D); ++yPos)
					for (int zPos = (int) (d8 - d10 / 2.0D); zPos <= (int) (d8 + d10 / 2.0D); ++zPos) {
						double d12 = (xPos + 0.5D - d6) / (d10 / 2.0D);
						double d13 = (yPos + 0.5D - d7) / (d11 / 2.0D);
						double d14 = (zPos + 0.5D - d8) / (d10 / 2.0D);

						if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D) {
							int blockId = world.getTypeId(xPos, yPos, zPos);

							if (blockId == Block.SAND.id)
								world.setTypeId(xPos, yPos, zPos, clayId, false);
						}
					}
		}

		return true;
	}
}
