package net.rush.protocol;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.rush.gui.contentpane.GuiPane;
import net.rush.inventory.PlayerInventory;
import net.rush.model.Block;
import net.rush.model.Entity;
import net.rush.model.Item;
import net.rush.model.ItemStack;
import net.rush.model.LivingEntity;
import net.rush.model.Player;
import net.rush.protocol.Session.State;
import net.rush.protocol.packets.Packet17LoginRequest;
import net.rush.protocol.packets.Packet17PingTime;
import net.rush.protocol.packets.Packet17StatusRequest;
import net.rush.protocol.packets.PacketAnimation;
import net.rush.protocol.packets.PacketBlockChange;
import net.rush.protocol.packets.PacketBlockPlacement;
import net.rush.protocol.packets.PacketChat;
import net.rush.protocol.packets.PacketClickWindow;
import net.rush.protocol.packets.PacketConfirmTransaction;
import net.rush.protocol.packets.PacketCreativeInventoryAction;
import net.rush.protocol.packets.PacketDigging;
import net.rush.protocol.packets.PacketDigging.DiggingStatus;
import net.rush.protocol.packets.PacketEntityAction;
import net.rush.protocol.packets.PacketEntityHeadLook;
import net.rush.protocol.packets.PacketHandshake;
import net.rush.protocol.packets.PacketHeldItemChange;
import net.rush.protocol.packets.PacketKeepAlive;
import net.rush.protocol.packets.PacketKick;
import net.rush.protocol.packets.PacketPlayerListItem;
import net.rush.protocol.packets.PacketPlayerLook;
import net.rush.protocol.packets.PacketPlayerLookAndPosition;
import net.rush.protocol.packets.PacketPlayerOnGround;
import net.rush.protocol.packets.PacketPlayerPosition;
import net.rush.protocol.packets.PacketPluginMessage;
import net.rush.protocol.packets.PacketServerListPing;
import net.rush.protocol.packets.PacketUseEntity;
import net.rush.protocol.utils.ServerPing;
import net.rush.protocol.utils.ServerPing.Players;
import net.rush.protocol.utils.ServerPing.Protocol;
import net.rush.util.MathHelper;
import net.rush.util.StringUtils;
import net.rush.util.ThreadLoginVerifier;
import net.rush.world.World;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PacketHandler {

	private Logger logger = Logger.getLogger("Minecraft");

	public <T extends Packet> void handle(Session session, Player player, T packet) {
		try {

			String name = packet.getClass().getSimpleName();
			
			if(name.contains("Position") || name.contains("Look"))
				getClass().getMethod("handle", Session.class, Player.class, PacketPlayerOnGround.class).invoke(this, session, player, packet);
			else
				getClass().getMethod("handle", Session.class, Player.class, packet.getClass()).invoke(this, session, player, packet);

			if(!name.contains("Position") && !name.contains("PlayerOnGround") && !name.contains("Look") && !name.contains("ChatPacket") && !name.contains("KeepAlive")  && !name.contains("Animation"))
				logger.info("Handling packet: " + packet.getClass().getSimpleName());
		} catch (NoSuchMethodException | NoSuchMethodError | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
			logger.info("&cMissing handler for packet: " + packet.getClass().getSimpleName());
			session.getServer().getGui().showPane(new GuiPane("Unhandled packet", "Missing handler for packet:", packet.getClass().getSimpleName(), Color.RED, Color.WHITE, Color.WHITE));
		}
	}

	public void handle(Session session, Player player, PacketServerListPing packet) {
		/*Object[] infos = { 1, 78, "1.6.4", session.getServer().getProperties().motd, session.getServer().getWorld().getPlayers().size(), session.getServer().getProperties().maxPlayers };
		StringBuilder builder = new StringBuilder();

		for (Object info : infos) {
			if (builder.length() == 0)
				builder.append('\u00A7');
			else
				builder.append('\0');

			builder.append(info.toString().replace("\0", ""));
		}*/

		String kickMessage = ChatColor.DARK_BLUE
				+ "\00" + 78
				+ "\00" + "1.5.2-1.6.4"
				+ "\00" + session.getServer().getProperties().motd
				+ "\00" + session.getServer().getWorld().getPlayers().size()
				+ "\00" + session.getServer().getProperties().maxPlayers;		
		session.send(new PacketKick(kickMessage, false));
	}

	public void handle(Session session, Player player, PacketKeepAlive packet) {
		if (session.getPingMessageId() == packet.getToken())
			session.pong();
	}

	public void handle(Session session, Player player, Packet17PingTime packet) {
		session.send(new Packet17PingTime(packet.time));
	}

	public void handle(Session session, Player player, PacketHandshake message) {
		// 1.7 clients are not logging in that way
		session.setClientVersion(message.getProtocolVer());

		if (!session.isCompat())
			return;

		if(message.getProtocolVer() < 78) {
			//session.disconnect("Outdated client! (Connect with 1.6.4)");
			//return;
		} else if (message.getProtocolVer() > 78) {
			session.disconnect("Outdated server!");
			return;
		}

		Session.State state = session.getState();

		if (state == Session.State.EXCHANGE_HANDSHAKE) {
			session.setState(State.EXCHANGE_IDENTIFICATION);

			if(session.getServer().getProperties().onlineMode)
				new ThreadLoginVerifier(session, message).start();
			else 
				session.loginPlayer(message.getUsername());			

		} else {
			session.disconnect("Handshake already exchanged.");
		}
	}

	public void handle(Session session, Player player, Packet17StatusRequest message) {
		ServerPing response = new ServerPing(
				new Protocol(session.getClientVersion().getVersion(), session.getClientVersion().getProtocol()), 
				new Players(session.getServer().getProperties().maxPlayers, session.getServer().getWorld().getPlayers().size()),
				session.getServer().getProperties().motd + "\n" + ChatColor.GREEN + "You are displaying Rush on " + session.getClientVersion().toString(), 
				session.getServer().getProperties().favicon);

		session.send(new PacketKick(response));
	}

	public void handle(Session session, Player player, PacketKick message) {
		session.disconnect("Goodbye!");
		for(Player pl : session.getServer().getWorld().getPlayers())
			pl.getSession().send(new PacketPlayerListItem(player.getName(), player.getGamemode(), false, (short)100));

		session.getServer().getLogger().info(player.getName() + " lost connection: " + message.getReason());
	}

	public void handle(Session session, Player player, PacketAnimation message) {
		message = new PacketAnimation(player.getId(), message.getAnimation());
		for (Player p : player.getWorld().getPlayers())
			if (p != player)
				p.getSession().send(message);
	}

	public void handle(Session session, Player player, PacketCreativeInventoryAction message) {

		if (message.getItem() != null && message.getItem() != null && Block.byId[message.getItem().getId()] == null && Item.byId[message.getItem().getId()] == null) {
			if(message.getSlot() != -1)
				player.getInventory().setItem(player.getInventory().getSlotConverter().netToLocal(message.getSlot()), null);
			return;
		}

		if(message.getSlot() == -1)
			player.throwItemFromPlayer(message.getItem(), message.getItem().count);		
		else
			player.getInventory().setItem(player.getInventory().getSlotConverter().netToLocal(message.getSlot()), message.getItem());
	}

	public void handle(Session session, Player player, PacketDigging message) {
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

		if(message.getStatus() == DiggingStatus.DROP_ITEM) {
			if(player.getItemInHand() != null && player.getItemInHand() != null && player.getItemInHand().getId() != 0 ) {
				player.throwItemFromPlayer(player.getItemInHand(), 1);
				player.getInventory().takeOrDamageItemInHand(player, false);
			}
			return;
		}

		int metadata = world.getBlockData(x, y, z);

		if (player.getGamemode() == GameMode.CREATIVE || message.getStatus() == DiggingStatus.DONE_DIGGING
				|| (message.getStatus() == DiggingStatus.START_DIGGING && block.getBlockHardness() == 0F)) {

			block.onBlockPreDestroy(world, x, y, z, metadata);
			block.onBlockDestroyed(world, player, x, y, z, metadata);

			if(player.getGamemode() != GameMode.CREATIVE) {
				block.dropBlock(world, x, y, z, metadata, 0);

				if(player.getItemInHand() != null && player.getItemInHand() != null && player.getItemInHand().getId() != 0 )
					player.getInventory().takeOrDamageItemInHand(player, true);
			} else
				player.sendMessage("Block broken in creative: " + block.getName() + " at X: " + x + " Y: " + y + " Z: " + z);

			world.setAir(x, y, z);
			world.playEffectExceptTo(Effect.STEP_SOUND, x, y, z, block.id, player);
		}
	}

	public void handle(Session session, Player player, PacketEntityAction message) {
		switch (message.getAction()) {
		case ACTION_CROUCH:
			player.setCrouching(true);
			break;
		case ACTION_UNCROUCH:
			player.setCrouching(false);
			break;
		case START_SPRINTING:
			player.setSprinting(true);
			break;
		case STOP_SPRINTING:
			player.setSprinting(false);
			break;
		default:
			throw new NullPointerException("Unknown action found while handling EntityActionPacket (ID " + message.getAction().getId() + ")");
		}
	}

	public void handle(Session session, Player player, PacketHeldItemChange message) {
		player.getInventory().setHeldItemSlot((int)message.getSlotId());
	}

	public void handle(Session session, Player player, PacketChat message) {
		if (player == null)
			return;

		String text = message.getMessage();

		if(text == null || "".equals(text))
			session.disconnect("Cannot send an empty message");

		if (text.length() > 110) {
			session.disconnect("Chat message too long");
		} else {

			if(text.matches("(&([a-f0-9k-or]))"))
				return;

			text = text.replaceAll("\\s+", " ").trim();

			if (text.startsWith("/")) {
				session.getServer().getCommandManager().execute(player, text);
				logger.info(player.getName() + " issued server command: " + text);
			} else {
				player.getServer().broadcastMessage("<" + player.getName() + "> " + text);
				logger.info(player.getName() + ": " + text);
			}
		}
	}

	public void handle(Session session, Player player, PacketPlayerLook message) {
		if (player == null)
			return;

		player.setRotation(message.getYaw(), message.getPitch());
		session.getServer().broadcastPacketExcept(new PacketEntityHeadLook(player.getId(), (byte) player.getRotation().getIntYaw()), player);
	}

	public void handle(Session session, Player player, Packet17LoginRequest message) {
		session.loginPlayer(message.name);
	}

	public void handle(Session session, Player player, PacketPlayerOnGround message) {
		if(player == null)
			return;
		
		boolean hasLook = false;
		boolean hasPosition = false;
		
		if (message instanceof PacketPlayerLook)
			hasLook = true;
		if (message instanceof PacketPlayerPosition)
			hasPosition = true;
		if (message instanceof PacketPlayerLookAndPosition) {
			hasLook = true;
			hasPosition = true;
		}
		
		if(hasPosition) 
			player.setPosition(message.getX(), message.getYOrStance(), message.getZ());
		
		if(hasLook) {
			player.setRotation(message.getYaw(), message.getPitch());
			session.getServer().broadcastPacketExcept(new PacketEntityHeadLook(player.getId(), (byte) player.getRotation().getIntYaw()), player);
		}
		
		player.setOnGround(message.isOnGround());
		
		if(player.getGamemode() != GameMode.SURVIVAL)
			return;

		if(!player.isOnGround())
			player.airTicks++;
		else {
			if (player.airTicks > 16) {
				double dmg = (2 * player.airTicks) / 3.8;
				System.out.println("DMG: " + dmg);
				player.damage(MathHelper.floor_double(dmg), DamageCause.FALL);
			}
				
			if(player.airTicks > 0)
				System.out.println("Ticks in air: " + player.airTicks);
			
			player.airTicks = 0;
		}			

	}

	public void handle(Session session, Player player, PacketPluginMessage message) {
		logger.info("pluginMessage channel: " + message.getChannel() + ", data: " + new String(message.getData(), StandardCharsets.UTF_8));
	}

	
	public void handle(Session session, Player player, PacketUseEntity message) {

		Entity en = player.getWorld().getEntities().getEntity(message.getTargetEntityId());

		if (en instanceof LivingEntity)
			if(message.isLeftclick()) 
				((LivingEntity)en).onPlayerHit(player);				
			else
				((LivingEntity)en).onPlayerInteract(player);
	}

	public void handle(Session session, Player player, PacketClickWindow message) {
		if (player == null)
			return;

		PlayerInventory inv = player.getInventory();

		if (message.getSlot() == -1 || message.getSlot() == -999) {
			player.setItemOnCursor(null);
			response(session, message, true);
			return;
		}

		int slot = inv.getSlotConverter().netToLocal(message.getSlot());

		if (slot < 0) {
			response(session, message, false);
			player.getServer().getLogger().log(Level.WARNING, "Got invalid inventory slot " + message.getSlot() + " from " + player.getName());
			return;
		}

		ItemStack currentItem = inv.getItem(slot);

		if (player.getGamemode() == GameMode.CREATIVE && message.getWindowId() == inv.getId()) {
			response(session, message, false);
			player.onSlotSet(inv, slot, currentItem);
			player.getServer().getLogger().log(Level.WARNING, player.getName() + " tried an invalid inventory action in Creative mode!");
			return;
		}
		if (currentItem == null || currentItem == null) {
			if (message.getClickedItem() != null && message.getClickedItem() != null && message.getClickedItem().getId() != -1) {
				player.onSlotSet(inv, slot, currentItem);
				response(session, message, false);
				return;
			}
		} else if (!message.getClickedItem().doItemsMatch(currentItem)) {
			player.onSlotSet(inv, slot, currentItem);
			response(session, message, false);
			return;
		}

		if (message.getMode() == 1) {
			/*if (inv == player.getInventory().getOpenWindow()) {
                // TODO: if player has e.g. chest open
            } else if (inv == player.getInventory().getCraftingInventory()) {
               // TODO: crafting stuff
            } else {*/
			if (slot < 9) {
				for (int i = 9; i < 36; ++i) {
					if (inv.getItem(i) == null || inv.getItem(i) == null) {
						// FIXME itemstacks
						inv.setItem(i, currentItem);
						inv.setItem(slot, null);
						response(session, message, true);
						return;
					}
				}
			} else {
				for (int i = 0; i < 9; ++i) {
					if (inv.getItem(i) == null || inv.getItem(i) == null) {
						// FIXME itemstacks
						inv.setItem(i, currentItem);
						inv.setItem(slot, null);
						response(session, message, true);
						return;
					}
				}
			}
			//}
			response(session, message, false);
			return;
		}

		/*if (inv == player.getInventory().getCraftingInventory() && slot == CraftingInventory.RESULT_SLOT && player.getItemOnCursor() != null) {
            response(session, message, false);
            return;
        }*/

		response(session, message, true);
		inv.setItem(slot, player.getItemOnCursor());
		player.setItemOnCursor(currentItem);

		/*if (inv == player.getInventory().getCraftingInventory() && slot == CraftingInventory.RESULT_SLOT && currentItem != null) {
            player.getInventory().getCraftingInventory().craft();
        }*/
	}

	public void handle(Session session, Player player, PacketBlockPlacement packet) {
		if(packet.getDirection() == -1)
			return;

		World world = player.getWorld();
		int x = packet.getX();
		int z = packet.getZ();
		int y = packet.getY();
		int xOffset = (int) (packet.getCursorX() * 16.0F);
		int yOffset = (int) (packet.getCursorY() * 16.0F);
		int zOffset = (int) (packet.getCursorZ() * 16.0F);
		int direction = packet.getDirection();

		if (placeOrActivate(player, world, packet.getHeldItem(), x, y, z, direction, xOffset, yOffset, zOffset))
			if(player.getGamemode() != GameMode.CREATIVE)
				player.getInventory().takeOrDamageItemInHand(player, true);

		if(packet.getHeldItem() == null)
			return;

		int blockId = packet.getHeldItem().getId();

		player.getSession().send(new PacketBlockChange(x, y, z, world));

		if (direction == 0)
			--y;

		if (direction == 1)
			++y;

		if (direction == 2)
			--z;

		if (direction == 3)
			++z;

		if (direction == 4)
			--x;

		if (direction == 5)
			++x;

		player.getSession().send(new PacketBlockChange(x, y, z, world));

		if (Block.byId[blockId] != null && Block.byId[blockId].material.isSolid())
			player.sendMessage("&bPlaced " + Block.byId[blockId].getName() + " @ " + StringUtils.serializeLoc(x, y, z) + " &dside: " + packet.getDirection());
		else if (Item.byId[blockId] != null)
			player.sendMessage("&5Clicked with item " + Item.byId[blockId].getName());
		else if (Item.byId[blockId] == null) {
			player.sendMessage("&eItem &6" + Material.getMaterial(blockId) + " is not yet implemented!");
		} else
			player.sendMessage("&6Block " + Material.getMaterial(blockId) + " is not yet implemented!");
	}

	// HELPERS

	private void response(Session session, PacketClickWindow message, boolean success) {
		session.send(new PacketConfirmTransaction(message.getWindowId(), message.getActionId(), success));
	}

	public boolean placeOrActivate(Player player, World world, ItemStack stack, int x, int y, int z, int direction, float xOffset, float yOffset, float zOffset) {
		if (!player.isCrouching() || stack == null) {
			int blockId = world.getType(x, y, z);

			if (blockId > 0 && Block.byId[blockId] != null && Block.byId[blockId].onBlockInteract(world, x, y, z, player, direction, xOffset, yOffset, zOffset)) {
				return true;
			}
		}		

		if (stack == null)	
			return false;

		Item item = Item.byId[stack.getId()];		
		if(item != null)
			return item.onItemUse(stack, player, world, x, y, z, direction, xOffset, yOffset, zOffset);

		return false;
	}
}