package net.rush.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class BlockDebreakifier {

	private static final boolean[] validBlocks = new boolean[198 << 4];
	private static final int[] correctedValues = new int[198];

	static {
		Arrays.fill(correctedValues, -1);

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File("blocks.yml"));
		List<String> list = conf.getStringList("blocks");

		for (String entry : list) {
			String[] parts = entry.split(":");
			int id = Integer.parseInt(parts[0]);
			int data = Integer.parseInt(parts[1]);
			
			validBlocks[(id << 4) | data] = true;

			if (correctedValues[id] == -1 || data < correctedValues[id]) 
				correctedValues[id] = data;
		}
	}

	public static int getCorrectedData(int id, int data) {
		if (id > 197)
			return data;
		if (id == 175 && data > 8) {
			data = 8;
		}
		if (validBlocks[(id << 4) | data])
			return data;
		else 
			return correctedValues[id] & 0xF;		
	}

	private static HashMap<Integer, Integer> invalidItems = new HashMap<>();
	static {
		/*replace(Block.WATER, Item.WATER_BUCKET);
		replace(Block.STATIONARY_WATER, Item.WATER_BUCKET);
		replace(Block.LAVA, Item.LAVA_BUCKET);
		replace(Block.STATIONARY_LAVA, Item.LAVA_BUCKET);
		replace(Block.PORTAL, Item.NETHER_BRICK);
		replace(Block.DOUBLE_STEP, Block.STEP);
		replace(Block.FIRE, Item.FLINT_AND_STEEL);
		replace(Block.ENDER_PORTAL, Block.ENDER_PORTAL_FRAME);
		replace(Block.WOOD_DOUBLE_STEP, Block.WOOD_STEP);
		replace(Block.COCOA, Item.SEEDS);
		replace(Block.CARROTS, Item.CARROT);
		replace(Block.POTATOES, Item.POTATO);*/
	}

	public static int getItemId(int id) {
		return invalidItems.containsKey(id) ? invalidItems.get(id) : id;
	}

	/*private static void replace(Block block, Block other) {
		replace(block.id, other.id);
	}

	private static void replace(Block block, Item other) {
		replace(block.id, other.id);
	}

	private static void replace(int block, int other) {
		invalidItems.put(block, other);
	}*/
}
