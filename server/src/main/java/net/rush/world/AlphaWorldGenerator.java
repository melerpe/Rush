package net.rush.world;

import imported.MapGenCaves;
import imported.world.gen.NoiseGeneratorOctaves;
import imported.world.gen.WorldGenBigTree;
import imported.world.gen.WorldGenCactus;
import imported.world.gen.WorldGenClay;
import imported.world.gen.WorldGenDungeons;
import imported.world.gen.WorldGenFlowers;
import imported.world.gen.WorldGenLiquids;
import imported.world.gen.WorldGenMinable;
import imported.world.gen.WorldGenReed;
import imported.world.gen.WorldGenTrees;
import imported.world.gen.WorldGenerator;

import java.util.Random;

import net.rush.chunk.Chunk;
import net.rush.chunk.ChunkCoords;
import net.rush.chunk.ChunkManager;
import net.rush.model.Block;
import net.rush.util.BlockArrayConverter;

public class AlphaWorldGenerator implements net.rush.world.WorldGenerator {

	private Random rand;
	private NoiseGeneratorOctaves noise1, noise2, noise3, sandAndGravelNoise, stoneNoise, noise6, noise7, decorationNoise;
	private World world;
	private double[] heightArray;
	private double[] sandNoiseCache = new double[256];
	private double[] gravelNoiseCache = new double[256];
	private double[] topStoneNoiseCache = new double[256];
	private MapGenCaves mapGenCaves = new MapGenCaves();

	double[] noise3D, noise1D, noise2D, heightNoise, heightNoise2;

	public AlphaWorldGenerator(World world) {
		this.world = world;
		rand = new Random(world.seed);
		noise1 = new NoiseGeneratorOctaves(rand, 16);
		noise2 = new NoiseGeneratorOctaves(rand, 16);
		noise3 = new NoiseGeneratorOctaves(rand, 8);
		sandAndGravelNoise = new NoiseGeneratorOctaves(rand, 4);
		stoneNoise = new NoiseGeneratorOctaves(rand, 4);
		noise6 = new NoiseGeneratorOctaves(rand, 10);
		noise7 = new NoiseGeneratorOctaves(rand, 16);
		decorationNoise = new NoiseGeneratorOctaves(rand, 8);
	}

	@Override
	public Chunk generate(World world, int x, int z) {
		rand.setSeed(x * 341873128712L + z * 132897987541L);

		byte[] blocksArray = new byte[16 * 16 * 128];

		this.generateHeights(x, z, blocksArray);
		this.generateTerrain(x, z, blocksArray);
		mapGenCaves.initDecoration(world, x, z, blocksArray);
		// chunk.b();

		return new Chunk(new ChunkCoords(x, z), BlockArrayConverter.convertBlockArray(blocksArray));
	}

	private void generateHeights(int x, int z, byte[] blockArray) {
		byte b0 = 4;
		byte oceanHeight = 64;
		int k = b0 + 1;
		byte b2 = 17;
		int l = b0 + 1;

		heightArray = this.provideSections(heightArray, x * b0, 0, z * b0, k, b2, l);

		for (int i1 = 0; i1 < b0; ++i1)
			for (int j1 = 0; j1 < b0; ++j1)
				for (int liquidHeight = 0; liquidHeight < 16; ++liquidHeight) {
					double d0 = 0.125D;
					double d1 = heightArray[((i1 + 0) * l + j1 + 0) * b2 + liquidHeight + 0];
					double d2 = heightArray[((i1 + 0) * l + j1 + 1) * b2 + liquidHeight + 0];
					double d3 = heightArray[((i1 + 1) * l + j1 + 0) * b2 + liquidHeight + 0];
					double d4 = heightArray[((i1 + 1) * l + j1 + 1) * b2 + liquidHeight + 0];
					double d5 = (heightArray[((i1 + 0) * l + j1 + 0) * b2 + liquidHeight + 1] - d1) * d0;
					double d6 = (heightArray[((i1 + 0) * l + j1 + 1) * b2 + liquidHeight + 1] - d2) * d0;
					double d7 = (heightArray[((i1 + 1) * l + j1 + 0) * b2 + liquidHeight + 1] - d3) * d0;
					double d8 = (heightArray[((i1 + 1) * l + j1 + 1) * b2 + liquidHeight + 1] - d4) * d0;

					for (int tries1 = 0; tries1 < 8; ++tries1) {
						double probModifier = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * probModifier;
						double d13 = (d4 - d2) * probModifier;

						for (int tries2 = 0; tries2 < 4; ++tries2) {
							int index = tries2 + i1 * 4 << 11 | 0 + j1 * 4 << 7 | liquidHeight * 8 + tries1;
							short maxHeight = 128;
							double d14 = 0.25D;
							double d15 = d10;
							double d16 = (d11 - d10) * d14;

							for (int tries3 = 0; tries3 < 4; ++tries3) {
								int blockId = 0;

								if (liquidHeight * 8 + tries1 < oceanHeight)
									blockId = Block.STATIONARY_WATER.id;

								if (d15 > 0.0D)
									blockId = Block.STONE.id;

								blockArray[index] = (byte) blockId;
								index += maxHeight;
								d15 += d16;
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
	}

	private void generateTerrain(int posX, int posZ, byte[] blockArray) {
		byte oceanHeight = 64;
		double probModifier = 0.03125D;

		sandNoiseCache = sandAndGravelNoise.generateNoise(sandNoiseCache, posX * 16, posZ * 16, 0.0D, 16, 16, 1, probModifier, probModifier, 1.0D);
		gravelNoiseCache = sandAndGravelNoise.generateNoise(gravelNoiseCache, posZ * 16, 109.0134D, posX * 16, 16, 1, 16, probModifier, 1.0D, probModifier);
		topStoneNoiseCache = stoneNoise.generateNoise(topStoneNoiseCache, posX * 16, posZ * 16, 0.0D, 16, 16, 1, probModifier * 2.0D, probModifier * 2.0D, probModifier * 2.0D);

		for (int blockX = 0; blockX < 16; ++blockX)
			for (int blockZ = 0; blockZ < 16; ++blockZ) {
				boolean sand = sandNoiseCache[blockX + blockZ * 16] + rand.nextDouble() * 0.2D > 0.0D;
				boolean gravel = gravelNoiseCache[blockX + blockZ * 16] + rand.nextDouble() * 0.2D > 3.0D;
				int topStoneDeterminer = (int) (topStoneNoiseCache[blockX + blockZ * 16] / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
				int counter = -1;
				byte topBlockId = (byte) Block.GRASS.id;
				byte blockId = (byte) Block.DIRT.id;

				for (int blockY = 127; blockY >= 0; --blockY) {
					int index = (blockX * 16 + blockZ) * 128 + blockY;

					if (blockY <= 0 + rand.nextInt(6) - 1)
						blockArray[index] = (byte) Block.BEDROCK.id;
					else {
						byte b3 = blockArray[index];

						if (b3 == 0)
							counter = -1;
						else if (b3 == Block.STONE.id)
							if (counter == -1) {
								if (topStoneDeterminer <= 0) {
									topBlockId = 0;
									blockId = (byte) Block.STONE.id;
								} else if (blockY >= oceanHeight - 4 && blockY <= oceanHeight + 1) {
									topBlockId = (byte) Block.GRASS.id;
									blockId = (byte) Block.DIRT.id;
									if (gravel)
										topBlockId = 0;

									if (gravel)
										blockId = (byte) Block.GRAVEL.id;

									if (sand)
										topBlockId = (byte) Block.SAND.id;

									if (sand)
										blockId = (byte) Block.SAND.id;
								}

								if (blockY < oceanHeight && topBlockId == 0)
									topBlockId = (byte) Block.STATIONARY_WATER.id;

								counter = topStoneDeterminer;
								if (blockY >= oceanHeight - 1)
									blockArray[index] = topBlockId;
								else
									blockArray[index] = blockId;
							} else if (counter > 0) {
								--counter;
								blockArray[index] = blockId;
							}
					}
				}
			}
	}

	private double[] provideSections(double[] heightArray, int x, int y, int z, int width, int depth, int height) {
		if (heightArray == null)
			heightArray = new double[width * depth * height];

		double d0 = 684.412D;
		double d1 = 684.412D;

		heightNoise = noise6.generateNoise(heightNoise, x, y, z, width, 1, height, 1.0D, 0.0D, 1.0D);
		heightNoise2 = noise7.generateNoise(heightNoise2, x, y, z, width, 1, height, 100.0D, 0.0D, 100.0D);
		noise3D = noise3.generateNoise(noise3D, x, y, z, width, depth, height, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
		noise1D = this.noise1.generateNoise(noise1D, x, y, z, width, depth, height, d0, d1, d0);
		noise2D = this.noise2.generateNoise(noise2D, x, y, z, width, depth, height, d0, d1, d0);
		int index = 0;
		int l1 = 0;

		for (int xPos = 0; xPos < width; ++xPos)
			for (int zPos = 0; zPos < height; ++zPos) {
				double d2 = (heightNoise[l1] + 256.0D) / 512.0D;

				if (d2 > 1.0D)
					d2 = 1.0D;

				double d3 = 0.0D;
				double d4 = heightNoise2[l1] / 8000.0D;

				if (d4 < 0.0D)
					d4 = -d4;

				d4 = d4 * 3.0D - 3.0D;
				if (d4 < 0.0D) {
					d4 /= 2.0D;
					if (d4 < -1.0D)
						d4 = -1.0D;

					d4 /= 1.4D;
					d4 /= 2.0D;
					d2 = 0.0D;
				} else {
					if (d4 > 1.0D)
						d4 = 1.0D;

					d4 /= 6.0D;
				}

				d2 += 0.5D;
				d4 = d4 * depth / 16.0D;
				double d5 = depth / 2.0D + d4 * 4.0D;

				++l1;

				for (int yPos = 0; yPos < depth; ++yPos) {
					double theHeight = 0.0D;
					double d7 = (yPos - d5) * 12.0D / d2;

					if (d7 < 0.0D)
						d7 *= 4.0D;

					double d8 = noise1D[index] / 512.0D;
					double d9 = noise2D[index] / 512.0D;
					double d10 = (noise3D[index] / 10.0D + 1.0D) / 2.0D;

					if (d10 < 0.0D)
						theHeight = d8;
					else if (d10 > 1.0D)
						theHeight = d9;
					else
						theHeight = d8 + (d9 - d8) * d10;

					theHeight -= d7;
					double d11;

					if (yPos > depth - 4) {
						d11 = (yPos - (depth - 4)) / 3.0F;
						theHeight = theHeight * (1.0D - d11) + -10.0D * d11;
					}

					if (yPos < d3) {
						d11 = (d3 - yPos) / 4.0D;
						if (d11 < 0.0D)
							d11 = 0.0D;

						if (d11 > 1.0D)
							d11 = 1.0D;

						theHeight = theHeight * (1.0D - d11) + -10.0D * d11;
					}

					heightArray[index] = theHeight;
					++index;
				}
			}

		return heightArray;
	}

	private long now = 0;
	
	private long took(long time) {
		long oldValue = now;
		now = System.currentTimeMillis();
		
		return System.currentTimeMillis() - oldValue;
	}
	
	private void logProgress(String what) {
		long delay = took(now);
		
		if(delay > 3000)
			System.out.println(what + " Took too long: " + delay + "ms!");
		//else
		//	System.out.println(what + " Took " + delay + " ms!");
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void populate(int chunkX, int chunkZ) {
		ChunkManager.decorating = true;
		// BlockSand.fallInstantly = true;
		int blockX = chunkX * 16;
		int blockZ = chunkZ * 16;
		
		System.out.println("[-!-] -->> Populating x: " + chunkX + " z:" + chunkZ);
		now = System.currentTimeMillis();
		
		rand.setSeed(world.seed);
		long xRandom = rand.nextLong() / 2L * 2L + 1L;
		long zRandom = rand.nextLong() / 2L * 2L + 1L;

		rand.setSeed(chunkX * xRandom + chunkZ * zRandom ^ world.seed);
		double d0 = 0.25D;

		int tries;
		int maybeX;
		int YorIndex;
		int xOrZ;

		// MOB SPAWNERS
		for (tries = 0; tries < 8; ++tries) {
			maybeX = blockX + rand.nextInt(16) + 8;
			YorIndex = rand.nextInt(128);
			xOrZ = blockZ + rand.nextInt(16) + 8;
			new WorldGenDungeons().generate(world, rand, maybeX, YorIndex, xOrZ);
		}
		
		logProgress("Dungeons");

		// CLAY DIRT AND GRAVEL		
		for (tries = 0; tries < 10; ++tries) {
			maybeX = blockX + rand.nextInt(16);
			YorIndex = rand.nextInt(128);
			xOrZ = blockZ + rand.nextInt(16);
			new WorldGenClay(32).generate(world, rand, maybeX, YorIndex, xOrZ);
		}
		
		logProgress("Clay");
		
		for (tries = 0; tries < /*20*/13; ++tries) {
			maybeX = blockX + rand.nextInt(16);
			YorIndex = rand.nextInt(128);
			xOrZ = blockZ + rand.nextInt(16);
			new WorldGenMinable(Block.DIRT.id, 32).generate(world, rand, maybeX, YorIndex, xOrZ);
		}

		logProgress("Dirt");
		
		for (tries = 0; tries < /*10*/6; ++tries) {
			maybeX = blockX + rand.nextInt(16);
			YorIndex = rand.nextInt(128);
			xOrZ = blockZ + rand.nextInt(16);
			new WorldGenMinable(Block.GRAVEL.id, 32).generate(world, rand, maybeX, YorIndex, xOrZ);
		}
		
		logProgress("Gravel");
		
		// VALUABLE ORES
		for (tries = 0; tries < /*20*/12; ++tries) {
			maybeX = blockX + rand.nextInt(16);
			YorIndex = rand.nextInt(128);
			xOrZ = blockZ + rand.nextInt(16);
			new WorldGenMinable(Block.COAL_ORE.id, /*16*/11).generate(world, rand, maybeX, YorIndex, xOrZ);
		}

		for (tries = 0; tries < /*20*/9; ++tries) {
			maybeX = blockX + rand.nextInt(16);
			YorIndex = rand.nextInt(64);
			xOrZ = blockZ + rand.nextInt(16);
			new WorldGenMinable(Block.IRON_ORE.id, /*8*/6).generate(world, rand, maybeX, YorIndex, xOrZ);
		}
		
		logProgress("Coal and Iron");

		for (tries = 0; tries < 2; ++tries) {
			maybeX = blockX + rand.nextInt(16);
			YorIndex = rand.nextInt(32);
			xOrZ = blockZ + rand.nextInt(16);
			new WorldGenMinable(Block.GOLD_ORE.id, /*8*/5).generate(world, rand, maybeX, YorIndex, xOrZ);
		}

		for (tries = 0; tries < /*8*/4; ++tries) {
			maybeX = blockX + rand.nextInt(16);
			YorIndex = rand.nextInt(16);
			xOrZ = blockZ + rand.nextInt(16);
			new WorldGenMinable(Block.REDSTONE_ORE.id, /*7*/4).generate(world, rand, maybeX, YorIndex, xOrZ);
		}
		
		logProgress("Gold and Redstone");

		for (tries = 0; tries < 1; ++tries) {
			maybeX = blockX + rand.nextInt(16);
			YorIndex = rand.nextInt(16);
			xOrZ = blockZ + rand.nextInt(16);
			new WorldGenMinable(Block.DIAMOND_ORE.id, /*7*/4).generate(world, rand, maybeX, YorIndex, xOrZ);
		}
		
		logProgress("Diamond");

		d0 = 0.5D;
		tries = (int) ((decorationNoise.a(blockX * d0, blockZ * d0) / 8.0D + rand.nextDouble() * 4.0D + 4.0D) / 3.0D);
		if (tries < 0)
			tries = 1;

		if (rand.nextInt(5/*10*/) == 0)
			++tries;
		
		// r start
		if (rand.nextInt(10) == 0)
			tries+= 3;
		// r end
		
		WorldGenerator treeGenerator = new WorldGenTrees();
		boolean bigTree = false; // r edit
		
		if (rand.nextInt(10) == 0) {
			treeGenerator = new WorldGenBigTree();
			bigTree = true;
		}
			
		int randZ;
		
		logProgress("Decoration noise setup");

		// TREES
		for (YorIndex = 0; YorIndex < tries; ++YorIndex) {
			xOrZ = blockX + 3 + rand.nextInt(bigTree ? 5 : 11);//+ rand.nextInt(16) + 8;
			randZ = blockZ + 3 + rand.nextInt(bigTree ? 5 : 11);//+ rand.nextInt(16) + 8;
			treeGenerator.setScale(1.0D, 1.0D, 1.0D);
			treeGenerator.generate(world, rand, xOrZ, world.getTerrainHeight(xOrZ, randZ), randZ);
		}
		
		logProgress("Trees");
		
		int l2;

		// FLOWERS AND MUSHROOMS
		for (YorIndex = 0; YorIndex < 2; ++YorIndex) {
			xOrZ = blockX + rand.nextInt(16) + 8;
			randZ = rand.nextInt(128);
			l2 = blockZ + rand.nextInt(16) + 8;
			new WorldGenFlowers(Block.YELLOW_FLOWER.id).generate(world, rand, xOrZ, randZ, l2);
		}

		if (rand.nextInt(2) == 0) {
			YorIndex = blockX + rand.nextInt(16) + 8;
			xOrZ = rand.nextInt(128);
			randZ = blockZ + rand.nextInt(16) + 8;
			new WorldGenFlowers(Block.RED_ROSE.id).generate(world, rand, YorIndex, xOrZ, randZ);
		}
		
		logProgress("Flowers");

		// TODO s
		// a) find out why is not generating (same with cacti)
		// b) move the check-for-staying methods in one class
		if (rand.nextInt(1) == 0) {
			YorIndex = blockX + rand.nextInt(16) + 8;
			xOrZ = rand.nextInt(128);
			randZ = blockZ + rand.nextInt(16) + 8;
			new WorldGenFlowers(Block.DEAD_BUSH.id).generate(world, rand, YorIndex, xOrZ, randZ);
		}

		logProgress("Dead Bush");
		
		if (rand.nextInt(4) == 0) {
			YorIndex = blockX + rand.nextInt(16) + 8;
			xOrZ = rand.nextInt(128);
			randZ = blockZ + rand.nextInt(16) + 8;
			new WorldGenFlowers(Block.BROWN_MUSHROOM.id).generate(world, rand, YorIndex, xOrZ, randZ);
		}

		if (rand.nextInt(8) == 0) {
			YorIndex = blockX + rand.nextInt(16) + 8;
			xOrZ = rand.nextInt(128);
			randZ = blockZ + rand.nextInt(16) + 8;
			new WorldGenFlowers(Block.RED_MUSHROOM.id).generate(world, rand, YorIndex, xOrZ, randZ);
		}
		
		logProgress("MushRooms");

		// SUGAR CANE
		for (YorIndex = 0; YorIndex < 13/*10*/; ++YorIndex) {
			xOrZ = blockX + rand.nextInt(16) + 8;
			randZ = rand.nextInt(128);
			l2 = blockZ + rand.nextInt(16) + 8;
			new WorldGenReed().generate(world, rand, xOrZ, randZ, l2);
		}

		// CACTI
		for (YorIndex = 0; YorIndex < 4/*1*/; ++YorIndex) {
			xOrZ = blockX + rand.nextInt(16) + 8;
			randZ = rand.nextInt(128);
			l2 = blockZ + rand.nextInt(16) + 8;
			new WorldGenCactus().generate(world, rand, xOrZ, randZ, l2);
		}

		logProgress("Cacti and Sugar Cane");
		
		// LAVA AND WATER LAKES
		for (YorIndex = 0; YorIndex < 60/*50*/; ++YorIndex) {
			xOrZ = blockX + rand.nextInt(16) + 8;
			randZ = rand.nextInt(rand.nextInt(120) + 8);
			l2 = blockZ + rand.nextInt(16) + 8;
			new WorldGenLiquids(Block.WATER.id).generate(world, rand, xOrZ, randZ, l2);
		}

		for (YorIndex = 0; YorIndex < 25/*20*/; ++YorIndex) {
			xOrZ = blockX + rand.nextInt(16) + 8;
			randZ = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
			l2 = blockZ + rand.nextInt(16) + 8;
			new WorldGenLiquids(Block.LAVA.id).generate(world, rand, xOrZ, randZ, l2);
		}
		
		logProgress("Liquids");
		
		// BlockSand.fallInstantly = false;
		ChunkManager.decorating = false;
	}
}
