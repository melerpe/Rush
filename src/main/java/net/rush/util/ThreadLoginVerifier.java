package net.rush.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.WorldType;

import net.rush.model.Player;
import net.rush.net.Session;
import net.rush.packets.packet.HandshakePacket;
import net.rush.packets.packet.LoginPacket;
import net.rush.util.enums.Dimension;

public class ThreadLoginVerifier extends Thread {

	final HandshakePacket loginPacket;
	final Session session;

	public ThreadLoginVerifier(Session session, HandshakePacket loginPacket) {
		this.session = session;
		this.loginPacket = loginPacket;
	}

	public void run() {
		try {
			String serverId = session.getServer().serverId;
			URL url = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(loginPacket.getUsername(), "UTF-8") + "&serverId=" + URLEncoder.encode(serverId, "UTF-8"));
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String response = reader.readLine();
			reader.close();
			
			System.out.println("(Login verifier) Got response: " + response);
			
			if (response.equals("YES")) {
				session.send(new LoginPacket(0, WorldType.NORMAL, GameMode.CREATIVE, Dimension.NORMAL, Difficulty.NORMAL, session.getServer().getWorld().getMaxHeight(), 30));
				session.setPlayer(new Player(session, loginPacket.getUsername(), GameMode.CREATIVE));
			} else
				session.disconnect("Failed to verify username!");
			
		} catch (Exception ex) {
			session.disconnect("Failed to verify username! [internal error " + ex + "]");
			ex.printStackTrace();
		}
	}
}