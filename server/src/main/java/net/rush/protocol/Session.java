package net.rush.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import net.rush.Server;
import net.rush.model.Player;
import net.rush.protocol.packets.KeepAlivePacket;
import net.rush.protocol.packets.KickPacket;

/**
 * A single connection to the server, which may or may not be associated with a
 * player.
 */
public final class Session {

	/**
	 * The number of ticks which are elapsed before a client is disconnected due
	 * to a timeout.
	 * If client don´t get this value for about 22 seconds it gets disconnected,
	 * but it is recommended to set this to lower number due to server lag.
	 */
	private static final int TIMEOUT_TICKS = 6 * 20;

	/**
	 * The state this connection is currently in.
	 */
	public enum State {

		/**
		 * In the exchange handshake state, the server is waiting for the client
		 * to send its initial handshake packet.
		 */
		EXCHANGE_HANDSHAKE,

		/**
		 * In the exchange identification state, the server is waiting for the
		 * client to send its identification packet.
		 */
		EXCHANGE_IDENTIFICATION,

		/**
		 * In the game state the session has an associated player.
		 */
		GAME;
	}

	private final Server server;
	private final Channel channel;

	/**
	 * A queue of incoming and unprocessed messages.
	 */
	private final Queue<Packet> messageQueue = new ArrayDeque<Packet>();

	/**
	 * A timeout counter. This is increment once every tick and if it goes above
	 * a certain value the session is disconnected.
	 */
	private int timeoutCounter = 0;

	@Getter@Setter
	private State state = State.EXCHANGE_HANDSHAKE;
	@Getter
	private Player player;

	/**
	 * True for client older than 1.7x
	 */
	@Getter
	private final boolean compat;

	@Getter
	private ClientVersion clientVersion = new ClientVersion(4); // default to prevent NPE

	private boolean pendingRemoval = false;
	private int pingMessageId;

	/**
	 * Creates a new session.
	 * @param server The server this session belongs to.
	 * @param channel The channel associated with this session.
	 */
	public Session(Server server, Channel channel, boolean compat) {
		this.server = server;
		this.channel = channel;
		this.compat = compat;
	}

	/**
	 * Sets the player associated with this session.
	 * @param player The new player.
	 * @throws IllegalStateException if there is already a player associated
	 * with this session.
	 */
	public void setPlayer(Player player) {
		if (this.player != null)
			throw new IllegalStateException();

		this.player = player;
		this.server.getWorld().getPlayers().add(player);
	}

	/**
	 * Handles any queued messages for this session and increments the timeout
	 * counter.
	 * @return 
	 * @return {@code true} if this session is still active, {@code false} if
	 * it is pending removal.
	 */
	public boolean pulse() {
		if(pendingRemoval)
			return false;

		Packet packet;
		while ((packet = messageQueue.poll()) != null) {
			packet.handle(this, player, (Packet) packet);
		}

		if (timeoutCounter >= TIMEOUT_TICKS) {
			if (pingMessageId == 0) {
				pingMessageId = new Random().nextInt();
				send(new KeepAlivePacket(pingMessageId));
			} else 
				disconnect("Timed out");

			timeoutCounter = 0;
		} else 
			timeoutCounter++;

		return true;
	}

	/**
	 * Sends a packet to the client.
	 * @param packet The message.
	 */
	public void send(Packet packet) {
		channel.writeAndFlush(packet);
	}

	/**
	 * Disconnects the session with the specified reason. This causes a
	 * {@link KickPacket} to be sent. When it has been delivered, the channel
	 * is closed.
	 * @param reason The reason for disconnection.
	 */
	@SuppressWarnings("deprecation")
	public void disconnect(String reason) {
		channel.writeAndFlush(new KickPacket(reason)).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Gets the server associated with this session.
	 * @return The server.
	 */
	public Server getServer() {
		return server;
	}

	@Override
	public String toString() {
		return Session.class.getName() + " [address=" + getIp() + "]";
	}

	/**
	 * Adds a message to the unprocessed queue.
	 * @param message The message.
	 * @param <T> The type of message.
	 */
	<T extends Packet> void messageReceived(T message) {
		messageQueue.add(message);
	}

	/**
	 * Disposes of this session by destroying the associated player, if there is
	 * one.
	 */
	void dispose() {
		if (player != null) {
			player.destroy();
			player = null; // in case we are disposed twice
		}
	}

	public int getPingMessageId() {
		return pingMessageId;
	}

	public String getIp() {
		return channel.remoteAddress().toString().replace("/", "");
	}

	public void pong() {
		timeoutCounter = 0;
		pingMessageId = 0;
	}

	void flagForRemoval() {
		pendingRemoval = true;
	}

	public void setClientVersion(int protocol) {
		this.clientVersion = new ClientVersion(protocol);
	}

	@Getter
	public static class ClientVersion {
		private final int protocol;
		private final String version;
		
		public ClientVersion(int protocol) {
			super();
			this.protocol = protocol;
			this.version = getVersion(protocol);
		}

		public static String getVersion(int protocol) {
			switch (protocol) {
			case 29: 
				return "O_1.2.5";
			case 39:
				return "O_1.3.9";
			case 51:
				return "O_1.4.7";
			case 61:
				return "O_1.5.2";
			case 78:
				return "1.6.4";
			case 4:
				return "1.7.2-5";
			case 5:
				return "1.7.6-10";
			case 47:
				return "1.8";
			default:
				throw new NullPointerException("Unsupported protocol (" + protocol + ")");
			}
		}

		@Override
		public String toString() {
			return "ver=" + version + ",protocol=" + protocol;
		}
	}
}

