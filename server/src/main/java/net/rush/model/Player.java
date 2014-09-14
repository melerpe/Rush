package net.rush.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lombok.Getter;
import net.rush.Server;
import net.rush.chunk.Chunk;
import net.rush.chunk.ChunkCoords;
import net.rush.inventory.Inventory;
import net.rush.inventory.PlayerInventory;
import net.rush.model.entity.ItemEntity;
import net.rush.protocol.Packet;
import net.rush.protocol.Session;
import net.rush.protocol.packets.Packet18Title;
import net.rush.protocol.packets.Packet18Title.TitleAction;
import net.rush.protocol.packets.PacketAnimation;
import net.rush.protocol.packets.PacketAnimation.AnimType;
import net.rush.protocol.packets.PacketChangeGameState;
import net.rush.protocol.packets.PacketChat;
import net.rush.protocol.packets.PacketDestroyEntity;
import net.rush.protocol.packets.PacketEntityEquipment;
import net.rush.protocol.packets.PacketNamedEntitySpawn;
import net.rush.protocol.packets.PacketNamedSoundEffect;
import net.rush.protocol.packets.PacketOpenWindow;
import net.rush.protocol.packets.PacketPlayerListItem;
import net.rush.protocol.packets.PacketPlayerLookAndPosition;
import net.rush.protocol.packets.PacketSetSlot;
import net.rush.protocol.packets.PacketSoundOrParticleEffect;
import net.rush.protocol.packets.PacketSpawnPosition;
import net.rush.protocol.packets.PacketUpdateHealth;
import net.rush.protocol.utils.MetaParam;
import net.rush.util.MathHelper;
import net.rush.util.enums.GameStateReason;
import net.rush.util.enums.InventoryEnum;
import net.rush.util.enums.SoundEnum;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;

/**
 * Represents an in-game player.
 */
public final class Player extends LivingEntity implements CommandSender {

	/**
	 * The normal height of a player's eyes above their feet.
	 */
	private final double NORMAL_EYE_HEIGHT = 1.62D;

	/**
	 * The height of a player's eyes above their feet when they are crouching.
	 */
	private final double CROUCH_EYE_HEIGHT = 1.42D;

	@Getter
	private final String name;
	@Getter
	private GameMode gamemode;
	@Getter
	private boolean sprinting = false;
	@Getter
	private boolean riding = false;
	@Getter
	private boolean onGround = true;

	@Getter
	private float exhaustion = 0F;
	@Getter
	private int food = 20;
	@Getter
	private float saturation = 0;
	@Getter
	private boolean alive = true;
	@Getter
	private final PlayerInventory inventory = new PlayerInventory();
	@Getter
	private final Session session;
	@Getter
	private HashSet<Entity> knownEntities = new HashSet<Entity>();
	@Getter
	private HashSet<ChunkCoords> knownChunks = new HashSet<ChunkCoords>();
	@Getter
	private boolean crouching = false;
	@Getter
	private ItemStack itemOnCursor;
	public int windowId = 0;
	public int airTicks = 0;

	/**
	 * Creates a new player and adds it to the world.
	 * @param session The player's session.
	 * @param name The player's name.
	 */
	public Player(Session session, String name) {
		super(session.getServer().getWorld(), EntityType.PLAYER);

		this.maxHealth = 20;
		this.name = name;
		this.session = session;
		this.gamemode = GameMode.getByValue(session.getServer().getProperties().gamemode);
		// TODO Fix and remove msg
		this.position = !session.isCompat() && session.getClientVersion().getProtocol() > 22 ? 
				new Position(world.getSpawnPosition().x, world.getSpawnPosition().y * 2, world.getSpawnPosition().z) : world.getSpawnPosition();
		if(!session.isCompat() && session.getClientVersion().getProtocol() > 22)
			sendMessage("&cCaution: 1.8 support is currently broken and unfinished!");
		// TODO END
				
		this.inventory.addViewer(this);

		// stream the initial set of blocks and teleport us
		this.streamBlocks();

		// display player in the TAB list
		this.updateTabList();

		this.session.send(new PacketSpawnPosition(position));
		this.session.send(new PacketPlayerLookAndPosition(position.x, position.y, position.z, position.y + NORMAL_EYE_HEIGHT, (float) rotation.getYaw(), (float) rotation.getPitch(), true));

		getServer().getLogger().info(name + " [" + session.getIp() + ", prot=" + getSession().getClientVersion().getProtocol() + "] logged in with entity id " + id + " at ([" + world.getName() + "] " + (int)position.x + ", " + (int)position.y + ", " + (int)position.z + ")");
		getServer().broadcastMessage("&e" + name + " has joined the game.");
		this.sendMessage("%Rush Welcome to Rush, " + name);
	}

	/**
	 * A convenience method for sending a message to this player.
	 * @param message The message.
	 */
	public void sendMessage(String message) {
		session.send(new PacketChat(message));
	}

	public void playSound(Sound sound, Position pos) {
		playSound(sound, pos, (0.5F + 0.5F * (float)rand.nextInt(2)), ((float) (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F));
	}

	public void playSound(Sound sound, Position pos, float volume, float pitch) {
		playSound(sound, pos.x, pos.y, pos.z, volume, pitch);
	}

	public void playSound(Sound sound, double x, double y, double z, float volume, float pitch) {
		playSound(SoundEnum.getSoundName(sound), x, y, z, volume, pitch);
	}

	public void playSound(String sound, Position pos) {
		playSound(sound, pos.x, pos.y, pos.z, (0.6F + 0.6F * (float)rand.nextInt(2)), ((float) (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F));
	}

	public void playSound(String sound, double x, double y, double z, float volume, float pitch) {
		session.send(new PacketNamedSoundEffect(sound, x, y, z, volume, pitch));
	}

	public void playEffect(int effectId, int x, int y, int z, int data) {
		session.send(new PacketSoundOrParticleEffect(effectId, x, y, z, data));
	}

	public void playAnimation(AnimType type) {
		session.send(new PacketAnimation(getId(), type));
	}
	
	public void playAnimationOf(int entityId, AnimType type) {
		session.send(new PacketAnimation(entityId, type));
	}

	public void updateTabList() {
		Packet newPlayer = new PacketPlayerListItem(name, gamemode, true, (short)100);
		for(Player pl : getWorld().getPlayers()) {
			pl.getSession().send(newPlayer);
			session.send(new PacketPlayerListItem(pl.getName(), pl.getGamemode(), true, (short)100));
		}
	}

	@Override
	public void pulse() {
		super.pulse();

		streamBlocks();

		for (Iterator<Entity> it = knownEntities.iterator(); it.hasNext(); ) {
			Entity entity = it.next();

			boolean withinDistance = entity.isActive() && isWithinDistance(entity);

			if (withinDistance) {
				Packet msg = entity.createUpdateMessage();
				if (msg != null)
					session.send(msg);

				if(!announced) {
					System.out.println("Created Update Message Packet - closing debug");
					announced = true;
				}

			} else {
				session.send(new PacketDestroyEntity(entity.getId()));
				it.remove();
			}
		}

		for (Entity entity : world.getEntities()) {
			if (entity == this)
				continue;
			boolean withinDistance = entity.isActive() && isWithinDistance(entity);

			if (withinDistance && !knownEntities.contains(entity)) {
				knownEntities.add(entity);
				session.send(entity.createSpawnMessage());

				if(!announced)
					System.out.println("Created Spawn Packet");
			}
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if (ticksLived % 20 * 12 == 0)
			heal();

		if (alive) {			
			for(Entity en : world.getEntities()) {
				if(en.isActive())
					en.onCollideWithPlayer(this);
			}
		}
	}

	/**
	 * Streams chunks to the player's client.
	 */
	public void streamBlocks() {
		Set<ChunkCoords> previousChunks = new HashSet<>(knownChunks);

		int centralX = ((int) position.x) / Chunk.WIDTH;
		int centralZ = ((int) position.z) / Chunk.HEIGHT;
		
		int viewDistance = Server.getServer().getProperties().viewDistance;
		
		for (int x = (centralX - viewDistance); x <= (centralX + viewDistance); x++) {
			for (int z = (centralZ - viewDistance); z <= (centralZ + viewDistance); z++) {
				ChunkCoords key = new ChunkCoords(x, z);
				
				if (!knownChunks.contains(key)) {
					knownChunks.add(key);
					//session.send(new PreChunkPacketImpl(x, z, true));
					session.send(world.getChunks().getChunk(x, z).toMessage());
				}

				previousChunks.remove(key);
			}
		}

		for (ChunkCoords key : previousChunks) {
			//session.send(new PreChunkPacketImpl(key.x, key.z, false));
			knownChunks.remove(key);
		}

		previousChunks.clear();
	}


	@Override
	public Packet createSpawnMessage() {
		int x = position.getPixelX();
		int y = position.getPixelY();
		int z = position.getPixelZ();
		int yaw = rotation.getIntYaw();
		int pitch = rotation.getIntPitch();
		return new PacketNamedEntitySpawn(id, name, new Position(x, y, z), yaw, pitch, getItemInHand() == null ? 0 : getItemInHand().getId(), metadata.clone());
	}

	/**
	 * Sets the crouching flag.
	 * @param crouching The crouching flag.
	 */
	public void setCrouching(boolean crouching) {
		// TODO: update other clients, needs to be figured out
		this.crouching = crouching;
		setMetadata(new MetaParam<Byte>(MetaParam.TYPE_BYTE, 0, new Byte((byte) (crouching ? 0x02: 0))));
		// FIXME: other bits in the bitmask would be wiped out
	}

	public void setSprinting(boolean sprinting) {
		this.sprinting = sprinting;
		setMetadata(new MetaParam<Byte>(MetaParam.TYPE_BYTE, 0, new Byte((byte) (sprinting ? 0x08: 0))));
		// FIXME: other bits in the bitmask would be wiped out
	}

	@Override
	public void setHealth(float newHealth) {
		float oldHealth = health;
		super.setHealth(newHealth);

		session.send(new PacketUpdateHealth(newHealth, food, saturation));

		if(newHealth < oldHealth)
			playSound(Sound.HURT_FLESH, position);

		if(newHealth <= 0)
			alive = false;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public void setGamemode(GameMode gamemode) {
		this.gamemode = gamemode;
		this.getSession().send(new PacketChangeGameState(GameStateReason.CHANGE_GAMEMODE, gamemode.getValue()));
	}

	public void setRiding(boolean riding) {
		this.riding = riding;
	}

	public void addExhaustion(float exhaustion) {
		this.exhaustion+= exhaustion;
	}

	public double getEyeHeight() {
		return isCrouching() ? CROUCH_EYE_HEIGHT : NORMAL_EYE_HEIGHT;
	}

	public void throwItemFromPlayer(ItemStack theItemStack, int count) {
		if (theItemStack == null)
			return;

		ItemStack itemstack = theItemStack.clone(); 

		itemstack.count = count;

		ItemEntity item = new ItemEntity(world, itemstack, getPosition().x, getPosition().y + getEyeHeight() - .3, getPosition().z);
		item.pickupDelay = 40;

		float offsetX = 0.1F;
		float offsetZ;

		offsetX = 0.3F;
		item.motionX = -MathHelper.sin((float)getRotation().getYaw() / 180.0F * (float) Math.PI) * MathHelper.cos((float)getRotation().getPitch() / 180.0F * (float) Math.PI) * offsetX;
		item.motionY = -MathHelper.sin((float)getRotation().getPitch() / 180.0F * (float) Math.PI) * offsetX + 0.1F;
		item.motionZ = MathHelper.cos((float)getRotation().getYaw() / 180.0F * (float) Math.PI) * MathHelper.cos((float)getRotation().getPitch() / 180.0F * (float) Math.PI) * offsetX;

		offsetX = 0.02F;
		offsetZ = rand.nextFloat() * (float) Math.PI * 2.0F;
		offsetX *= rand.nextFloat();

		item.motionX += Math.cos(offsetZ) * offsetX;
		item.motionY += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
		item.motionZ += Math.sin(offsetZ) * offsetX;

		item.throwerId = id;

		world.spawnEntity(item);		
		item.metadataChanged = true;
	}

	public void openInventory(InventoryEnum type) {
		openInventory(type, 9, "", -1);
	}

	public void openInventory(InventoryEnum type, int slots, String name) {
		openInventory(type, slots, name, -1);
	}

	public void openInventory(InventoryEnum type, int slots, String name, int horseId) {
		windowId++;		
		session.send(new PacketOpenWindow(windowId, type.id, name, slots, name != "", horseId));
	}

	// Inventory

	public ItemStack getItemInHand() {
		return inventory.getItemInHand();
	}

	public void setItemInHand(ItemStack item) {
		inventory.setItemInHand(item);
	}

	public void setItemOnCursor(ItemStack item) {
		itemOnCursor = item;
		session.send(new PacketSetSlot(1, inventory.getHeldItemSlot(), item));
	}

	public void onSlotSet(Inventory inv, int index, ItemStack item) {
		int equipSlot = -1;

		if (index == getInventory().getHeldItemSlot())
			equipSlot = PacketEntityEquipment.HELD_ITEM;

		else if (index == PlayerInventory.HELMET_SLOT) 
			equipSlot = PacketEntityEquipment.HELMET_SLOT;

		else if (index == PlayerInventory.CHESTPLATE_SLOT)
			equipSlot = PacketEntityEquipment.CHESTPLATE_SLOT;

		else if (index == PlayerInventory.LEGGINGS_SLOT)
			equipSlot = PacketEntityEquipment.LEGGINGS_SLOT;

		else if (index == PlayerInventory.BOOTS_SLOT)
			equipSlot = PacketEntityEquipment.BOOTS_SLOT;

		if (equipSlot >= 0) {
			PacketEntityEquipment message = new PacketEntityEquipment(getId(), equipSlot, item);
			for (Player player : getWorld().getPlayers())
				if (player != this && player.isWithinDistance(this))
					player.getSession().send(message);
		}

		session.send(new PacketSetSlot(inventory.getId(), index, item));
	}

	public Server getServer() {
		return session.getServer();
	}

	public void heal() {
		if(health < maxHealth) {
			setHealth(health + 1);
			getSession().send(new PacketUpdateHealth(health, (short)food, saturation));
		}
	}
	
	public void setTitle(TitleAction action, String text) {
		if(!session.isCompat() && session.getClientVersion().getProtocol() > 46) {
			session.send(new Packet18Title(action, text));
		} else
			throw new IllegalStateException("Title can only be displayed on 1.8 client, but " + getName() + " is running " + session.getClientVersion().getVersion());
	}
	
	public void setTitle(TitleAction action, int fadeIn, int stay, int fadeOut) {
		if(!session.isCompat() && session.getClientVersion().getProtocol() > 46) {
			session.send(new Packet18Title(action, fadeIn, stay, fadeOut));
		} else
			throw new IllegalStateException("Title can only be displayed on 1.8 client, but " + getName() + " is running " + session.getClientVersion().getVersion());
	}
}

