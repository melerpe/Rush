package net.rush.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import net.rush.protocol.Session;
import net.rush.protocol.packets.PacketHandshake;

public class ThreadLoginVerifier extends Thread {

	private final PacketHandshake packet;
	private final Session session;

	public ThreadLoginVerifier(Session session, PacketHandshake packet) {
		this.session = session;
		this.packet = packet;
	}

	public void run() {
		try {
			String serverId = session.getServer().serverId;
			URL url = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(packet.getUsername(), "UTF-8") + "&serverId=" + URLEncoder.encode(serverId, "UTF-8"));
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String response = reader.readLine();
			reader.close();
			
			System.out.println("(Login verifier) Got response: " + response);
			
			if (response.equals("YES"))
				session.loginPlayer(packet.getUsername());
			else
				session.disconnect("Failed to verify username!");
			
		} catch (Exception ex) {
			session.disconnect("Failed to verify username! [Error: " + ex + "]");
			ex.printStackTrace();
		}
	}
}
