package net.rush.protocol;

import java.util.HashMap;

import net.rush.protocol.packets.Packet17LoginRequest;
import net.rush.protocol.packets.Packet17LoginSuccess;
import net.rush.protocol.packets.Packet17PingTime;
import net.rush.protocol.packets.Packet17StatusRequest;
import net.rush.protocol.packets.Packet18LoginCompression;
import net.rush.protocol.packets.Packet18Title;
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
import net.rush.protocol.packets.PacketPlayerLookAndPosition;
import net.rush.protocol.packets.PacketPlayerOnGround;
import net.rush.protocol.packets.PacketPlayerPosition;
import net.rush.protocol.packets.PacketPluginMessage;
import net.rush.protocol.packets.PacketRemoveEntityEffect;
import net.rush.protocol.packets.PacketRespawn;
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

public enum ProtocolNew {

	// Undef
	HANDSHAKE {
		{
			TO_SERVER.registerPacket(0x00, PacketHandshake.class);
		}
	},
	// 0
	GAME {
		{
			TO_CLIENT.registerPacket(0x00, PacketKeepAlive.class);
			TO_CLIENT.registerPacket(0x01, PacketLogin.class);
			TO_CLIENT.registerPacket(0x02, PacketChat.class);
			TO_CLIENT.registerPacket(0x03, PacketTimeUpdate.class);
			TO_CLIENT.registerPacket(0x04, PacketEntityEquipment.class);
			TO_CLIENT.registerPacket(0x05, PacketSpawnPosition.class);
			TO_CLIENT.registerPacket(0x06, PacketUpdateHealth.class);
			TO_CLIENT.registerPacket(0x07, PacketRespawn.class);
			TO_CLIENT.registerPacket(0x08, PacketPlayerLookAndPosition.class);
			TO_CLIENT.registerPacket(0x09, PacketHeldItemChange.class);
			TO_CLIENT.registerPacket(0x0A, PacketUseBed.class); //Packet17EntityLocationAction
			TO_CLIENT.registerPacket(0x0B, PacketAnimation.class);
			TO_CLIENT.registerPacket(0x0C, PacketNamedEntitySpawn.class);
			TO_CLIENT.registerPacket(0x0D, PacketItemCollect.class);
			TO_CLIENT.registerPacket(0x0E, PacketSpawnObject.class);
			TO_CLIENT.registerPacket(0x0F, PacketSpawnMob.class);
			TO_CLIENT.registerPacket(0x10, PacketSpawnPainting.class);
			TO_CLIENT.registerPacket(0x11, PacketSpawnExpOrb.class);
			TO_CLIENT.registerPacket(0x12, PacketEntityVelocity.class);
			TO_CLIENT.registerPacket(0x13, PacketDestroyEntity.class);
			TO_CLIENT.registerPacket(0x14, PacketEntityExists.class);
			TO_CLIENT.registerPacket(0x15, PacketEntityRelMove.class);
			TO_CLIENT.registerPacket(0x16, PacketEntityLook.class);
			TO_CLIENT.registerPacket(0x17, PacketEntityLookRelMove.class);
			TO_CLIENT.registerPacket(0x18, PacketEntityTeleport.class);
			TO_CLIENT.registerPacket(0x19, PacketEntityHeadLook.class);
			TO_CLIENT.registerPacket(0x1A, PacketEntityStatus.class);
			TO_CLIENT.registerPacket(0x1B, PacketAttachEntity.class);
			TO_CLIENT.registerPacket(0x1C, PacketEntityMetadata.class);
			TO_CLIENT.registerPacket(0x1D, PacketEntityEffect.class);
			TO_CLIENT.registerPacket(0x1E, PacketRemoveEntityEffect.class);
			TO_CLIENT.registerPacket(0x1F, PacketSetExperience.class);
			//TO_CLIENT.registerPacket(0x20, Packet44UpdateAttributes.class);
			TO_CLIENT.registerPacket(0x21, PacketMapChunk.class);
			TO_CLIENT.registerPacket(0x22, PacketMultiBlockChange.class);
			TO_CLIENT.registerPacket(0x23, PacketBlockChange.class);
			TO_CLIENT.registerPacket(0x24, PacketBlockAction.class);
			TO_CLIENT.registerPacket(0x25, PacketBlockBreakAnim.class);
			TO_CLIENT.registerPacket(0x26, PacketChunkBulk.class);
			TO_CLIENT.registerPacket(0x27, PacketExplosion.class);
			TO_CLIENT.registerPacket(0x28, PacketSoundOrParticleEffect.class);
			TO_CLIENT.registerPacket(0x29, PacketNamedSoundEffect.class);
			//TO_CLIENT.registerPacket(0x2A, Packet63WorldParticles.class);
			TO_CLIENT.registerPacket(0x2B, PacketChangeGameState.class);
			TO_CLIENT.registerPacket(0x2C, PacketThunderbolt.class);
			TO_CLIENT.registerPacket(0x2D, PacketOpenWindow.class);
			TO_CLIENT.registerPacket(0x2E, PacketCloseWindow.class);
			TO_CLIENT.registerPacket(0x2F, PacketSetSlot.class);
			TO_CLIENT.registerPacket(0x30, PacketSetWindowItems.class);
			TO_CLIENT.registerPacket(0x31, PacketUpdateWindowProperty.class);
			TO_CLIENT.registerPacket(0x32, PacketConfirmTransaction.class);
			TO_CLIENT.registerPacket(0x33, PacketUpdateSign.class);
			TO_CLIENT.registerPacket(0x34, PacketMap.class);
			TO_CLIENT.registerPacket(0x35, PacketUpdateTileEntity.class);
			//TO_CLIENT.registerPacket(0x36, Packet133OpenTileEntity.class);
			//TO_CLIENT.registerPacket(0x37, Packet200Statistic.class);
			TO_CLIENT.registerPacket(0x38, PacketPlayerListItem.class);
			TO_CLIENT.registerPacket(0x39, PacketPlayerAbilities.class);
			TO_CLIENT.registerPacket(0x3A, PacketTabComplete.class);
			//TO_CLIENT.registerPacket(0x3B, Packet206SetScoreboardObjective.class);
			//TO_CLIENT.registerPacket(0x3C, Packet207SetScoreboardScore.class);
			//TO_CLIENT.registerPacket(0x3D, Packet208SetScoreboardDisplayObjective.class);
			//TO_CLIENT.registerPacket(0x3E, Packet209SetScoreboardTeam.class);
			TO_CLIENT.registerPacket(0x3F, PacketPluginMessage.class);
			TO_CLIENT.registerPacket(0x40, PacketKick.class);
			
			// 1.8
			TO_CLIENT.registerPacket(0x45, Packet18Title.class);

			TO_SERVER.registerPacket(0x00, PacketKeepAlive.class);
			TO_SERVER.registerPacket(0x01, PacketChat.class);
			TO_SERVER.registerPacket(0x02, PacketUseEntity.class);
			TO_SERVER.registerPacket(0x03, PacketPlayerOnGround.class);
			TO_SERVER.registerPacket(0x04, PacketPlayerPosition.class);
			TO_SERVER.registerPacket(0x05, PacketPlayerLook.class);
			TO_SERVER.registerPacket(0x06, PacketPlayerLookAndPosition.class);
			TO_SERVER.registerPacket(0x07, PacketDigging.class);
			TO_SERVER.registerPacket(0x08, PacketBlockPlacement.class);
			TO_SERVER.registerPacket(0x09, PacketHeldItemChange.class);
			TO_SERVER.registerPacket(0x0A, PacketAnimation.class);
			TO_SERVER.registerPacket(0x0B, PacketEntityAction.class);
			TO_SERVER.registerPacket(0x0C, PacketSteerVehicle.class);
			TO_SERVER.registerPacket(0x0D, PacketCloseWindow.class);
			TO_SERVER.registerPacket(0x0E, PacketClickWindow.class);
			TO_SERVER.registerPacket(0x0F, PacketConfirmTransaction.class);
			TO_SERVER.registerPacket(0x10, PacketCreativeInventoryAction.class);
			TO_SERVER.registerPacket(0x11, PacketEnchantItem.class);
			TO_SERVER.registerPacket(0x12, PacketUpdateSign.class);
			TO_SERVER.registerPacket(0x13, PacketPlayerAbilities.class);
			TO_SERVER.registerPacket(0x14, PacketTabComplete.class);
			TO_SERVER.registerPacket(0x15, PacketClientSettings.class);
			TO_SERVER.registerPacket(0x16, PacketClientStatus.class);
			TO_SERVER.registerPacket(0x17, PacketPluginMessage.class);
			
		}
	},
	// 1
	STATUS {
		{
			TO_CLIENT.registerPacket(0x00, PacketKick.class);
			TO_CLIENT.registerPacket(0x01, Packet17PingTime.class);

			TO_SERVER.registerPacket(0x00, Packet17StatusRequest.class);
			TO_SERVER.registerPacket(0x01, Packet17PingTime.class);
		}
	},
	//2
	LOGIN {
		{
			TO_CLIENT.registerPacket(0x00, PacketKick.class);
			TO_CLIENT.registerPacket(0x01, PacketEncryptionRequest.class);
			TO_CLIENT.registerPacket(0x02, Packet17LoginSuccess.class);
			
			// 1.8
			TO_CLIENT.registerPacket(0x3, Packet18LoginCompression.class);

			TO_SERVER.registerPacket(0x00, Packet17LoginRequest.class);
			TO_SERVER.registerPacket(0x01, PacketEncryptionResponse.class);
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
				throw new RuntimeException(String.format("Couldn't construct packet ID %1$d (0x0%1$X)", id), ex);
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
