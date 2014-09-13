package net.rush.protocol;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.HashMap;

import lombok.Setter;
import net.rush.model.Player;
import net.rush.protocol.packets.PacketAnimation;
import net.rush.protocol.packets.PacketAttachEntity;
import net.rush.protocol.packets.PacketBlockAction;
import net.rush.protocol.packets.PacketBlockBreakAnim;
import net.rush.protocol.packets.PacketBlockChange;
import net.rush.protocol.packets.PacketBlockPlacement;
import net.rush.protocol.packets.PacketChangeGameState;
import net.rush.protocol.packets.PacketChat;
import net.rush.protocol.packets.PacketChunkBulk;
import net.rush.protocol.packets.PacketClickWindow;
import net.rush.protocol.packets.PacketClientSettings;
import net.rush.protocol.packets.PacketClientStatus;
import net.rush.protocol.packets.PacketCloseWindow;
import net.rush.protocol.packets.PacketConfirmTransaction;
import net.rush.protocol.packets.PacketCreativeInventoryAction;
import net.rush.protocol.packets.PacketDestroyEntity;
import net.rush.protocol.packets.PacketDigging;
import net.rush.protocol.packets.PacketEnchantItem;
import net.rush.protocol.packets.PacketEncryptionRequest;
import net.rush.protocol.packets.PacketEncryptionResponse;
import net.rush.protocol.packets.PacketEntityAction;
import net.rush.protocol.packets.PacketEntityEffect;
import net.rush.protocol.packets.PacketEntityEquipment;
import net.rush.protocol.packets.PacketEntityExists;
import net.rush.protocol.packets.PacketEntityHeadLook;
import net.rush.protocol.packets.PacketEntityLook;
import net.rush.protocol.packets.PacketEntityLookRelMove;
import net.rush.protocol.packets.PacketEntityMetadata;
import net.rush.protocol.packets.PacketEntityRelMove;
import net.rush.protocol.packets.PacketEntityStatus;
import net.rush.protocol.packets.PacketEntityTeleport;
import net.rush.protocol.packets.PacketEntityVelocity;
import net.rush.protocol.packets.PacketExplosion;
import net.rush.protocol.packets.PacketHandshake;
import net.rush.protocol.packets.PacketHeldItemChange;
import net.rush.protocol.packets.PacketIncrementStatistic;
import net.rush.protocol.packets.PacketItemCollect;
import net.rush.protocol.packets.PacketKeepAlive;
import net.rush.protocol.packets.PacketKick;
import net.rush.protocol.packets.PacketLogin;
import net.rush.protocol.packets.PacketMap;
import net.rush.protocol.packets.PacketMapChunk;
import net.rush.protocol.packets.PacketMultiBlockChange;
import net.rush.protocol.packets.PacketNamedEntitySpawn;
import net.rush.protocol.packets.PacketNamedSoundEffect;
import net.rush.protocol.packets.PacketOpenWindow;
import net.rush.protocol.packets.PacketPlayerAbilities;
import net.rush.protocol.packets.PacketPlayerListItem;
import net.rush.protocol.packets.PacketPlayerLook;
import net.rush.protocol.packets.PacketPlayerLookPosition;
import net.rush.protocol.packets.PacketPlayerOnGround;
import net.rush.protocol.packets.PacketPlayerPosition;
import net.rush.protocol.packets.PacketPluginMessage;
import net.rush.protocol.packets.PacketRemoveEntityEffect;
import net.rush.protocol.packets.PacketRespawn;
import net.rush.protocol.packets.PacketServerListPing;
import net.rush.protocol.packets.PacketSetExperience;
import net.rush.protocol.packets.PacketSetSlot;
import net.rush.protocol.packets.PacketSetWindowItems;
import net.rush.protocol.packets.PacketSoundOrParticleEffect;
import net.rush.protocol.packets.PacketSpawnExpOrb;
import net.rush.protocol.packets.PacketSpawnMob;
import net.rush.protocol.packets.PacketSpawnObject;
import net.rush.protocol.packets.PacketSpawnPainting;
import net.rush.protocol.packets.PacketSpawnPosition;
import net.rush.protocol.packets.PacketSteerVehicle;
import net.rush.protocol.packets.PacketTabComplete;
import net.rush.protocol.packets.PacketThunderbolt;
import net.rush.protocol.packets.PacketTimeUpdate;
import net.rush.protocol.packets.PacketUpdateHealth;
import net.rush.protocol.packets.PacketUpdateSign;
import net.rush.protocol.packets.PacketUpdateTileEntity;
import net.rush.protocol.packets.PacketUpdateWindowProperty;
import net.rush.protocol.packets.PacketUseBed;
import net.rush.protocol.packets.PacketUseEntity;
import net.rush.protocol.utils.PacketUtils;

public abstract class Packet extends PacketUtils {

	@Setter
	protected int protocol;

	@Setter
	// Indicated whenever the client is older than 1.7x
	protected boolean compat = false;

	@SuppressWarnings("unchecked")
	private static Class<? extends Packet>[] packetClasses = new Class[256];
	private static HashMap<String, Integer> packetMap = new HashMap<>();

	private PacketHandler handler = new PacketHandler();

	private static void register(int id, Class<? extends Packet> packetClass) {
		if(packetMap.containsKey(packetClass))
			throw new RuntimeException("Cannot register already registered packet class " + packetClass);

		packetClasses[id] = packetClass;
		packetMap.put(packetClass.getSimpleName(), id);
	}

	public int getId() {
		try {
			return packetMap.get(getClass().getSimpleName());
		} catch (NullPointerException ex) {
			throw new RuntimeException("Legacy packet map does not contain ID for packet " + this);
		}
	}

	public static Packet createPacket(int id) {
		if (id > 256) 
			throw new RuntimeException("Packet ID " + id + " outside of range ");

		try {
			return packetClasses[id].newInstance();
		} catch (Exception ex) {
			throw new IllegalStateException(String.format("Couldn't construct legacy packet ID %1$d (0x%1$X)", id), ex);
		}
	}

	public void read(ByteBuf in) throws IOException {
		throw new UnsupportedOperationException("Missing read method for " + this);
	}

	public void write(ByteBuf out) throws IOException {
		throw new UnsupportedOperationException("Missing write method for " + this);
	}

	public void readCompat(ByteBuf in) throws IOException {
		read(in);
	}

	public void writeCompat(ByteBuf out) throws IOException {
		write(out);
	}

	public <T extends Packet> void handle(Session session, Player player, T packet) {
		handler.handle(session, player, packet);
		//throw new UnsupportedOperationException("Unknown handling of " + this);
	}

	// Compat

	protected boolean compat_readByteBoolean(ByteBuf in) {
		return compat ? in.readUnsignedByte() == 1 ? true : false : in.readBoolean();
	}

	protected void compat_writeByteBoolean(boolean value, ByteBuf out) {
		if(compat)
			out.writeByte(value ? 1 : 0);
		else
			out.writeBoolean(value);
	}

	protected void compat_writePosition18(int x, int y, int z, ByteBuf out) {
		if (protocol < 16)
			writePositionYShort(x, y, z, out);
		else 
			writePosition18(out, x, y, z);
	}

	/* TODO (low priority)
	    Explosion packet
		IncrementStatisticPacket
		MapDataPacket - check 1.7
		SpawnExperienceOrbPacket - check fixed-point number (*32)
		ThunderBoltPacket - check fixed-point number
	 */

	/*
	 * TODO 1.8 HIGH PRIORITY
	  all deprecated classes needs review/fix - help greatly appreciated! :)
	  PacketPlayOutMultiBlockChangen
	  PacketPlayOutOpenWindow
	 */

	static {
		register(0x00, PacketKeepAlive.class);
		register(0x01, PacketLogin.class);
		register(0x02, PacketHandshake.class);
		register(0x03, PacketChat.class);
		register(0x04, PacketTimeUpdate.class);
		register(0x05, PacketEntityEquipment.class);
		register(0x06, PacketSpawnPosition.class);
		register(0x07, PacketUseEntity.class);
		register(0x08, PacketUpdateHealth.class);
		register(0x09, PacketRespawn.class);
		register(0x0A, PacketPlayerOnGround.class);
		register(0x0B, PacketPlayerPosition.class);
		register(0x0C, PacketPlayerLook.class);
		register(0x0D, PacketPlayerLookPosition.class);
		register(0x0E, PacketDigging.class);
		register(0x0F, PacketBlockPlacement.class);
		register(0x10, PacketHeldItemChange.class);
		register(0x11, PacketUseBed.class);
		register(0x12, PacketAnimation.class);
		register(0x13, PacketEntityAction.class);
		register(0x14, PacketNamedEntitySpawn.class);
		register(0x16, PacketItemCollect.class);
		register(0x17, PacketSpawnObject.class);
		register(0x18, PacketSpawnMob.class);
		register(0x19, PacketSpawnPainting.class);
		register(0x1A, PacketSpawnExpOrb.class);
		register(0x1C, PacketEntityVelocity.class);
		register(0x1D, PacketDestroyEntity.class);
		register(0x1E, PacketEntityExists.class);
		register(0x1F, PacketEntityRelMove.class);
		register(0x20, PacketEntityLook.class);
		register(0x21, PacketEntityLookRelMove.class);
		register(0x22, PacketEntityTeleport.class);
		register(0x23, PacketEntityHeadLook.class);
		register(0x26, PacketEntityStatus.class);
		register(0x27, PacketAttachEntity.class);
		register(0x28, PacketEntityMetadata.class);
		register(0x29, PacketEntityEffect.class);
		register(0x2A, PacketRemoveEntityEffect.class);
		register(0x2B, PacketSetExperience.class);
		register(0x38, PacketChunkBulk.class);
		register(0x33, PacketMapChunk.class);
		register(0x34, PacketMultiBlockChange.class);
		register(0x35, PacketBlockChange.class);
		register(0x36, PacketBlockAction.class);
		register(0x3C, PacketExplosion.class);
		register(0x3D, PacketSoundOrParticleEffect.class);
		register(0x46, PacketChangeGameState.class);
		register(0x47, PacketThunderbolt.class);
		register(0x64, PacketOpenWindow.class);
		register(0x65, PacketCloseWindow.class);
		register(0x66, PacketClickWindow.class);
		register(0x67, PacketSetSlot.class);
		register(0x68, PacketSetWindowItems.class);
		register(0x69, PacketUpdateWindowProperty.class);
		register(0x6A, PacketConfirmTransaction.class);
		register(0x6B, PacketCreativeInventoryAction.class);
		register(0x6C, PacketEnchantItem.class);
		register(0x82, PacketUpdateSign.class);
		register(0x83, PacketMap.class);
		register(0x84, PacketUpdateTileEntity.class);
		register(0xC8, PacketIncrementStatistic.class);
		register(0xC9, PacketPlayerListItem.class);
		register(0xCA, PacketPlayerAbilities.class);
		register(0xFA, PacketPluginMessage.class);
		register(0xFE, PacketServerListPing.class);
		register(0xFF, PacketKick.class);
		// 1.3.2
		register(0xFC, PacketEncryptionResponse.class);
		register(0xFD, PacketEncryptionRequest.class);
		register(0xCD, PacketClientStatus.class);
		register(0x37, PacketBlockBreakAnim.class);
		register(0x14, PacketNamedEntitySpawn.class);
		register(0xCB, PacketTabComplete.class);
		register(0xCC, PacketClientSettings.class);
		register(0x3E, PacketNamedSoundEffect.class);
		// 1.5.1
		register(0x1B, PacketSteerVehicle.class);
		// TODO EntityProperties, Scoreboard stuff
	}
}
