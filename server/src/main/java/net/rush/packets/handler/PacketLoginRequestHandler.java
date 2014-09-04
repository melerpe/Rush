package net.rush.packets.handler;

import net.rush.ServerProperties;
import net.rush.model.Player;
import net.rush.net.Session;
import net.rush.packets.packet.LoginPacket;
import net.rush.packets.packet.LoginRequest;
import net.rush.packets.packet.LoginSuccess;
import net.rush.packets.packet.PacketLoginCompression;
import net.rush.util.RushException;
import net.rush.util.enums.Dimension;

/**
 * A {@link PacketHandler} which performs the initial handshake with clients.
 */
public final class PacketLoginRequestHandler extends PacketHandler<LoginRequest> {

	@Override
	public void handle(Session session, Player player, LoginRequest message) {
		if(player != null)
			throw new RushException("Player must be null! Got " + player.getName());

		if(session.getClientVersion().getProtocol() > 26)
			session.send(new PacketLoginCompression(-1));
		
		session.send(new LoginSuccess("0-0-0-0-0", message.name));
		
		ServerProperties prop = session.getServer().getProperties();
		session.send(new LoginPacket(0, prop.levelType, prop.gamemode, Dimension.NORMAL, prop.difficulty, prop.maxBuildHeight, prop.maxPlayers, prop.hardcore));
		session.setPlayer(new Player(session, message.name));
	}

}

