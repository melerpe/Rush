package net.rush.packets.handler;

import net.rush.model.Entity;
import net.rush.model.Player;
import net.rush.net.Session;
import net.rush.packets.packet.AnimationPacket;
import net.rush.packets.packet.impl.AnimationPacketImpl;

/**
 * A {@link PacketHandler} which handles {@link Entity} animations.
 */
public final class AnimationPacketHandler extends PacketHandler<AnimationPacket> {

	@Override
	public void handle(Session session, Player player, AnimationPacket message) {
		// TODO check the animation id is valid?
		message = new AnimationPacketImpl(player.getId(), message.getAnimation());
		for (Player p : player.getWorld().getPlayers()) {
			if (p != player) {
				p.getSession().send(message);
			}
		}
	}

}

