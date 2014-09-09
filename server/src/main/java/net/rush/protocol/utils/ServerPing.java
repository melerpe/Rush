package net.rush.protocol.utils;

public class ServerPing {

	private final Protocol version;
	private final String description;
	private final String favicon;
	private final Players players;

	public ServerPing(Protocol version, Players players, String description, String favicon) {
		this.version = version;
		this.description = description;
		this.favicon = favicon;
		this.players = players;
	}

	public Protocol getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

	public String getFavicon() {
		return favicon;
	}

	public Players getPlayers() {
		return players;
	}
	
	public static class Protocol {
		
		private final String name;
		private final int protocol;
		
		public Protocol(String name, int protocol) {
			this.name = name;
			this.protocol = protocol;
		}
		
		public String getName() {
			return name;
		}
		
		public int getProtocol() {
			return protocol;
		}
	}

	public static class Players {
		
		private final int max;
		private final int online;
		
		public Players(int max, int online) {
			this.max = max;
			this.online = online;
		}

		public int getMax() {
			return max;
		}
		
		public int getOnline() {
			return online;
		}
	}
}