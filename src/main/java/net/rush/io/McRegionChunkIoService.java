package net.rush.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rush.chunk.Chunk;
import net.rush.chunk.ChunkCoords;
import net.rush.io.region.RegionFile;
import net.rush.io.region.RegionFileCache;
import net.rush.util.nbt.ByteArrayTag;
import net.rush.util.nbt.ByteTag;
import net.rush.util.nbt.CompoundTag;
import net.rush.util.nbt.IntTag;
import net.rush.util.nbt.ListTag;
import net.rush.util.nbt.LongTag;
import net.rush.util.nbt.NBTInputStream;
import net.rush.util.nbt.NBTOutputStream;
import net.rush.util.nbt.Tag;

/**
 * An implementation of the {@link ChunkIoService} which reads and writes
 * McRegion maps.
 * <p />
 * Information on the McRegion file format can be found on the
 * <a href="http://mojang.com/2011/02/16/minecraft-save-file-format-in-beta-1-3">Mojang</a>
 * blog.


 */
public final class McRegionChunkIoService implements ChunkIoService {

	/**
	 * The size of a region - a 32x32 group of chunks.
	 */
	private static final int REGION_SIZE = 32;

	/**
	 * The root directory of the map.
	 */
	private File dir;

	/**
	 * The region file cache.
	 */
	private RegionFileCache cache = new RegionFileCache();
	
	// TODO: consider the session.lock file

	public McRegionChunkIoService(File dir) {
		this.dir = dir;
	}

	@Override
	public Chunk read(int x, int z) throws IOException {
		RegionFile region = cache.getRegionFile(dir, x, z);
		int regionX = x & (REGION_SIZE - 1);
		int regionZ = z & (REGION_SIZE - 1);
		if (!region.hasChunk(regionX, regionZ)) {
			return null;
		}

		DataInputStream in = region.getChunkDataInputStream(regionX, regionZ);
		Chunk chunk = new Chunk(new ChunkCoords(x, z));

		NBTInputStream nbt = new NBTInputStream(in, false);
		CompoundTag tag = (CompoundTag) nbt.readTag();
		Map<String, Tag> levelTags = ((CompoundTag) tag.getValue().get("Level")).getValue();

		byte[] tileData = ((ByteArrayTag) levelTags.get("Blocks")).getValue();
		chunk.setTypes(tileData);

		byte[] skyLightData = ((ByteArrayTag) levelTags.get("SkyLight")).getValue();
		byte[] blockLightData = ((ByteArrayTag) levelTags.get("BlockLight")).getValue();
		byte[] metaData = ((ByteArrayTag) levelTags.get("Data")).getValue();

		for (int cx = 0; cx < Chunk.WIDTH; cx++) {
			for (int cz = 0; cz < Chunk.HEIGHT; cz++) {
				for (int cy = 0; cy < Chunk.DEPTH; cy++) {
					boolean mostSignificantNibble = ((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy) % 2 == 1;
					int offset = ((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy) / 2;

					int skyLight, blockLight, meta;
					if (mostSignificantNibble) {
						skyLight = (skyLightData[offset] & 0xF0) >> 4;
						blockLight = (blockLightData[offset] & 0xF0) >> 4;
						meta = (metaData[offset] & 0xF0) >> 4;
					} else {
						skyLight = skyLightData[offset] & 0x0F;
						blockLight = blockLightData[offset] & 0x0F;
						meta = metaData[offset] & 0x0F;
					}

					chunk.setSkyLight(cx, cz, cy, skyLight);
					chunk.setBlockLight(cx, cz, cy, blockLight);
					chunk.setMetaData(cx, cz, cy, meta);
				}
			}
		}
		nbt.close();
		return chunk;
	}


	/**
	 * Writes a chunk to a McRegion file.
	 * WARNING! The files written by this method probably won't load in the Notchian server. Make backups.
	 */
	@Override
	public void write(int x, int z, Chunk chunk) throws IOException {
		CompoundTag levelTag = chunkToTag(chunk);
		RegionFile region = cache.getRegionFile(dir, x, z);
		int regionX = x & (REGION_SIZE - 1);
		int regionZ = z & (REGION_SIZE - 1);

		DataOutputStream out = region.getChunkDataOutputStream(regionX, regionZ);
		NBTOutputStream nbtOut = new NBTOutputStream(out, false);
		try {
			Map<String, Tag> tagMap = new HashMap<String, Tag>(1);
			tagMap.put("Level", levelTag);

			CompoundTag tag = new CompoundTag("", tagMap);
			nbtOut.writeTag(tag);
		} finally {
			out.close();
		}
		nbtOut.close();
	}

	private CompoundTag chunkToTag(Chunk chunk) {
		final int size = Chunk.WIDTH * Chunk.HEIGHT * Chunk.DEPTH;

		byte[] tileData = new byte[size];
		byte[] skyLightData = new byte[size / 2];
		byte[] blockLightData = new byte[size / 2];
		byte[] metaData = new byte[size / 2];
		byte[] heightMapData = new byte[Chunk.WIDTH * Chunk.HEIGHT];

		for (int cx = 0; cx < Chunk.WIDTH; cx++) {
			for (int cz = 0; cz < Chunk.HEIGHT; cz++) {
				for (int cy = 0; cy < Chunk.DEPTH; cy+=2) {
					int blockOffset = ((cx * Chunk.HEIGHT + cz) * Chunk.DEPTH + cy);
					int offset = blockOffset / 2;
					tileData[blockOffset] = (byte) chunk.getType(cx, cz, cy);
					tileData[blockOffset + 1] = (byte) chunk.getType(cx, cz, cy + 1);
					skyLightData[offset] = (byte) ((chunk.getSkyLight(cx, cz, cy + 1) << 4) | chunk.getSkyLight(cx, cz, cy));
					blockLightData[offset] = (byte) ((chunk.getBlockLight(cx, cz, cy + 1) << 4) | chunk.getBlockLight(cx, cz, cy));
					metaData[offset] = (byte) ((chunk.getMetaData(cx, cz, cy + 1) << 4) | chunk.getMetaData(cx, cz, cy));
				}
			}
		}

		Map<String, Tag> _old_levelTags = new HashMap<String, Tag>();
		_old_levelTags.put("Blocks", new ByteArrayTag("Blocks", chunk.types));
		_old_levelTags.put("Data", new ByteArrayTag("Data", metaData));
		_old_levelTags.put("SkyLight", new ByteArrayTag("SkyLight", skyLightData));
		_old_levelTags.put("BlockLight", new ByteArrayTag("BlockLight", blockLightData));
		// TODO: Heightmap, entities, tileentities, lastupdate
		_old_levelTags.put("HeightMap", new ByteArrayTag("HeightMap", heightMapData));
		_old_levelTags.put("Entities", chunkEntitiesToTag(chunk));
		_old_levelTags.put("TileEntities", chunkTileEntitiesToTag(chunk));
		_old_levelTags.put("LastUpdate", new LongTag("LastUpdate", 0));

		_old_levelTags.put("xPos", new IntTag("xPos", chunk.getX()));
		_old_levelTags.put("zPos", new IntTag("zPos", chunk.getZ()));
		// TODO: terrainpopulated
		_old_levelTags.put("TerrainPopulated", new ByteTag("TerrainPopulated", (byte) 0));
		
		// begin new
		CompoundTag tag = new CompoundTag("Level");
		tag.setBoolean("TerrainPopulated", true);
		tag.setInteger("xPos", chunk.getX());
		tag.setInteger("zPos", chunk.getZ());
		tag.setLong("LastUpdate", 0);
		tag.setByteArray("Biomes", new byte[256]);
		tag.setTag("Entities", chunkEntitiesToTag(chunk));
		
		ListTag sections = new ListTag("Sections", CompoundTag.class);
		
		CompoundTag section = new CompoundTag("");
		section.setByte("Y", (byte)0);
		section.setByteArray("Blocks", chunk.types);
		section.setByteArray("Data", metaData);
		section.setByteArray("BlockLight", blockLightData);
		section.setByteArray("SkyLight", blockLightData);
		
		sections.add(section);
		
		tag.setTag("Sections", sections);
		// end new
		
		return new CompoundTag("Level", _old_levelTags);
	}

	// TODO
	private ListTag chunkEntitiesToTag(Chunk chunk) {
		List<CompoundTag> entityTags = new ArrayList<CompoundTag>();
		return new ListTag("Entities", CompoundTag.class, entityTags);
	}

	// TODO
	private ListTag chunkTileEntitiesToTag(Chunk chunk) {
		List<CompoundTag> entityTags = new ArrayList<CompoundTag>();
		return new ListTag("TileEntities", CompoundTag.class, entityTags);
	}

}

