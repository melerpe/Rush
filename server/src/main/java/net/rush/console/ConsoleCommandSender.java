package net.rush.console;

import net.rush.Server;
import net.rush.model.CommandSender;

public class ConsoleCommandSender implements CommandSender {

	private final Server server;
	
	public ConsoleCommandSender(Server server) {
		this.server = server;
	}
	
	public void sendMessage(String message) {
		server.getLogger().info(message);
	}

	public String getName() {
		return "CONSOLE";
	}

	public Server getServer() {
		return server;
	}

}
