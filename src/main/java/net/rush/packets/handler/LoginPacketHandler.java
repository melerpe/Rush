package net.rush.packets.handler;

import net.rush.model.Player;
import net.rush.net.Session;
import net.rush.net.Session.State;
import net.rush.packets.packet.LoginPacket;
import net.rush.packets.packet.impl.LoginPacketImpl;

import org.bukkit.GameMode;

/**
 * A {@link PacketHandler} which handles {@link ChatMessage}s by processing
 * commands or broadcasting messages to every player in the server.

 */
public final class LoginPacketHandler extends PacketHandler<LoginPacket> {

	@Override
	public void handle(Session session, Player player, LoginPacket message) {
		Session.State state = session.getState();
		if (state == Session.State.EXCHANGE_IDENTIFICATION) {
			session.setState(State.GAME);
			session.send(new LoginPacketImpl(0, "", "default", 1, 0, (byte)0, 256, 20));
			session.setPlayer(new Player(session, message.getUsername(), GameMode.CREATIVE)); // TODO case-correct the name
		} else {
			boolean game = state == State.GAME;
			session.disconnect(game ? "Identification already exchanged." : "Handshake not yet exchanged.");
		}
	}

}

