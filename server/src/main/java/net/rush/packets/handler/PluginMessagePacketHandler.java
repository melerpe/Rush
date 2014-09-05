package net.rush.packets.handler;

import java.nio.charset.StandardCharsets;

import net.rush.model.Player;
import net.rush.net.Session;
import net.rush.packets.packet.PluginMessagePacket;

/**
 * A {@link PacketHandler} which disconnects clients when they send a
 * {@link KickMessage} to the server.

 */
public final class PluginMessagePacketHandler extends PacketHandler<PluginMessagePacket> {

	@Override
	public void handle(Session session, Player player, PluginMessagePacket message) {
		logger.info("pluginMessage channel: " + message.getChannel() + ", data: " + new String(message.getData(), StandardCharsets.UTF_8));
		// Moved to ServerListPingPacketHandler
		//if(message.getChannel().equals("MC|PingHost")) {}
	}

}

