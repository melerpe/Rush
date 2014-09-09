package net.rush.protocol;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.HashMap;

import javax.management.RuntimeErrorException;

import lombok.Setter;
import net.rush.model.Player;
import net.rush.protocol.packets.AnimationPacket;
import net.rush.protocol.packets.AttachEntityPacket;
import net.rush.protocol.packets.BlockActionPacket;
import net.rush.protocol.packets.BlockBreakAnimationPacket;
import net.rush.protocol.packets.BlockChangePacket;
import net.rush.protocol.packets.ChangeGameStatePacket;
import net.rush.protocol.packets.ChatPacket;
import net.rush.protocol.packets.ClickWindowPacket;
import net.rush.protocol.packets.ClientSettingsPacket;
import net.rush.protocol.packets.ClientStatusPacket;
import net.rush.protocol.packets.CloseWindowPacket;
import net.rush.protocol.packets.ConfirmTransactionPacket;
import net.rush.protocol.packets.CreativeInventoryActionPacket;
import net.rush.protocol.packets.DestroyEntityPacket;
import net.rush.protocol.packets.EnchantItemPacket;
import net.rush.protocol.packets.EncryptionKeyRequestPacket;
import net.rush.protocol.packets.EncryptionKeyResponsePacket;
import net.rush.protocol.packets.EntityActionPacket;
import net.rush.protocol.packets.EntityEffectPacket;
import net.rush.protocol.packets.EntityEquipmentPacket;
import net.rush.protocol.packets.EntityExistsPacket;
import net.rush.protocol.packets.EntityHeadLookPacket;
import net.rush.protocol.packets.EntityLookAndRelMovePacket;
import net.rush.protocol.packets.EntityLookPacket;
import net.rush.protocol.packets.EntityMetadataPacket;
import net.rush.protocol.packets.EntityRelMovePacket;
import net.rush.protocol.packets.EntityStatusPacket;
import net.rush.protocol.packets.EntityTeleportPacket;
import net.rush.protocol.packets.EntityVelocityPacket;
import net.rush.protocol.packets.ExplosionPacket;
import net.rush.protocol.packets.HandshakePacket;
import net.rush.protocol.packets.HeldItemChangePacket;
import net.rush.protocol.packets.IncrementStatisticPacket;
import net.rush.protocol.packets.ItemCollectPacket;
import net.rush.protocol.packets.KeepAlivePacket;
import net.rush.protocol.packets.KickPacket;
import net.rush.protocol.packets.LoginPacket;
import net.rush.protocol.packets.MapChunkPacket;
import net.rush.protocol.packets.MapDataPacket;
import net.rush.protocol.packets.MultiBlockChangePacket;
import net.rush.protocol.packets.NamedEntitySpawnPacket;
import net.rush.protocol.packets.NamedSoundEffectPacket;
import net.rush.protocol.packets.OpenWindowPacket;
import net.rush.protocol.packets.PlayerAbilitiesPacket;
import net.rush.protocol.packets.PlayerBlockPlacementPacket;
import net.rush.protocol.packets.PlayerDiggingPacket;
import net.rush.protocol.packets.PlayerListItemPacket;
import net.rush.protocol.packets.PlayerLookPacket;
import net.rush.protocol.packets.PlayerOnGroundPacket;
import net.rush.protocol.packets.PlayerPositionAndLookPacket;
import net.rush.protocol.packets.PlayerPositionPacket;
import net.rush.protocol.packets.PlayerRespawnPacket;
import net.rush.protocol.packets.PluginMessagePacket;
import net.rush.protocol.packets.PreChunkPacket;
import net.rush.protocol.packets.RemoveEntityEffectPacket;
import net.rush.protocol.packets.ServerListPingPacket;
import net.rush.protocol.packets.SetExperiencePacket;
import net.rush.protocol.packets.SetSlotPacket;
import net.rush.protocol.packets.SetWindowItemsPacket;
import net.rush.protocol.packets.SoundOrParticleEffectPacket;
import net.rush.protocol.packets.SpawnExperienceOrbPacket;
import net.rush.protocol.packets.SpawnMobPacket;
import net.rush.protocol.packets.SpawnObjectPacket;
import net.rush.protocol.packets.SpawnPaintingPacket;
import net.rush.protocol.packets.SpawnPositionPacket;
import net.rush.protocol.packets.SteerVehiclePacket;
import net.rush.protocol.packets.TabCompletePacket;
import net.rush.protocol.packets.ThunderboltPacket;
import net.rush.protocol.packets.TimeUpdatePacket;
import net.rush.protocol.packets.UpdateHealthPacket;
import net.rush.protocol.packets.UpdateSignPacket;
import net.rush.protocol.packets.UpdateTileEntityPacket;
import net.rush.protocol.packets.UpdateWindowPropertyPacket;
import net.rush.protocol.packets.UseBedPacket;
import net.rush.protocol.packets.UseEntityPacket;
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
		//Preconditions.checkArgument(byId[id] == null, "Cannot register already registered packet id %s", id);
		//Preconditions.checkArgument(!byClass.containsKey(packetClass), "Cannot register already registered packet class %s", packetClass);

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
			throw new RuntimeException("Could not construct packet ID " + id, ex);
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
		if (protocol < 16) {
			writePositionYShort(x, y, z, out);
		} else {
			writePosition18(out, x, y, z);
		}
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
	  PacketPlayOutPlayerInfo
	  PlayerListItem - rewrite
	 */

	static {
		register(0x00, KeepAlivePacket.class);
		register(0x01, LoginPacket.class);
		register(0x02, HandshakePacket.class);
		register(0x03, ChatPacket.class);
		register(0x04, TimeUpdatePacket.class);
		register(0x05, EntityEquipmentPacket.class);
		register(0x06, SpawnPositionPacket.class);
		register(0x07, UseEntityPacket.class);
		register(0x08, UpdateHealthPacket.class);
		register(0x09, PlayerRespawnPacket.class);
		register(0x0A, PlayerOnGroundPacket.class);
		register(0x0B, PlayerPositionPacket.class);
		register(0x0C, PlayerLookPacket.class);
		register(0x0D, PlayerPositionAndLookPacket.class);
		register(0x0E, PlayerDiggingPacket.class);
		register(0x0F, PlayerBlockPlacementPacket.class);
		register(0x10, HeldItemChangePacket.class);
		register(0x11, UseBedPacket.class);
		register(0x12, AnimationPacket.class);
		register(0x13, EntityActionPacket.class);
		register(0x14, NamedEntitySpawnPacket.class);
		register(0x16, ItemCollectPacket.class);
		register(0x17, SpawnObjectPacket.class);
		register(0x18, SpawnMobPacket.class);
		register(0x19, SpawnPaintingPacket.class);
		register(0x1A, SpawnExperienceOrbPacket.class);
		register(0x1C, EntityVelocityPacket.class);
		register(0x1D, DestroyEntityPacket.class);
		register(0x1E, EntityExistsPacket.class);
		register(0x1F, EntityRelMovePacket.class);
		register(0x20, EntityLookPacket.class);
		register(0x21, EntityLookAndRelMovePacket.class);
		register(0x22, EntityTeleportPacket.class);
		register(0x23, EntityHeadLookPacket.class);
		register(0x26, EntityStatusPacket.class);
		register(0x27, AttachEntityPacket.class);
		register(0x28, EntityMetadataPacket.class);
		register(0x29, EntityEffectPacket.class);
		register(0x2A, RemoveEntityEffectPacket.class);
		register(0x2B, SetExperiencePacket.class);
		register(0x38, PreChunkPacket.class);
		register(0x33, MapChunkPacket.class);
		register(0x34, MultiBlockChangePacket.class);
		register(0x35, BlockChangePacket.class);
		register(0x36, BlockActionPacket.class);
		register(0x3C, ExplosionPacket.class);
		register(0x3D, SoundOrParticleEffectPacket.class);
		register(0x46, ChangeGameStatePacket.class);
		register(0x47, ThunderboltPacket.class);
		register(0x64, OpenWindowPacket.class);
		register(0x65, CloseWindowPacket.class);
		register(0x66, ClickWindowPacket.class);
		register(0x67, SetSlotPacket.class);
		register(0x68, SetWindowItemsPacket.class);
		register(0x69, UpdateWindowPropertyPacket.class);
		register(0x6A, ConfirmTransactionPacket.class);
		register(0x6B, CreativeInventoryActionPacket.class);
		register(0x6C, EnchantItemPacket.class);
		register(0x82, UpdateSignPacket.class);
		register(0x83, MapDataPacket.class);
		register(0x84, UpdateTileEntityPacket.class);
		register(0xC8, IncrementStatisticPacket.class);
		register(0xC9, PlayerListItemPacket.class);
		register(0xCA, PlayerAbilitiesPacket.class);
		register(0xFA, PluginMessagePacket.class);
		register(0xFE, ServerListPingPacket.class);
		register(0xFF, KickPacket.class);
		// 1.3.2
		register(0xFC, EncryptionKeyResponsePacket.class);
		register(0xFD, EncryptionKeyRequestPacket.class);
		register(0xCD, ClientStatusPacket.class);
		register(0x37, BlockBreakAnimationPacket.class);
		register(0x14, NamedEntitySpawnPacket.class);
		register(0xCB, TabCompletePacket.class);
		register(0xCC, ClientSettingsPacket.class);
		register(0x3E, NamedSoundEffectPacket.class);
		// 1.5.1
		register(0x1B, SteerVehiclePacket.class);
		// TODO EntityProperties, Scoreboard stuff
	}

	/** Debug purposes */
	public String getNameAndId() {
		return " Packet (name=" + getClass().getSimpleName() + ", id=" + getId() + ")";
	}
}
