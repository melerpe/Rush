package net.rush.packets.handler;

import net.rush.model.Player;
import net.rush.model.Rotation;
import net.rush.net.Session;
import net.rush.packets.packet.PlayerLookPacket;

/**
 * A {@link PacketHandler} that updates a {@link Player}'s {@link Rotation}
 * when the server receives a {@link RotationMessage}.

 */
public final class LookPacketHandler extends PacketHandler<PlayerLookPacket> {

	@Override
	public void handle(Session session, Player player, PlayerLookPacket message) {
		if (player == null)
			return;

		player.setRotation(new Rotation(message.getYaw(), message.getPitch()));
	}

}

