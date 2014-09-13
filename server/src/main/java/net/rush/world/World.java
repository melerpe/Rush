package net.rush.world;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import net.rush.Server;
import net.rush.chunk.Chunk;
import net.rush.chunk.ChunkCoords;
import net.rush.chunk.ChunkManager;
import net.rush.io.ChunkIoService;
import net.rush.model.Block;
import net.rush.model.Entity;
import net.rush.model.EntityManager;
import net.rush.model.ItemStack;
import net.rush.model.Material;
import net.rush.model.Player;
import net.rush.model.Position;
import net.rush.model.entity.EntityRegistry;
import net.rush.model.entity.ItemEntity;
import net.rush.model.misc.NextTickEntry;
import net.rush.model.misc.Vec3Pool;
import net.rush.protocol.packets.PacketBlockChange;
import net.rush.protocol.packets.PacketTimeUpdate;
import net.rush.util.nbt.CompoundTag;
import net.rush.util.nbt.NBTOutputStream;

import org.apache.commons.lang3.Validate;
import org.bukkit.Difficulty;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.entity.EntityType;

/**
 * A class which represents the in-game world.
 */
public class World {

	/**
	 * The number of pulses in a Minecraft day.
	 */
	private static final int PULSES_PER_DAY = 24000;

	/**
	 * The spawn position.
	 */
	private Position spawnPosition = new Position(0, 110, 0);

	/**
	 * The chunk manager.
	 */
	private ChunkManager chunks;

	/**
	 * The entity manager.
	 */
	private final EntityManager entities = new EntityManager();
	private Queue<NextTickEntry> tickQueue = new LinkedList<NextTickEntry>();
	private long time = 0;
	private int maxHeight = 256;
	private String name;

	public Set<ChunkCoords> activeChunks = new HashSet<ChunkCoords>();
	public final Vec3Pool vectorPool = new Vec3Pool(300, 2000);
	public Random rand = new Random();
	public final long seed = 123456;
	
	public final int worldYbits = 7;
	public final int xShift = worldYbits + 4;

	protected int randomBlockChooser = rand.nextInt();
	public org.bukkit.World bukkit_world = null;
	

	/**
	 * Creates a new world with the specified chunk I/O service and world
	 * generator.
	 * 
	 * @param service
	 *            The chunk I/O service.
	 * @param generator
	 *            The world generator.
	 */
	public World(String name) {
		this.name = name;

	}
	
	public void setChunkManager(ChunkIoService service, WorldGenerator generator) {
		chunks = new ChunkManager(this, service, generator);
	}

	/**
	 * Updates all the entities within this world.
	 */
	public int pulse() {
		long now = System.currentTimeMillis();
		for (Entity entity : entities) {
			entity.pulse();
			entity.updateEntity();
		}

		for (Entity entity : entities)
			entity.reset();

		advanceTime();
		resetActiveChunks();
		tickFromQueue();
		tickActiveChunks();	
		return (int) (System.currentTimeMillis() - now);
	}

	protected void resetActiveChunks() {
		activeChunks.clear();

		int chunkX, chunkZ;

		// how many chunks from player should be ticked (redstone activated, grass grown etc)
		// is 7 in notchian server
		int activationRadius = 6;

		for (Player pl : getPlayers()) {
			chunkX = ((int) pl.getPosition().x) / Chunk.WIDTH;
			chunkZ = ((int) pl.getPosition().z) / Chunk.HEIGHT;

			for (int x = (chunkX - activationRadius); x <= (chunkX + activationRadius); x++) {
				for (int z = (chunkZ - activationRadius); z <= (chunkZ + activationRadius); z++) {
					ChunkCoords key = new ChunkCoords(x, z);
					if (!activeChunks.contains(key))
						activeChunks.add(key);
				}
			}
		}
	}

	/**
	 * Gets the chunk manager.
	 * 
	 * @return The chunk manager.
	 */
	public ChunkManager getChunks() {
		return chunks;
	}

	/**
	 * Gets the entity manager.
	 * 
	 * @return The entity manager.
	 */
	public EntityManager getEntities() {
		return entities;
	}

	/**
	 * Gets a collection of all the players within this world.
	 * 
	 * @return A {@link Collection} of {@link Player} objects.
	 */
	public Collection<Player> getPlayers() {
		return entities.getAll(Player.class);
	}

	public Player getPlayer(String name) {	
		Validate.notNull(name, "Name cannot be null");

		Player found = null;
		String lowerName = name.toLowerCase();
		int delta = Integer.MAX_VALUE;

		for (Player player : getPlayers()) {
			if (player.getName().toLowerCase().startsWith(lowerName)) {
				int curDelta = player.getName().length() - lowerName.length();
				if (curDelta < delta) {
					found = player;
					delta = curDelta;
				}
				if (curDelta == 0) 
					break;
			}
		}
		return found;
	}	

	/**
	 * Gets the spawn position.
	 * 
	 * @return The spawn position.
	 */
	public Position getSpawnPosition() {
		return spawnPosition;
	}

	/**
	 * Gets the current time.
	 * 
	 * @return The current time.
	 */
	public long getTime() {
		return time;
	}

	
	/**
	 * Sets the current time.
	 * 
	 * @param time The current time.
	 */
	public void setTime(long time) {
		this.time = time % PULSES_PER_DAY;

		PacketTimeUpdate msg = new PacketTimeUpdate(0, time); // TODO Correct world age?
		for (Player player : getPlayers())
			player.getSession().send(msg);
	}

	/**
	 * Advances the time forwards, should be called every pulse.
	 */
	private void advanceTime() {
		time = (time + 1) % PULSES_PER_DAY;
		// TODO: every now and again we should broadcast the time to all
		// players to keep things in sync
	}

	public Chunk getChunkFromBlockCoords(int x, int z) {
		return getChunkFromChunkCoords(x >> 4, z >> 4);
	}

	public Chunk getChunkFromChunkCoords(int x, int z) {
		return chunks.getChunk(x, z);
	}

	public boolean isAir(int x, int y, int z) {
		return getType(x, y, z) == 0;
	}

	public void setAir(int x, int y, int z) {
		setType(x, y, z, 0, true);
		setBlockData(x, y, z, 0, false);

		callNeighborChange(x, y, z, 0);
	}

	private void neighborChange(int x, int y, int z, int data) {
		Block block = Block.byId[getType(x, y, z)];

		if (block != null)
			block.onNeighborBlockChange(this, x, y, z, data);
	}

	private void callNeighborChange(int x, int y, int z, int data) {
		neighborChange(x - 1, y, z, data);
		neighborChange(x + 1, y, z, data);
		neighborChange(x, y - 1, z, data);
		neighborChange(x, y + 1, z, data);
		neighborChange(x, y, z - 1, data);
		neighborChange(x, y, z + 1, data);
	}

	public void setDataWithNotify(int x, int y, int z, int data, boolean notifyPlayers) {
		setBlockData(x, y, z, data, notifyPlayers);
		callNeighborChange(x, y, z, data);
	}

	public void setTypeWithNotify(int x, int y, int z, int type, boolean notifyPlayers) {
		setType(x, y, z, type, notifyPlayers);
		callNeighborChange(x, y, z, getBlockData(x, y, z));
	}

	public void setTypeAndDataWithNotify(int x, int y, int z, int type, int data, boolean notifyPlayers) {
		setTypeAndData(x, y, z, type, data, notifyPlayers);
		callNeighborChange(x, y, z, data);
	}

	////////////
	public void setTypeAndData(int x, int y, int z, int type, int data, boolean notifyPlayers) {
		setType(x, y, z, type, notifyPlayers);
		setBlockData(x, y, z, data, notifyPlayers);
	}

	public Material getMaterial(int x, int y, int z) {
		int blockId = getType(x, y, z);

		return blockId == 0 ? Material.AIR : Block.byId[blockId].material;

	}

	/** @param notifyPlayers - should we send BlockChangePacket to all players in the world? */
	public void setType(int x, int y, int z, int type, boolean notifyPlayers) {
		int chunkX = x / Chunk.WIDTH + ((x < 0 && x % Chunk.WIDTH != 0) ? -1 : 0);
		int chunkZ = z / Chunk.HEIGHT + ((z < 0 && z % Chunk.HEIGHT != 0) ? -1 : 0);

		int localX = (x - chunkX * Chunk.WIDTH) % Chunk.WIDTH;
		int localZ = (z - chunkZ * Chunk.HEIGHT) % Chunk.HEIGHT;

		Chunk chunk = chunks.getChunk(chunkX, chunkZ);
		chunk.setType(localX, y, localZ, type);

		if(notifyPlayers) 
			sendBlockChangePacket(x, y, z);
	}

	public void setBlockData(int x, int y, int z, int data, boolean notifyPlayers) {
		int chunkX = x / Chunk.WIDTH + ((x < 0 && x % Chunk.WIDTH != 0) ? -1 : 0);
		int chunkZ = z / Chunk.HEIGHT + ((z < 0 && z % Chunk.HEIGHT != 0) ? -1 : 0);

		int localX = (x - chunkX * Chunk.WIDTH) % Chunk.WIDTH;
		int localZ = (z - chunkZ * Chunk.HEIGHT) % Chunk.HEIGHT;

		Chunk chunk = chunks.getChunk(chunkX, chunkZ);
		chunk.setMetaData(localX, y, localZ, data);

		if(notifyPlayers) 
			sendBlockChangePacket(x, y, z);
	}

	public void sendBlockChangePacket(int x, int y, int z) {
		PacketBlockChange packet = new PacketBlockChange(x, y, z, this);

		for(Player pl : getPlayers())
			pl.getSession().send(packet);
	}

	public int getType(int x, int y, int z) {
		if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
			if (y < 0)
				return 0;
			if (y >= 256)
				return 0;

			return getChunkFromBlockCoords(x, z).getType(x & 15, y, z & 15);
		}
		return 0;
	}

	public int getBlockData(int x, int y, int z) {
		int chunkX = x / Chunk.WIDTH + ((x < 0 && x % Chunk.WIDTH != 0) ? -1 : 0);
		int chunkZ = z / Chunk.HEIGHT + ((z < 0 && z % Chunk.HEIGHT != 0) ? -1 : 0);

		int localX = (x - chunkX * Chunk.WIDTH) % Chunk.WIDTH;
		int localZ = (z - chunkZ * Chunk.HEIGHT) % Chunk.HEIGHT;

		Chunk chunk = chunks.getChunk(chunkX, chunkZ);
		return chunk.getMetaData(localX, y, localZ);
	}

	public void dropItem(double x, double y, double z, int type, int count, int data) {
		ItemStack item = new ItemStack(type, count, data);

		float offset = 0.7F;
		double randX = rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
		double randY = rand.nextFloat() * offset + (1.0F - offset) * 0.5D;
		double randZ = rand.nextFloat() * offset + (1.0F - offset) * 0.5D;

		ItemEntity itemEntity = new ItemEntity(this, item, x + randX, y + randY, z + randZ);
		spawnEntity(itemEntity);
	}

	public void dropItem(double x, double y, double z, int type) {
		dropItem(x, y, z, type, 1, 0);
	}

	public Entity spawnEntity(Position pos, EntityType type) {
		try {
			Class<? extends Entity> clazz = EntityRegistry.entityLookup(type);
			Entity entity = clazz.getDeclaredConstructor(World.class).newInstance(this);
			entity.setPosition(pos);

			return entity;

		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ex) {
			throw new Error("Error spawning entity" + type.toString(), ex);
		}
	}

	/** Retursn the entity id. */
	public int spawnEntity(Entity en) {
		return getEntities().allocate(en);
	}

	// //

	public Difficulty getDifficulty() {
		return Difficulty.NORMAL;
	}

	public Environment getEnvironment() {
		return Environment.NORMAL;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public String getName() {
		return name;
	}

	public WorldType getWorldType() {
		return WorldType.NORMAL;
	}

	public void save() {
		try {
			this.chunks.saveAll();
			this.saveWorldInfo();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void setSpawnLocation(int x, int y, int z) {
		spawnPosition = new Position(x, y, z);
	}

	public int getBlockLightValue(int x, int y, int z) {
		return 0;
	}

	public void playSound(double x, double y, double z, String sound, float volume, float pitch) {
		for (Player pl : getPlayers())
			pl.playSound(sound, x, y, z, volume, pitch);
	}

	public void playSound(double x, double y, double z, Sound sound, float volume, float pitch) {
		for (Player pl : getPlayers())
			pl.playSound(sound, x, y, z, volume, pitch);
	}

	public void playEffect(int effectId, int x, int y, int z, int data) {
		for (Player pl : getPlayers())
			pl.playEffect(effectId, x, y, z, data);
	}

	public void playEffect(Effect effect, int x, int y, int z, int data) {
		playEffect(effect.getId(), x, y, z, data);
	}

	/**
	 * Used on block break.
	 */
	public void playEffectExceptTo(int effectId, int x, int y, int z, int data, Player ignoredPl) {
		for (Player pl : getPlayers())
			if(pl != ignoredPl && pl.isWithinDistance(ignoredPl))
				pl.playEffect(effectId, x, y, z, data);
	}

	public void playEffectExceptTo(Effect effect, int x, int y, int z, int data, Player ignoredPl) {
		playEffectExceptTo(effect.getId(), x, y, z, data, ignoredPl);
	}

	/**
	 * Runs through the list of updates to run and ticks them
	 */
	public void tickFromQueue() {
		if(tickQueue.isEmpty())
			return;

		NextTickEntry nextTick = tickQueue.poll();

		if (chunks.chunkExist(nextTick.posX, nextTick.posZ)) {
			int id = getType(nextTick.posX, nextTick.posY, nextTick.posZ);

			if (id > 0 && Block.isAssociatedWith(id, nextTick.blockId)) 
				Block.byId[id].tick(this, nextTick.posX, nextTick.posY, nextTick.posZ, rand);
		} else 
			scheduleBlockUpdate(nextTick.posX, nextTick.posY, nextTick.posZ, nextTick.blockId, 0);
	}

	public void scheduleBlockUpdate(int x, int y, int z, int blockID, int priority) {
		NextTickEntry tickEntry = new NextTickEntry(x, y, z, blockID);
		byte radius = 0; // FIXME

		if (chunks.chunkExists(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius)) {
			if (blockID > 0) {
				tickEntry.setScheduledTime(getTime());
				tickEntry.setPriority(priority);
			}

			if(!tickQueue.contains(tickEntry))
				tickQueue.add(tickEntry);
		}
	}

	protected void tickActiveChunks() {

		Iterator<ChunkCoords> it = activeChunks.iterator();

		while(it.hasNext()) {
			ChunkCoords coords = it.next();
			Chunk chunk = getChunkFromChunkCoords(coords.x, coords.z);
			
			int chunkX = coords.x * Chunk.WIDTH;
			int chunkZ = coords.z * Chunk.HEIGHT;

			// In 3 rounds, picks up random block in a chunk and tick it,
			// x y z is converted to world x y z
			// Since Minecraft 1.8 this is customizable in attribute "randomTick" (or similar)
			for(int count = 0; count < 3; count++) {

				// the rand.nextInt can be 0 and is always one number lower than the argument
				int x = rand.nextInt(16);
				int y = rand.nextInt(maxHeight);
				int z = rand.nextInt(16);

				int type = chunk.getType(x, y, z);

				if(Block.byId[type] == null)
					throw new NullPointerException("Block " + org.bukkit.Material.getMaterial(type) + " missing!");
				
				if(type != 0)
					if(Block.byId[type].getTickRandomly())
						Block.byId[type].tick(this, x + chunkX, y, z + chunkZ, rand);
			}

			// Ticks every block, laggy
			// code commented for later purposes
			/*for(int xx = chunkX; xx < chunkX + Chunk.WIDTH; xx++) {
				for(int zz = chunkZ; zz < chunkZ + Chunk.HEIGHT; zz++)	{		    	
					for(int yy = 0; yy < Chunk.DEPTH; yy++) {

						int type = getTypeId(xx, yy, zz);

						if(type == 0) 
							continue;

						Block block = Block.byId[type];

						if(block != null && block.getTickRandomly())
							block.tick(this, xx, yy, zz, rand);

					}
				}
			}*/
		}
	}

	public Block getHighestBlockAt(int x, int z ) {
		for (int y = maxHeight - 1; y > 0; --y) {
			int blockId = getType(x, y, z);

			if (blockId != Block.AIR.id)
				return Block.byId[blockId];
		}
		return null;
	}

	public int getTerrainHeight(int x, int z) {
		for (int y = maxHeight - 1; y > 0; --y) {
			int blockId = getType(x, y, z);

			if (blockId != Block.AIR.id)
				return y + 1;
		}
		return 0;
	}
	
	public void saveWorldInfo() {
		setSessionLock();

		CompoundTag worldNbt = getWorldNbtTag();
		CompoundTag mainNbt = new CompoundTag();

		mainNbt.setTag("Data", worldNbt);
		try {
			File file = new File(Server.getServer().getProperties().levelName, "level.dat");

			if (file.exists())
				file.delete();

			writeGzippedCompound(mainNbt, new FileOutputStream(file));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setSessionLock() {
		try {
			File file = new File(Server.getServer().getProperties().levelName, "session.lock");
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
			try {
				dos.writeLong(System.currentTimeMillis());
			} finally {
				dos.close();
			}
		} catch (IOException ex) {
			throw new RuntimeException("Failed to check session lock, aborting", ex);
		}
	}

	// TODO "Should" be compatible with notchian server, need some more work.
	private CompoundTag getWorldNbtTag() {
		CompoundTag tag = new CompoundTag();
		tag.setBoolean("hardcore", false);
		tag.setBoolean("MapFeatures", true);
		tag.setBoolean("raining", false);
		tag.setBoolean("thundering", false);
		tag.setInteger("GameType", 0);
		tag.setInteger("generatorVersion", 0);
		tag.setInteger("rainTime", 0);
		tag.setInteger("SpawnX", (int)spawnPosition.x);
		tag.setInteger("SpawnY", (int)spawnPosition.y);
		tag.setInteger("SpawnZ", (int)spawnPosition.z);
		tag.setInteger("thunderTime", 0);
		tag.setInteger("version", 19113);
		tag.setLong("LastPlayed", 0);
		tag.setLong("RandomSeed", 0);
		tag.setLong("SizeOnDisk", 0);
		tag.setLong("time", 0);
		tag.setString("LevelName", "world");

		return tag;
	}

	private void writeGzippedCompound(CompoundTag tag, OutputStream out) throws IOException {
		try {
			NBTOutputStream nbtOut = null;
			try {
				nbtOut = new NBTOutputStream(out);
				nbtOut.writeTag(tag);
				nbtOut.close();
				nbtOut = null;
			} finally {
				if (nbtOut != null)
					nbtOut.close();
			}

		} finally {
			out.close();
		}
	}
}
