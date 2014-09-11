package net.rush.protocol;

import java.util.HashMap;

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
import net.rush.protocol.packets.Packet17LoginRequest;
import net.rush.protocol.packets.Packet17LoginSuccess;
import net.rush.protocol.packets.Packet17StatusRequest;
import net.rush.protocol.packets.Packet18LoginCompression;
import net.rush.protocol.packets.Packet18Title;
import net.rush.protocol.packets.PingTime;
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

public enum ProtocolNew {

	// Undef
	HANDSHAKE {
		{
			TO_SERVER.registerPacket(0x00, HandshakePacket.class);
		}
	},
	// 0
	GAME {
		{
			TO_CLIENT.registerPacket(0x00, KeepAlivePacket.class);
			TO_CLIENT.registerPacket(0x01, LoginPacket.class);
			TO_CLIENT.registerPacket(0x02, ChatPacket.class);
			TO_CLIENT.registerPacket(0x03, TimeUpdatePacket.class);
			TO_CLIENT.registerPacket(0x04, EntityEquipmentPacket.class);
			TO_CLIENT.registerPacket(0x05, SpawnPositionPacket.class);
			TO_CLIENT.registerPacket(0x06, UpdateHealthPacket.class);
			TO_CLIENT.registerPacket(0x07, PlayerRespawnPacket.class);
			TO_CLIENT.registerPacket(0x08, PlayerPositionAndLookPacket.class);
			TO_CLIENT.registerPacket(0x09, HeldItemChangePacket.class);
			TO_CLIENT.registerPacket(0x0A, UseBedPacket.class); //Packet17EntityLocationAction
			TO_CLIENT.registerPacket(0x0B, AnimationPacket.class);
			TO_CLIENT.registerPacket(0x0C, NamedEntitySpawnPacket.class);
			TO_CLIENT.registerPacket(0x0D, ItemCollectPacket.class);
			TO_CLIENT.registerPacket(0x0E, SpawnObjectPacket.class);
			TO_CLIENT.registerPacket(0x0F, SpawnMobPacket.class);
			TO_CLIENT.registerPacket(0x10, SpawnPaintingPacket.class);
			TO_CLIENT.registerPacket(0x11, SpawnExperienceOrbPacket.class);
			TO_CLIENT.registerPacket(0x12, EntityVelocityPacket.class);
			TO_CLIENT.registerPacket(0x13, DestroyEntityPacket.class);
			TO_CLIENT.registerPacket(0x14, EntityExistsPacket.class);
			TO_CLIENT.registerPacket(0x15, EntityRelMovePacket.class);
			TO_CLIENT.registerPacket(0x16, EntityLookPacket.class);
			TO_CLIENT.registerPacket(0x17, EntityLookAndRelMovePacket.class);
			TO_CLIENT.registerPacket(0x18, EntityTeleportPacket.class);
			TO_CLIENT.registerPacket(0x19, EntityHeadLookPacket.class);
			TO_CLIENT.registerPacket(0x1A, EntityStatusPacket.class);
			TO_CLIENT.registerPacket(0x1B, AttachEntityPacket.class);
			TO_CLIENT.registerPacket(0x1C, EntityMetadataPacket.class);
			TO_CLIENT.registerPacket(0x1D, EntityEffectPacket.class);
			TO_CLIENT.registerPacket(0x1E, RemoveEntityEffectPacket.class);
			TO_CLIENT.registerPacket(0x1F, SetExperiencePacket.class);
			//TO_CLIENT.registerPacket(0x20, Packet44UpdateAttributes.class);
			TO_CLIENT.registerPacket(0x21, MapChunkPacket.class);
			TO_CLIENT.registerPacket(0x22, MultiBlockChangePacket.class);
			TO_CLIENT.registerPacket(0x23, BlockChangePacket.class);
			TO_CLIENT.registerPacket(0x24, BlockActionPacket.class);
			TO_CLIENT.registerPacket(0x25, BlockBreakAnimationPacket.class);
			TO_CLIENT.registerPacket(0x26, PreChunkPacket.class);
			TO_CLIENT.registerPacket(0x27, ExplosionPacket.class);
			TO_CLIENT.registerPacket(0x28, SoundOrParticleEffectPacket.class);
			TO_CLIENT.registerPacket(0x29, NamedSoundEffectPacket.class);
			//TO_CLIENT.registerPacket(0x2A, Packet63WorldParticles.class);
			TO_CLIENT.registerPacket(0x2B, ChangeGameStatePacket.class);
			TO_CLIENT.registerPacket(0x2C, ThunderboltPacket.class);
			TO_CLIENT.registerPacket(0x2D, OpenWindowPacket.class);
			TO_CLIENT.registerPacket(0x2E, CloseWindowPacket.class);
			TO_CLIENT.registerPacket(0x2F, SetSlotPacket.class);
			TO_CLIENT.registerPacket(0x30, SetWindowItemsPacket.class);
			TO_CLIENT.registerPacket(0x31, UpdateWindowPropertyPacket.class);
			TO_CLIENT.registerPacket(0x32, ConfirmTransactionPacket.class);
			TO_CLIENT.registerPacket(0x33, UpdateSignPacket.class);
			TO_CLIENT.registerPacket(0x34, MapDataPacket.class);
			TO_CLIENT.registerPacket(0x35, UpdateTileEntityPacket.class);
			//TO_CLIENT.registerPacket(0x36, Packet133OpenTileEntity.class);
			//TO_CLIENT.registerPacket(0x37, Packet200Statistic.class);
			TO_CLIENT.registerPacket(0x38, PlayerListItemPacket.class);
			TO_CLIENT.registerPacket(0x39, PlayerAbilitiesPacket.class);
			TO_CLIENT.registerPacket(0x3A, TabCompletePacket.class);
			//TO_CLIENT.registerPacket(0x3B, Packet206SetScoreboardObjective.class);
			//TO_CLIENT.registerPacket(0x3C, Packet207SetScoreboardScore.class);
			//TO_CLIENT.registerPacket(0x3D, Packet208SetScoreboardDisplayObjective.class);
			//TO_CLIENT.registerPacket(0x3E, Packet209SetScoreboardTeam.class);
			TO_CLIENT.registerPacket(0x3F, PluginMessagePacket.class);
			TO_CLIENT.registerPacket(0x40, KickPacket.class);
			
			// 1.8
			TO_CLIENT.registerPacket(0x45, Packet18Title.class);

			TO_SERVER.registerPacket(0x00, KeepAlivePacket.class);
			TO_SERVER.registerPacket(0x01, ChatPacket.class);
			TO_SERVER.registerPacket(0x02, UseEntityPacket.class);
			TO_SERVER.registerPacket(0x03, PlayerOnGroundPacket.class);
			TO_SERVER.registerPacket(0x04, PlayerPositionPacket.class);
			TO_SERVER.registerPacket(0x05, PlayerLookPacket.class);
			TO_SERVER.registerPacket(0x06, PlayerPositionAndLookPacket.class);
			TO_SERVER.registerPacket(0x07, PlayerDiggingPacket.class);
			TO_SERVER.registerPacket(0x08, PlayerBlockPlacementPacket.class);
			TO_SERVER.registerPacket(0x09, HeldItemChangePacket.class);
			TO_SERVER.registerPacket(0x0A, AnimationPacket.class);
			TO_SERVER.registerPacket(0x0B, EntityActionPacket.class);
			TO_SERVER.registerPacket(0x0C, SteerVehiclePacket.class);
			TO_SERVER.registerPacket(0x0D, CloseWindowPacket.class);
			TO_SERVER.registerPacket(0x0E, ClickWindowPacket.class);
			TO_SERVER.registerPacket(0x0F, ConfirmTransactionPacket.class);
			TO_SERVER.registerPacket(0x10, CreativeInventoryActionPacket.class);
			TO_SERVER.registerPacket(0x11, EnchantItemPacket.class);
			TO_SERVER.registerPacket(0x12, UpdateSignPacket.class);
			TO_SERVER.registerPacket(0x13, PlayerAbilitiesPacket.class);
			TO_SERVER.registerPacket(0x14, TabCompletePacket.class);
			TO_SERVER.registerPacket(0x15, ClientSettingsPacket.class);
			TO_SERVER.registerPacket(0x16, ClientStatusPacket.class);
			TO_SERVER.registerPacket(0x17, PluginMessagePacket.class);
			
		}
	},
	// 1
	STATUS {
		{
			TO_CLIENT.registerPacket(0x00, KickPacket.class);
			TO_CLIENT.registerPacket(0x01, PingTime.class);

			TO_SERVER.registerPacket(0x00, Packet17StatusRequest.class);
			TO_SERVER.registerPacket(0x01, PingTime.class);
		}
	},
	//2
	LOGIN {
		{
			TO_CLIENT.registerPacket(0x00, KickPacket.class);
			TO_CLIENT.registerPacket(0x01, EncryptionKeyRequestPacket.class);
			TO_CLIENT.registerPacket(0x02, Packet17LoginSuccess.class);
			
			// 1.8
			TO_CLIENT.registerPacket(0x3, Packet18LoginCompression.class);

			TO_SERVER.registerPacket(0x00, Packet17LoginRequest.class);
			TO_SERVER.registerPacket(0x01, EncryptionKeyResponsePacket.class);
		}
	};

	public final int MAX_PACKET_ID = 0xFF;
	public final ProtocolDirection TO_SERVER = new ProtocolDirection("TO_SERVER");
	public final ProtocolDirection TO_CLIENT = new ProtocolDirection("TO_CLIENT");

	@SuppressWarnings("unchecked")
	public class ProtocolDirection {

		public ProtocolDirection(String name) {
			this.name = name;
		}

		private final String name;
		private final HashMap<Class<? extends Packet>, Integer> packetMap = new HashMap<>();
		private final Class<? extends Packet>[] packetClasses = new Class[MAX_PACKET_ID];

		public boolean hasPacket(int id) {
			return id < MAX_PACKET_ID && packetClasses[id] != null;
		}

		@Override
		public String toString() {
			return name;
		}

		public Packet createPacket(int id) {
			if (id > MAX_PACKET_ID) 
				throw new RuntimeException("Packet ID " + id + " outside of range ");	

			try {
				return packetClasses[id].newInstance();
			} catch (Exception ex) {
				throw new RuntimeException("Could not construct packet ID " + id, ex);
			}
		}

		protected void registerPacket(int id, Class<? extends Packet> packetClass) {
			packetClasses[id] = packetClass;
			packetMap.put(packetClass, id);
		}

		protected void unregisterPacket(int id) {
			packetMap.remove(packetClasses[id]);
			packetClasses[id] = null;
		}

		public int getId(Class<? extends Packet> packet) {
			int id = -1;
			id = packetMap.get(packet);
			if(id == -1)
				throw new RuntimeException("Cannot get ID for packet" + packet);
			
			return id;
		}
	}
}
