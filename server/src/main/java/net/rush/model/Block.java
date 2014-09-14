package net.rush.model;

import java.util.Random;

import net.rush.model.block.BlockAir;
import net.rush.model.block.BlockCacti;
import net.rush.model.block.BlockClay;
import net.rush.model.block.BlockCraftingTable;
import net.rush.model.block.BlockCrops;
import net.rush.model.block.BlockDeadBush;
import net.rush.model.block.BlockFallable;
import net.rush.model.block.BlockFlower;
import net.rush.model.block.BlockFluids;
import net.rush.model.block.BlockGrass;
import net.rush.model.block.BlockLeaves;
import net.rush.model.block.BlockLog;
import net.rush.model.block.BlockMushroom;
import net.rush.model.block.BlockOre;
import net.rush.model.block.BlockSapling;
import net.rush.model.block.BlockSoil;
import net.rush.model.block.BlockStone;
import net.rush.model.block.BlockSugarCane;
import net.rush.model.block.BlockWood;
import net.rush.model.block.BlockWool;
import net.rush.model.item.ItemBlock;
import net.rush.world.World;

public class Block {
	
	public static class Sound {
		public static final StepSound STONE = new StepSound("stone", 1.0F, 1.0F);
		public static final StepSound WOOD = new StepSound("wood", 1.0F, 1.0F);
		public static final StepSound GRAVEL = new StepSound("gravel", 1.0F, 1.0F);
		public static final StepSound GRASS = new StepSound("grass", 1.0F, 1.0F);
		public static final StepSound METAL = new StepSound("stone", 1.0F, 1.5F);
		public static final StepSound GLASS = new StepSound.StepSoundStone("stone", 1.0F, 1.0F);
		public static final StepSound WOOL = new StepSound("cloth", 1.0F, 1.0F);
		public static final StepSound SAND = new StepSound("sand", 1.0F, 1.0F);
		public static final StepSound SNOW = new StepSound("snow", 1.0F, 1.0F);
		public static final StepSound LADDER = new StepSound.StepSoundLadder("ladder", 1.0F, 1.0F);
		public static final StepSound ANVIL = new StepSound.StepSoundAnvil("anvil", 0.3F, 1.0F);
	}

	public static final Block[] byId = new Block[4096];

	/** An array of 4096 booleans corresponding to the result of the isOpaqueCube() method for each block ID */
	public static final boolean[] opaqueCubeLookup = new boolean[4096];

	/** How much light is subtracted for going through this block */
	public static final int[] lightOpacity = new int[4096];

	/** Array of booleans that tells if a block can grass */
	public static final boolean[] canBlockGrass = new boolean[4096];

	/** Amount of light emitted */
	public static final int[] lightValue = new int[4096];

	/** Flag if block ID should use the brightest neighbor light value as its own */
	public static boolean[] useNeighborBrightness = new boolean[4096];

	public static final Block AIR = new BlockAir(0);	
	public static final Block STONE = new BlockStone(1).setHardness(1.5F).setResistance(10).setStepSound(Sound.STONE).setName("stone").aliases("rock");
	public static final BlockGrass GRASS = (BlockGrass) new BlockGrass(2).setHardness(0.6F).setStepSound(Sound.GRASS).setName("grass").setTickRandomly(true);
	public static final Block DIRT = new Block(3, Material.DIRT).setHardness(0.5F).setStepSound(Sound.GRAVEL).setName("dirt");
	public static final Block COBBLESTONE = (new Block(4, Material.STONE)).setHardness(2.0F).setResistance(10F).setStepSound(Sound.STONE).setName("stonebrick").aliases("smooth_brick");
	public static final Block WOOD = new BlockWood(5).setHardness(2F).setResistance(5F).setStepSound(Sound.WOOD).setName("wood").aliases("planks");
	public static final Block SAPLING = new BlockSapling(6).setHardness(0.0F).setStepSound(Sound.GRASS).setName("sapling");
	public static final Block BEDROCK = new Block(7, Material.STONE).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(Sound.STONE).setName("bedrock");
	public static final Block WATER = new BlockFluids(8).setHardness(100.0F).setLightOpacity(3).setName("water").aliases("water_flow");
	public static final Block/*Stationary*/ STATIONARY_WATER = new BlockFluids(9).setHardness(100.0F).setLightOpacity(3).setName("water").aliases("water_still");
	public static final Block LAVA = new BlockFluids(10).setHardness(0.0F).setLightValue(1.0F).setName("lava").aliases("lava_flow");
	public static final Block/*Stationary*/ STATIONARY_LAVA = new BlockFluids(11).setHardness(100.0F).setLightValue(1.0F).setName("lava").aliases("lava_still");
	public static final Block SAND = new BlockFallable(12, Material.SAND).setHardness(0.5F).setStepSound(Sound.SAND).setName("sand").aliases("sand");
	public static final Block GRAVEL = new BlockFallable(13, Material.SAND).setHardness(0.6F).setStepSound(Sound.GRAVEL).setName("gravel").aliases("gravel");
	public static final Block GOLD_ORE = new BlockOre(14).setHardness(3.0F).setResistance(5.0F).setName("oreGold").aliases("gold_ore");
	public static final Block IRON_ORE = new BlockOre(15).setHardness(3.0F).setResistance(5.0F).setName("oreIron").aliases("iron_ore");
	public static final Block COAL_ORE = new BlockOre(16).setHardness(3.0F).setResistance(5.0F).setName("oreCoal").aliases("coal_ore");	
	public static final Block LOG = new BlockLog(17).setHardness(2.0F).setStepSound(Sound.WOOD).setName("log");
	// TODO proper leaves
	public static final BlockLeaves LEAVES = (BlockLeaves) (new BlockLeaves(18)).setHardness(0.2F).setLightOpacity(1).setStepSound(Sound.GRASS).setName("leaves").aliases("leaves");
	/*public static final Block SPONGE = (new BlockSponge(19)).setHardness(0.6F).setStepSound(soundGrassFootstep).setName("sponge").aliases("sponge");
	public static final Block GLASS = (new BlockGlass(20, Material.glass, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).setName("glass").aliases("glass");
	*/public static final Block LAPIS_ORE = new BlockOre(21).setHardness(3.0F).setResistance(5.0F).setName("oreLapis").aliases("lapis_ore");
	/*public static final Block LAPIS_BLOCK = (new Block(22, Material.rock)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundStoneFootstep).setName("blockLapis")
			.aliases("lapis_block");
	public static final Block DISPENSER = (new BlockDispenser(23)).setHardness(3.5F).setStepSound(soundStoneFootstep).setName("dispenser").aliases("dispenser");
	public static final Block SANDSTONE = (new BlockSandStone(24)).setStepSound(soundStoneFootstep).setHardness(0.8F).setName("sandStone").aliases("sandstone");
	public static final Block NOTE_BLOCK = (new BlockNote(25)).setHardness(0.8F).setName("musicBlock").aliases("noteblock");
	public static final Block BED = (new BlockBed(26)).setHardness(0.2F).setName("bed").aliases("bed");
	public static final Block POWERED_RAILS = (new BlockRailPowered(27)).setHardness(0.7F).setStepSound(soundMetalFootstep).setName("goldenRail").aliases("rail_golden");
	public static final Block DETECTOR_RAILS = (new BlockDetectorRail(28)).setHardness(0.7F).setStepSound(soundMetalFootstep).setName("detectorRail").aliases("rail_detector");
	public static final BlockPistonBase PISTON_STICKY_BASE = (BlockPistonBase) (new BlockPistonBase(29, true)).setName("pistonStickyBase");
	public static final Block WEB = (new BlockWeb(30)).setLightOpacity(1).setHardness(4.0F).setName("web").aliases("web");
	*/public static final Block TALL_GRASS = new Block/*TallGrass*/(31, Material.PLANT).setHardness(0.0F).setStepSound(Sound.GRASS).setName("tallgrass"); // TODO was BlockTallGrass
	public static final Block DEAD_BUSH = new BlockDeadBush(32).setHardness(0.0F).setStepSound(Sound.GRASS).setName("deadbush"); // TODO was BlockDeadBush
	/*public static final BlockPistonBase PISTON_BASE = (BlockPistonBase) (new BlockPistonBase(33, false)).setName("pistonBase");
	public static final BlockPistonExtension PISTON_EXTENSION = new BlockPistonExtension(34);
	*/public static final Block WOOL = new BlockWool(35).setHardness(0.8F).setStepSound(Sound.WOOL).setName("cloth").aliases("wool_colored");
	/*public static final BlockPistonMoving MOVING_PISTON = new BlockPistonMoving(36);
	*/public static final Block YELLOW_FLOWER = new BlockFlower(37).setHardness(0.0F).setStepSound(Sound.GRASS).setName("flower").aliases("flower_dandelion");
	public static final Block RED_ROSE = new BlockFlower(38).setHardness(0.0F).setStepSound(Sound.GRASS).setName("rose").aliases("flower_rose");
	public static final BlockMushroom BROWN_MUSHROOM = (BlockMushroom) new BlockMushroom(39).setHardness(0.0F).setStepSound(Sound.GRASS).setLightValue(0.125F).setName("mushroom").aliases("mushroom_brown");
	public static final BlockMushroom RED_MUSHROOM = (BlockMushroom) new BlockMushroom(40).setHardness(0.0F).setStepSound(Sound.GRASS).setName("mushroom").aliases("mushroom_red");
	/*public static final Block GOLD_BLOCK = (new BlockOreStorage(41)).setHardness(3.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setName("blockGold").aliases("gold_block");
	public static final Block IRON_BLOCK = (new BlockOreStorage(42)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setName("blockIron").aliases("iron_block");
	public static final BlockHalfSlab DOUBLE_STEP = (BlockHalfSlab) (new BlockStep(43, true)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setName("stoneSlab");
	public static final BlockHalfSlab STEP = (BlockHalfSlab) (new BlockStep(44, false)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setName("stoneSlab");
	public static final Block BRICK = (new Block(45, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setName("brick");
	public static final Block TNT = (new BlockTNT(46)).setHardness(0.0F).setStepSound(soundGrassFootstep).setName("tnt").aliases("tnt");
	public static final Block BOOKSHELF = (new BlockBookshelf(47)).setHardness(1.5F).setStepSound(soundWoodFootstep).setName("bookshelf").aliases("bookshelf");
	*/public static final Block MOSSY_STONE = new Block(48, Material.STONE).setHardness(2.0F).setResistance(10.0F).setStepSound(Sound.STONE).setName("stoneMoss");
	/*public static final Block OBSIDIAN = (new BlockObsidian(49)).setHardness(50.0F).setResistance(2000.0F).setStepSound(soundStoneFootstep).setName("obsidian").aliases("obsidian");
	public static final Block TORCH = (new BlockTorch(50)).setHardness(0.0F).setLightValue(0.9375F).setStepSound(soundWoodFootstep).setName("torch").aliases("torch_on");
	public static final BlockFire FIRE = (BlockFire) (new BlockFire(51)).setHardness(0.0F).setLightValue(1.0F).setStepSound(soundWoodFootstep).setName("fire").aliases("fire");
	public static final Block MONSTER_SPAWNER = (new BlockMobSpawner(52)).setHardness(5.0F).setStepSound(soundMetalFootstep).setName("mobSpawner").aliases("mob_spawner");
	public static final Block WOODEN_STAIRS = (new BlockStairs(53, planks, 0)).setName("stairsWood");
	public static final BlockChest CHEST = (BlockChest) (new BlockChest(54, 0)).setHardness(2.5F).setStepSound(soundWoodFootstep).setName("chest");
	public static final BlockRedstoneWire REDSTONE_WIRE = (BlockRedstoneWire) (new BlockRedstoneWire(55)).setHardness(0.0F).setStepSound(soundPowderFootstep).setName("redstoneDust").aliases("redstone_dust");
	*/public static final Block DIAMOND_ORE = new BlockOre(56).setHardness(3.0F).setResistance(5.0F).setName("oreDiamond").aliases("diamond_ore");
	/*public static final Block DIAMOND_BLOCK = (new BlockOreStorage(57)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setName("blockDiamond").aliases("diamond_block");
	*/public static final Block WORKBENCH = (new BlockCraftingTable(58)).setHardness(2.5F).setStepSound(Sound.WOOD).setName("workbench").aliases("crafting_table", "crafting");
	public static final Block CROPS = new BlockCrops(59).setStepSound(Sound.GRASS).setName("crops").aliases("wheat");
	public static final Block SOIL = new BlockSoil(60).setHardness(0.6F).setStepSound(Sound.GRAVEL).setName("farmland").aliases("soil");
	/*public static final Block FURNACE = (new BlockFurnace(61, false)).setHardness(3.5F).setStepSound(soundStoneFootstep).setName("furnace");
	public static final Block BURNING_FURNACE = (new BlockFurnace(62, true)).setHardness(3.5F).setStepSound(soundStoneFootstep).setLightValue(0.875F).setName("furnace");
	public static final Block SIGN_POST = (new BlockSign(63, TileEntitySign.class, true)).setHardness(1.0F).setStepSound(soundWoodFootstep).setName("sign").disableStats();
	public static final Block WOOD_DOOR = (new BlockDoor(64, Material.wood)).setHardness(3.0F).setStepSound(soundWoodFootstep).setName("doorWood").aliases("door_wood");
	public static final Block LADDER = (new BlockLadder(65)).setHardness(0.4F).setStepSound(soundLadderFootstep).setName("ladder").aliases("ladder");
	public static final Block RAIL = (new BlockRail(66)).setHardness(0.7F).setStepSound(soundMetalFootstep).setName("rail").aliases("rail_normal");
	public static final Block COBBLESTONE_STAIRS = (new BlockStairs(67, cobblestone, 0)).setName("stairsStone");
	public static final Block WALL_SIGN = (new BlockSign(68, TileEntitySign.class, false)).setHardness(1.0F).setStepSound(soundWoodFootstep).setName("sign").disableStats();
	public static final Block LEVER = (new BlockLever(69)).setHardness(0.5F).setStepSound(soundWoodFootstep).setName("lever").aliases("lever");
	public static final Block STONE_PLATE = (new BlockPressurePlate(70, "stone", Material.rock, EnumMobType.mobs)).setHardness(0.5F).setStepSound(soundStoneFootstep).setName("pressurePlate");
	public static final Block IRON_DOOR = (new BlockDoor(71, Material.iron)).setHardness(5.0F).setStepSound(soundMetalFootstep).setName("doorIron").aliases("door_iron");
	public static final Block WOOD_PLATE = (new BlockPressurePlate(72, "planks_oak", Material.wood, EnumMobType.everything)).setHardness(0.5F).setStepSound(soundWoodFootstep).setName("pressurePlate");
	*/public static final Block REDSTONE_ORE = new /*Redstone*/BlockOre(73/*, false*/).setHardness(3.0F).setResistance(5.0F).setName("oreRedstone").aliases("redstone_ore");
	public static final Block GLOWING_REDSTONE_ORE = new /*Redstone*/BlockOre(74/*, true*/).setLightValue(0.625F).setHardness(3.0F).setResistance(5.0F).setName("oreRedstone").aliases("redstone_ore");
	/*public static final Block REDSTONE_TORCH_OFF = (new BlockRedstoneTorch(75, false)).setHardness(0.0F).setStepSound(soundWoodFootstep).setName("notGate").aliases("redstone_torch_off");
	public static final Block REDSTONE_TORCH_ON = (new BlockRedstoneTorch(76, true)).setHardness(0.0F).setLightValue(0.5F).setStepSound(soundWoodFootstep).setName("notGate")
			.aliases("redstone_torch_on");
	public static final Block STONE_BUTTON = (new BlockButtonStone(77)).setHardness(0.5F).setStepSound(soundStoneFootstep).setName("button");
	*/public static final Block SNOW = new Block(78, Material.SNOW_LAYER)/*new BlockSnow(78)*/.setHardness(0.1F).setStepSound(Sound.SNOW).setName("snow").setLightOpacity(0);
	/*public static final Block ICE = (new BlockIce(79)).setHardness(0.5F).setLightOpacity(3).setStepSound(soundGlassFootstep).setName("ice").aliases("ice");
	public static final Block SNOW_BLOCK = (new BlockSnowBlock(80)).setHardness(0.2F).setStepSound(soundSnowFootstep).setName("snow").aliases("snow");
	*/public static final Block CACTUS = new BlockCacti(81).setHardness(0.4F).setStepSound(Sound.WOOL).setName("cactus").aliases("cactus");
	public static final Block CLAY_BLOCK = new BlockClay(82).setHardness(0.6F).setStepSound(Sound.GRAVEL).setName("clay").aliases("clay");
	public static final Block SUGAR_CANE_BLOCK = new BlockSugarCane(83).setHardness(0.0F).setStepSound(Sound.GRASS).setName("reeds").aliases("reeds");
	/*public static final Block JUKEBOX = (new BlockJukeBox(84)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setName("jukebox").aliases("jukebox");
	public static final Block FENCE = (new BlockFence(85, "planks_oak", Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setName("fence");
	public static final Block PUMPKIN = (new BlockPumpkin(86, false)).setHardness(1.0F).setStepSound(soundWoodFootstep).setName("pumpkin").aliases("pumpkin");
	public static final Block NETHERRACK = (new BlockNetherrack(87)).setHardness(0.4F).setStepSound(soundStoneFootstep).setName("hellrock").aliases("netherrack");
	public static final Block SOUL_SAND = (new BlockSoulSand(88)).setHardness(0.5F).setStepSound(soundSandFootstep).setName("hellsand").aliases("soul_sand");
	public static final Block GLOWSTONE = (new BlockGlowStone(89, Material.glass)).setHardness(0.3F).setStepSound(soundGlassFootstep).setLightValue(1.0F).setName("lightgem").aliases("glowstone");
	public static final BlockPortal PORTAL = (BlockPortal) (new BlockPortal(90)).setHardness(-1.0F).setStepSound(soundGlassFootstep).setLightValue(0.75F).setName("portal").aliases("portal");
	public static final Block JACK_O_LANTERN = (new BlockPumpkin(91, true)).setHardness(1.0F).setStepSound(soundWoodFootstep).setLightValue(1.0F).setName("litpumpkin").aliases("pumpkin");
	public static final Block CAKEB_BLOCK = (new BlockCake(92)).setHardness(0.5F).setStepSound(soundClothFootstep).setName("cake").aliases("cake");
	public static final BlockRedstoneRepeater DIODE_BLOCK_OFF = (BlockRedstoneRepeater) (new BlockRedstoneRepeater(93, false)).setHardness(0.0F).setStepSound(soundWoodFootstep).setName("diode").disableStats()
			.aliases("repeater_off");
	public static final BlockRedstoneRepeater DIODE_BLOCK_ON = (BlockRedstoneRepeater) (new BlockRedstoneRepeater(94, true)).setHardness(0.0F).setLightValue(0.625F).setStepSound(soundWoodFootstep).setName("diode").disableStats()
			.aliases("repeater_on");
	public static final Block LOCKED_CHEST = (new BlockLockedChest(95)).setHardness(0.0F).setLightValue(1.0F).setStepSound(soundWoodFootstep).setName("lockedchest").setTickRandomly(true);
	public static final Block TRAP_DOOR = (new BlockTrapDoor(96, Material.wood)).setHardness(3.0F).setStepSound(soundWoodFootstep).setName("trapdoor").aliases("trapdoor");
	public static final Block MONSTER_EGG_BLOCK = (new BlockSilverfish(97)).setHardness(0.75F).setName("monsterStoneEgg");
	public static final Block SMOOTH_BRICK = (new BlockStoneBrick(98)).setHardness(1.5F).setResistance(10.0F).setStepSound(soundStoneFootstep).setName("stonebricksmooth").aliases("stonebrick");
	public static final Block BROWN_MUSHROOM_BLOCK = (new BlockMushroomCap(99, Material.wood, 0)).setHardness(0.2F).setStepSound(soundWoodFootstep).setName("mushroom").aliases("mushroom_block");
	public static final Block RED_MUSHROOM_BLOCK = (new BlockMushroomCap(100, Material.wood, 1)).setHardness(0.2F).setStepSound(soundWoodFootstep).setName("mushroom").aliases("mushroom_block");
	public static final Block IRON_BARS = (new BlockPane(101, "iron_bars", "iron_bars", Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setName("fenceIron");
	public static final Block THIN_GLASS = (new BlockPane(102, "glass", "glass_pane_top", Material.glass, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).setName("thinGlass");
	public static final Block MELON = (new BlockMelon(103)).setHardness(1.0F).setStepSound(soundWoodFootstep).setName("melon").aliases("melon");
	public static final Block PUMPKIN_STEM = (new BlockStem(104, pumpkin)).setHardness(0.0F).setStepSound(soundWoodFootstep).setName("pumpkinStem").aliases("pumpkin_stem");
	public static final Block MELON_STEM = (new BlockStem(105, melon)).setHardness(0.0F).setStepSound(soundWoodFootstep).setName("pumpkinStem").aliases("melon_stem");
	*/public static final Block VINE = new Block(106, Material.REPLACEABLE_PLANT)/*(new BlockVine(106))*/.setHardness(0.2F).setStepSound(Sound.GRASS).setName("vine");
	/*public static final Block FENCE_GATE = (new BlockFenceGate(107)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setName("fenceGate");
	public static final Block BRICK_STAIRS = (new BlockStairs(108, brick, 0)).setName("stairsBrick");
	public static final Block SMOOTH_STONE_STAIRS = (new BlockStairs(109, stoneBrick, 0)).setName("stairsStoneBrickSmooth");
	*/public static final Block MYCEL = new BlockGrass(110).setHardness(0.6F).setStepSound(Sound.GRASS).setName("mycel").aliases("mycelium");
	/*public static final Block WATER_LILY = (new BlockLilyPad(111)).setHardness(0.0F).setStepSound(soundGrassFootstep).setName("waterlily").aliases("waterlily");
	public static final Block NETHER_BRICK = (new Block(112, Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setName("netherBrick")
			.aliases("nether_brick");
	public static final Block NETHER_FENCE = (new BlockFence(113, "nether_brick", Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setName("netherFence");
	public static final Block NETHER_STAIRS = (new BlockStairs(114, netherBrick, 0)).setName("stairsNetherBrick");
	public static final Block NETHER_STALK = (new BlockNetherStalk(115)).setName("netherStalk").aliases("nether_wart");
	public static final Block ENCHANTMENT_TABLE = (new BlockEnchantmentTable(116)).setHardness(5.0F).setResistance(2000.0F).setName("enchantmentTable").aliases("enchanting_table");
	public static final Block BREWING_STAND = (new BlockBrewingStand(117)).setHardness(0.5F).setLightValue(0.125F).setName("brewingStand").aliases("brewing_stand");
	public static final BlockCauldron CAULDRON = (BlockCauldron) (new BlockCauldron(118)).setHardness(2.0F).setName("cauldron").aliases("cauldron");
	public static final Block ENDER_PORTAL = (new BlockEndPortal(119, Material.portal)).setHardness(-1.0F).setResistance(6000000.0F);
	public static final Block ENDER_PORTAL_FRAME = (new BlockEndPortalFrame(120)).setStepSound(soundGlassFootstep).setLightValue(0.125F).setHardness(-1.0F).setName("endPortalFrame").setResistance(6000000.0F)
			;
	public static final Block END_STONE = (new Block(121, Material.rock)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundStoneFootstep).setName("whiteStone")
			.aliases("end_stone");
	public static final Block DRAGON_EGG = (new BlockDragonEgg(122)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundStoneFootstep).setLightValue(0.125F).setName("dragonEgg").aliases("dragon_egg");
	public static final Block REDSTONE_LAMP_OFF = (new BlockRedstoneLight(123, false)).setHardness(0.3F).setStepSound(soundGlassFootstep).setName("redstoneLight")
			.aliases("redstone_lamp_off");
	public static final Block REDSTONE_LAMP_ON = (new BlockRedstoneLight(124, true)).setHardness(0.3F).setStepSound(soundGlassFootstep).setName("redstoneLight").aliases("redstone_lamp_on");
	public static final BlockHalfSlab DOUBLE_WOOD_STEP = (BlockHalfSlab) (new BlockWoodSlab(125, true)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setName("woodSlab");
	public static final BlockHalfSlab WOOD_STEP = (BlockHalfSlab) (new BlockWoodSlab(126, false)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundWoodFootstep).setName("woodSlab");
	public static final Block COCOA = (new BlockCocoa(127)).setHardness(0.2F).setResistance(5.0F).setStepSound(soundWoodFootstep).setName("cocoa").aliases("cocoa");
	public static final Block SANDSTONE_STAIRS = (new BlockStairs(128, sandStone, 0)).setName("stairsSandStone");
	*/public static final Block EMERALD_ORE = new BlockOre(129).setHardness(3.0F).setResistance(5.0F).setName("oreEmerald").aliases("emerald_ore");
	/*public static final Block ENDER_CHEST = (new BlockEnderChest(130)).setHardness(22.5F).setResistance(1000.0F).setStepSound(soundStoneFootstep).setName("enderChest").setLightValue(0.5F);
	public static final BlockTripWireSource TRIP_WIRE_SOURCE = (BlockTripWireSource) (new BlockTripWireSource(131)).setName("tripWireSource").aliases("trip_wire_source");
	public static final Block TRIP_WIRE = (new BlockTripWire(132)).setName("tripWire").aliases("trip_wire");
	public static final Block EMERALD_BLOCK = (new BlockOreStorage(133)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setName("blockEmerald").aliases("emerald_block");
	public static final Block SPRUCE_STAIRS = (new BlockStairs(134, planks, 1)).setName("stairsWoodSpruce");
	public static final Block BIRCH_STAIRS = (new BlockStairs(135, planks, 2)).setName("stairsWoodBirch");
	public static final Block JUNGLE_STAIRS = (new BlockStairs(136, planks, 3)).setName("stairsWoodJungle");
	public static final Block COMMAND = (new BlockCommandBlock(137)).setBlockUnbreakable().setResistance(6000000.0F).setName("commandBlock").aliases("command_block");
	public static final BlockBeacon BEACON = (BlockBeacon) (new BlockBeacon(138)).setName("beacon").setLightValue(1.0F).aliases("beacon");
	public static final Block COBBLE_WALL = (new BlockWall(139, cobblestone)).setName("cobbleWall");
	public static final Block FLOWER_PORT = (new BlockFlowerPot(140)).setHardness(0.0F).setStepSound(soundPowderFootstep).setName("flowerPot").aliases("flower_pot");
	public static final Block CARROT = (new BlockCarrot(141)).setName("carrots").aliases("carrots");
	public static final Block POTATO = (new BlockPotato(142)).setName("potatoes").aliases("potatoes");
	public static final Block WOOD_BUTTON = (new BlockButtonWood(143)).setHardness(0.5F).setStepSound(soundWoodFootstep).setName("button");
	public static final Block SKULL = (new BlockSkull(144)).setHardness(1.0F).setStepSound(soundStoneFootstep).setName("skull").aliases("skull");
	public static final Block ANVIL = (new BlockAnvil(145)).setHardness(5.0F).setStepSound(soundAnvilFootstep).setResistance(2000.0F).setName("anvil");
	public static final Block TRAPPED_CHEST = (new BlockChest(146, 1)).setHardness(2.5F).setStepSound(soundWoodFootstep).setName("chestTrap");
	public static final Block GOLD_PLATE = (new BlockPressurePlateWeighted(147, "gold_block", Material.iron, 64)).setHardness(0.5F).setStepSound(soundWoodFootstep).setName("weightedPlate_light");
	public static final Block IRON_PLATE = (new BlockPressurePlateWeighted(148, "iron_block", Material.iron, 640)).setHardness(0.5F).setStepSound(soundWoodFootstep).setName("weightedPlate_heavy");
	public static final BlockComparator REDSTONE_COMPARATOR_OFF = (BlockComparator) (new BlockComparator(149, false)).setHardness(0.0F).setStepSound(soundWoodFootstep).setName("comparator").disableStats()
			.aliases("comparator_off");
	public static final BlockComparator REDSTONE_COMPARATOR_ON = (BlockComparator) (new BlockComparator(150, true)).setHardness(0.0F).setLightValue(0.625F).setStepSound(soundWoodFootstep).setName("comparator").disableStats()
			.aliases("comparator_on");
	public static final BlockDaylightDetector DAYLIGHT_DETECTOR = (BlockDaylightDetector) (new BlockDaylightDetector(151)).setHardness(0.2F).setStepSound(soundWoodFootstep).setName("daylightDetector")
			.aliases("daylight_detector");
	public static final Block REDSTONE_BLOCK = (new BlockPoweredOre(152)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundMetalFootstep).setName("blockRedstone").aliases("redstone_block");
	*/public static final Block QUARTZ_ORE = new BlockOre(153).setHardness(3.0F).setResistance(5.0F).setName("netherquartz").aliases("quartz_ore");
	/*public static final BlockHopper HOPPER = (BlockHopper) (new BlockHopper(154)).setHardness(3.0F).setResistance(8.0F).setStepSound(soundWoodFootstep).setName("hopper").aliases("hopper");
	public static final Block QUARTZ_BLOCK = (new BlockQuartz(155)).setStepSound(soundStoneFootstep).setHardness(0.8F).setName("quartzBlock").aliases("quartz_block");
	public static final Block QUARTZ_STAIRS = (new BlockStairs(156, blockNetherQuartz, 0)).setName("stairsQuartz");
	public static final Block ACTIVATOR_RAIL = (new BlockRailPowered(157)).setHardness(0.7F).setStepSound(soundMetalFootstep).setName("activatorRail").aliases("rail_activator");
	public static final Block DROPPED = (new BlockDropper(158)).setHardness(3.5F).setStepSound(soundStoneFootstep).setName("dropper").aliases("dropper");
	public static final Block STAINED_CLAY = (new BlockColored(159, Material.rock)).setHardness(1.25F).setResistance(7.0F).setStepSound(soundStoneFootstep).setName("clayHardenedStained").aliases("hardened_clay_stained");
	public static final Block HAY_BLOCK = (new BlockHay(170)).setHardness(0.5F).setStepSound(soundGrassFootstep).setName("hayBlock");
	public static final Block CARPET = (new BlockCarpet(171)).setHardness(0.1F).setStepSound(soundClothFootstep).setName("woolCarpet").setLightOpacity(0);
	public static final Block HARD_CLAY = (new Block(172, Material.rock)).setHardness(1.25F).setResistance(7.0F).setStepSound(soundStoneFootstep).setName("clayHardened")
			.aliases("hardened_clay");
	public static final Block COAL_BLOCK = (new Block(173, Material.rock)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundStoneFootstep).setName("blockCoal")
			.aliases("coal_block");*/

	public final int id;

	/** Indicates how many hits it takes to break a block. */
	protected float blockHardness = 0F;
	protected float blockResistance;	
	protected boolean tickRandomly = false;
	protected boolean hasTileEntity;	
	protected Random rand = new Random();
	
	public StepSound sound = Sound.STONE;
	public float blockParticleGravity = 1.0F;
	public final Material material;

	/**
	 * Determines how much velocity is maintained while moving on top of this block
	 */
	public float slipperiness = 0.6F;
	private String name;
	private String[] aliases;

	protected Block(int id, Material material) {
		if (byId[id] != null) 
			throw new IllegalArgumentException("Block ID " + id + " is already set by " + byId[id] + " when adding " + this);

		this.material = material;
		this.id = id;
		
		byId[id] = this;
		opaqueCubeLookup[id] = isOpaqueCube();
		lightOpacity[id] = isOpaqueCube() ? 255 : 0;
		canBlockGrass[id] = !material.getCanBlockGrass();

	}


	/**
	 * Sets the footstep sound for the block. Returns the object for convenience in constructing.
	 */
	protected Block setStepSound(StepSound stepSound) {
		this.sound = stepSound;
		return this;
	}

	/**
	 * Sets how much light is blocked going through this block. Returns the object for convenience in constructing.
	 */
	protected Block setLightOpacity(int light_opacity) {
		lightOpacity[id] = light_opacity;
		return this;
	}

	/**
	 * Sets the amount of light emitted by a block from 0.0f to 1.0f (converts internally to 0-15). Returns the object
	 * for convenience in constructing.
	 */
	protected Block setLightValue(float light_Value) {
		lightValue[id] = (int) (15.0F * light_Value);
		return this;
	}

	/**
	 * Sets the the blocks resistance to explosions. Returns the object for convenience in constructing.
	 */
	protected Block setResistance(float blockResistance) {
		this.blockResistance = blockResistance * 3.0F;
		return this;
	}

	public static boolean isNormalCube(int blockId) {
		Block id = byId[blockId];
		return id == null ? false : id.material.isOpaque() && id.renderAsNormalBlock();
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock() {
		return true;
	}

	public boolean getBlocksMovement(/*IBlockAccess par1IBlockAccess,*/ int x, int y, int z) {
		return !material.blocksMovement();
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType() {
		return 0;
	}

	/**
	 * Sets how many hits it takes to break a block.
	 */
	protected Block setHardness(float blockHardness) {
		this.blockHardness = blockHardness;

		if (blockResistance < blockHardness * 5.0F) {
			blockResistance = blockHardness * 5.0F;
		}

		return this;
	}

	protected Block setBlockUnbreakable() {
		setHardness(-1.0F);
		return this;
	}

	/**
	 * Returns the block hardness at a location.
	 */
	public float getBlockHardness() {
		return blockHardness;
	}

	/**
	 * Sets whether this block type will receive random update ticks
	 */
	protected Block setTickRandomly(boolean tickrandomly) {
		tickRandomly = tickrandomly;
		return this;
	}

	/**
	 * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
	 * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
	 */
	public boolean getTickRandomly() {
		return tickRandomly;
	}

	public boolean hasTileEntity() {
		return hasTileEntity;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	public boolean isOpaqueCube() {
		return true;
	}

	/**
	 * Returns whether this block is collideable based on the arguments passed in
	 */
	public boolean canCollideCheck() {
		return isCollidable();
	}

	/**
	 * Returns if this block is collidable (only used by Fire).
	 */
	public boolean isCollidable() {
		return true;
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	public void tick(World world, int x, int y, int z, Random rand) {}

	/**
	 * Called right before the block is destroyed by a player.
	 */
	public void onBlockDestroyed(World world, Player player, int x, int y, int z, int metaData) {
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own)
	 */
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockId) {
	}

	public int getDropCount() {
		return 1;
	}

	public int getDropId() {
		return id;
	}

	/**
	 * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
	 * Player.
	 */
	public float getPlayerRelativeBlockHardness(Player player, World world, int x, int y, int z) {
		float hardness = getBlockHardness();
		return hardness;//hardness < 0.0F ? 0.0F : (!player.canHarvestBlock(this) ? player.getCurrentPlayerStrVsBlock(this, false) / hardness / 100.0F : player.getCurrentPlayerStrVsBlock(this, true) / hardness / 30.0F);
				// TODO
	}

	/**
	 * Drops the specified block items
	 */
	public void dropBlock(World world, int x, int y, int z, int damage, int bonus) {
		dropBlockWithChance(world, x, y, z, damage, 1.0F, bonus);
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified items
	 */
	public void dropBlockWithChance(World world, int x, int y, int z, int damage, float chance, int bonus) {
		int quantity = quantityDroppedWithBonus(bonus);

		for (int i = 0; i < quantity; ++i) {
			if (world.rand.nextFloat() <= chance) {
				int id = getDropId();

				if (id > 0) {
					world.dropItem(x, y, z, id, 1, damageDropped(damage));
				}
			}
		}
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and wood.
	 */
	public int damageDropped(int dmg) {
		return 0;
	}

	/**
	 * Returns how much this block can resist explosions from the passed in entity.
	 */
	public float getExplosionResistance(Entity en) {
		return blockResistance / 5.0F;
	}

	/**
	 * Called upon the block being destroyed by an explosion
	 */
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
	}

	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, ItemStack is) {
		return canPlaceBlockOnTop(world, x, y, z);
	}

	/**
	 * Checks to see if you can place this block can be placed on top of the block
	 */
	public boolean canPlaceBlockOnTop(World world, int x, int y, int z) {
		return canPlaceBlockAt(world, x, y + 1, z);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates.
	 */
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int id = world.getType(x, y, z);
		return id == 0 || byId[id].material.isReplaceable();
	}

	/**
	 * Called upon right clicking on the block.
	 */
	public boolean onBlockInteract(World world, int x, int y, int z, Player player, int direction, float xOffset, float yOffset, float zOffset) {
		return false;
	}

	/**
	 * Called whenever an entity is walking on top of this block.
	 */
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
	}

	/**
	 * Called when a block is placed using its ItemBlock.
	 */
	public int onBlockPlaced(World world, int x, int y, int z, int direction, float xOffset, float yOffset, float zOffset, int metadata) {
		return 0;
	}

	/**
	 * Used on growing trees, mushrooms, crops etc
	 * @returns true if the grow was successfully
	 */
	public boolean grow(World world, int x, int y, int z, boolean bonemeal) {
		return true;
	}

	/**
	 * Triggered whenever an entity collides with this block (enters into the block).
	 */
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity en) {
	}


	/**
	 * Called when the player destroys a block with an item that can harvest it.
	 */
	public void harvestBlock(World world, Player pl, int x, int y, int z, int blockData) {
		pl.addExhaustion(0.025F);

		// TODO
		/*if (canSilkHarvest() && EnchantmentHelper.getSilkTouchLevel(pl.getItemInHand())) {
			ItemStack bonusItem = createStackedBlock(blockData);

			if (bonusItem != null) {
				dropBlockWithChance(world, x, y, z, getDamageValue(world, x, y, z), bonusItem);
			}
		} else {
			int fortune = EnchantmentHelper.getFortuneLevel(pl.getItemInHand());
			dropBlock(world, x, y, z, blockData, fortune);
		}*/
	}

	/**
	 * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
	 */
	protected boolean canSilkHarvest() {
		return renderAsNormalBlock() && !hasTileEntity;
	}

	/**
	 * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
	 * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
	 */
	protected ItemStack createStackedBlock(int blockType) {
		int data = 0;

		// TODO
		/*if (blockID >= 0 && blockID < Item.byId.length && Item.byId[blockID].getHasSubtypes()) {
			data = blockType;
		}*/

		return new ItemStack(id, 1, data);
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
	 */
	public int quantityDroppedWithBonus(int bonus) {
		return getDropCount();
	}

	/**
	 * Called when the block is placed in the world.
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z, LivingEntity livingEntity, ItemStack itemStack) {
	}

	/**
	 * Called after a block is placed
	 */
	public void onPostBlockPlaced(World world, int x, int y, int z, int blockId) {
	}

	public Block setName(String name) {
		this.name = name;
		return this;
	}
	
	public Block aliases(String... name) {
		this.aliases = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases == null ? new String[] { name } : aliases;
	}
	

	/**
	 * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
	 * and stop pistons
	 */
	public int getMobility() {
		return material.getMaterialMobility();
	}

	/**
	 * Block's chance to react to an entity falling on it.
	 */
	public void onFallenUpon(World world, int x, int y, int z, Entity en, float chance) {
	}

	/**
	 * Get the block's damage value (for use with pick block).
	 */
	public int getDamageValue(World world, int x, int y, int z) {
		return damageDropped(world.getBlockData(x, y, z));
	}

	/**
	 * Called when the block is attempted to be harvested
	 */
	public void onBlockHarvested(World world, int x, int y, int z, int blockId, Player player) {
	}

	/**
	 * Called on server worlds only when the block is about to be replaced by a different block or the same block with a
	 * different metadata value.
	 */
	public void onBlockPreDestroy(World world, int x, int y, int z, int oldBlockData) {
	}

	/**
	 * currently only used by BlockCauldron to incrament meta-data during rain
	 */
	public void fillWithRain(World world, int x, int y, int z) {
	}

	/**
	 * Return whether this block can drop from an explosion.
	 */
	public boolean canDropFromExplosion(Explosion explosion) {
		return true;
	}

	/**
	 * Returns true if the given block ID is equivalent to this one. Example: redstoneTorchOn matches itself and
	 * redstoneTorchOff, and vice versa. Most blocks only match themselves.
	 */
	public boolean isAssociatedBlockID(int blockId) {
		return this.id == blockId;
	}

	/**
	 * Static version of isAssociatedBlockID.
	 */
	public static boolean isAssociatedWith(int firstId, int secondId) {
		return firstId == secondId ? true : (firstId != 0 && secondId != 0 && byId[firstId] != null && byId[secondId] != null ? byId[firstId].isAssociatedBlockID(secondId) : false);
	}

	static {
		/*Item.byId[WOOL.blockID] = (new ItemCloth(cloth.blockID - 256)).setName("cloth");
		Item.byId[STAINED_CLAY.blockID] = (new ItemCloth(stainedClay.blockID - 256)).setName("clayHardenedStained");
		Item.byId[CARPET.blockID] = (new ItemCloth(carpet.blockID - 256)).setName("woolCarpet");
		Item.byId[LOG.blockID] = (new ItemMultiTextureTile(wood.blockID - 256, wood, BlockLog.woodType)).setName("log");
		Item.byId[WOOD.blockID] = (new ItemMultiTextureTile(planks.blockID - 256, planks, BlockWood.woodType)).setName("wood");
		Item.byId[MONSTER_EGG_BLOCK.blockID] = (new ItemMultiTextureTile(silverfish.blockID - 256, silverfish, BlockSilverfish.silverfishStoneTypes)).setName("monsterStoneEgg");
		Item.byId[SMOOTH_BRICK.blockID] = (new ItemMultiTextureTile(stoneBrick.blockID - 256, stoneBrick, BlockStoneBrick.STONE_BRICK_TYPES)).setName("stonebricksmooth");
		Item.byId[SANDSTONE.blockID] = (new ItemMultiTextureTile(sandStone.blockID - 256, sandStone, BlockSandStone.SAND_STONE_TYPES)).setName("sandStone");
		Item.byId[QUARTZ_BLOCK.blockID] = (new ItemMultiTextureTile(blockNetherQuartz.blockID - 256, blockNetherQuartz, BlockQuartz.quartzBlockTypes)).setName("quartzBlock");
		Item.byId[STEP.blockID] = (new ItemSlab(stoneSingleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab, false)).setName("stoneSlab");
		Item.byId[DOUBLE_STEP.blockID] = (new ItemSlab(stoneDoubleSlab.blockID - 256, stoneSingleSlab, stoneDoubleSlab, true)).setName("stoneSlab");
		Item.byId[WOOD_STEP.blockID] = (new ItemSlab(woodSingleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab, false)).setName("woodSlab");
		Item.byId[DOUBLE_WOOD_STEP.blockID] = (new ItemSlab(woodDoubleSlab.blockID - 256, woodSingleSlab, woodDoubleSlab, true)).setName("woodSlab");
		Item.byId[SAPLING.blockID] = (new ItemMultiTextureTile(sapling.blockID - 256, sapling, BlockSapling.WOOD_TYPES)).setName("sapling");
		Item.byId[LEAVES.blockID] = (new ItemLeaves(leaves.blockID - 256)).setName("leaves");
		Item.byId[VINE.blockID] = new ItemColored(vine.blockID - 256, false);
		Item.byId[TALL_GRASS.blockID] = (new ItemColored(tallGrass.blockID - 256, true)).setBlockNames(new String[] { "shrub", "grass", "fern" });
		Item.byId[SNOW.blockID] = new ItemSnow(snow.blockID - 256, snow);
		Item.byId[WATER_LILY.blockID] = new ItemLilyPad(waterlily.blockID - 256);
		Item.byId[PISTON_BASE.blockID] = new ItemPiston(pistonBase.blockID - 256);
		Item.byId[PISTON_STICKY_BASE.blockID] = new ItemPiston(pistonStickyBase.blockID - 256);
		Item.byId[COBBLE_WALL.blockID] = (new ItemMultiTextureTile(cobblestoneWall.blockID - 256, cobblestoneWall, BlockWall.types)).setName("cobbleWall");
		Item.byId[ANVIL.blockID] = (new ItemAnvilBlock(anvil)).setName("anvil");
		 */
		for (int id = 0; id < 256; ++id) {
			if (byId[id] != null) {
				if (Item.byId[id] == null)
					Item.byId[id] = new ItemBlock(id - 256);

				boolean useNeightborBrightness = false;

				if (id > 0 && byId[id].getRenderType() == 10)
					useNeightborBrightness = true;
				
				//if (id > 0 && byId[id] instanceof BlockHalfSlab)
				//	useNeightborBrightness = true; TODO

				if (id == SOIL.id || canBlockGrass[id] || lightOpacity[id] == 0)
					useNeightborBrightness = true;

				useNeighborBrightness[id] = useNeightborBrightness;
			}
		}

		canBlockGrass[0] = true;
	}
	
	@Override
	public String toString() {
		return "Block {id=" + id + ", name=" + getName() + "}";
	}
}
