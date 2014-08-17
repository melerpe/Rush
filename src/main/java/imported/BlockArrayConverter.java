package imported;

import net.rush.chunk.Chunk;

public class BlockArrayConverter {

	/**
	 * Converts a block array with height 128 (mcregion) to block array with height 256 (anvil)
	 */
	public static byte[] convertBlockArray(byte[] mcRegionBlocks) {

		for (int byteY = 0; byteY < 8; ++byteY) {
			boolean loopRunning = true;

			for (int xPos = 0; xPos < 16 && loopRunning; ++xPos) {
				int count = 0;

				while (count < 16 && loopRunning) {
					int zPos = 0;

					while (true) {
						if (zPos < 16) {
							int flatBlockPos = xPos << 11 | zPos << 7 | count + (byteY << 4);
							byte blockId = mcRegionBlocks[flatBlockPos];

							if (blockId == 0) {
								++zPos;
								continue;
							}

							loopRunning = false;
						}

						++count;
						break;
					}
				}
			}

			if (!loopRunning) {
				byte[] anvilBlocks = new byte[Chunk.SIZE];

				for (int x = 0; x < Chunk.WIDTH; ++x) {
					for (int y = 0; y < (Chunk.DEPTH / 2); ++y) { // 256 : 2 = 128 -> McRegion height
						for (int z = 0; z < Chunk.HEIGHT; ++z) {
							int flatBlockPos = x << 11 | z << 7 | y + (byteY << 4);
							byte blockId = mcRegionBlocks[flatBlockPos];

							anvilBlocks[y << 8 | z << 4 | x] = (byte) (blockId & 255);								
						}
					}
				}

				return anvilBlocks;
			}
		}
		
		throw new RuntimeException("Failed to convert block array! (Size: " + mcRegionBlocks.length + ")");
	}
}

