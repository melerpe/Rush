package net.rush.packets.handler;

import net.rush.model.Player;
import net.rush.net.Session;
import net.rush.packets.packet.PingTime;

/**
 * A {@link PacketHandler} which disconnects clients when they send a
 * {@link KickMessage} to the server.
 */
public final class PacketPingTimeHandler extends PacketHandler<PingTime> {

	@Override
	public void handle(Session session, Player player, PingTime packet) {
		session.send(new PingTime(packet.time));
	}

}

