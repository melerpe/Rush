package net.rush.packets.handler;

import net.rush.model.Block;
import net.rush.model.ItemStack;
import net.rush.model.Player;
import net.rush.net.Session;
import net.rush.packets.packet.PlayerDiggingPacket;
import net.rush.world.World;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;

/**
 * A {@link PacketHandler} which processes digging messages.
 */
public final class DiggingPacketHandler extends PacketHandler<PlayerDiggingPacket> {

	@Override
	public void handle(Session session, Player player, PlayerDiggingPacket message) {
		if (player == null)
			return;

		World world = player.getWorld();

		int x = message.getX();
		int z = message.getZ();
		int y = message.getY();

		Block block = Block.byId[world.getType(x, y, z)];

		if(block == null) {
			player.sendMessage("&cUnknown broken block: " + Material.getMaterial(world.getType(x, y, z)));
			return;
		}

		if(message.getStatus() == PlayerDiggingPacket.DROP_ITEM) {
			if(player.getItemInHand() != null && player.getItemInHand() != ItemStack.NULL_ITEMSTACK && player.getItemInHand().getId() != 0 ) {
				player.throwItemFromPlayer(player.getItemInHand(), 1);
				player.getInventory().takeOrDamageItemInHand(player, false);
			}
			return;
		}

		int metadata = world.getBlockData(x, y, z);

		if (player.getGamemode() == GameMode.CREATIVE || message.getStatus() == PlayerDiggingPacket.DONE_DIGGING
				|| (message.getStatus() == PlayerDiggingPacket.START_DIGGING && block.getBlockHardness() == 0F)) {

			block.onBlockPreDestroy(world, x, y, z, metadata);
			block.onBlockDestroyedByPlayer(world, player, x, y, z, metadata);

			if(player.getGamemode() != GameMode.CREATIVE) {
				block.dropBlock(world, x, y, z, metadata, 0);
				
				if(player.getItemInHand() != null && player.getItemInHand() != ItemStack.NULL_ITEMSTACK && player.getItemInHand().getId() != 0 )
					player.getInventory().takeOrDamageItemInHand(player, true);
			} else
				player.sendMessage("Block broken in creative: " + block.getName() + " at X: " + x + " Y: " + y + " Z: " + z);

			world.setAir(x, y, z);
			world.playEffectExceptTo(Effect.STEP_SOUND, x, y, z, block.id, player);
		}
	}

}

