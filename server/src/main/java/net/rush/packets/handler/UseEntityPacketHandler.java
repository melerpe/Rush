package net.rush.packets.handler;

import net.rush.model.Entity;
import net.rush.model.LivingEntity;
import net.rush.model.Player;
import net.rush.net.Session;
import net.rush.packets.packet.UseEntityPacket;

public final class UseEntityPacketHandler extends PacketHandler<UseEntityPacket> {

	@Override
	public void handle(Session session, Player player, UseEntityPacket message) {

		Entity en = session.getServer().getWorld().getEntities().getEntity(message.getTargetEntityId());
		
		if (en instanceof LivingEntity)
			if(message.getRightclick())
				((LivingEntity)en).onPlayerInteract(player);
			else
				((LivingEntity)en).onPlayerHit(player);
	}

}

