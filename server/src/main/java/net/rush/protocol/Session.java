package net.rush.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import net.rush.Server;
import net.rush.ServerProperties;
import net.rush.model.Player;
import net.rush.protocol.packets.Packet17LoginSuccess;
import net.rush.protocol.packets.Packet18LoginCompression;
import net.rush.protocol.packets.PacketKeepAlive;
import net.rush.protocol.packets.PacketKick;
import net.rush.protocol.packets.PacketLogin;
import net.rush.protocol.utils.ClientVersion;
import net.rush.util.enums.Dimension;

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
		 * Server is waiting for the client to send its initial handshake packet.
		 */
		EXCHANGE_HANDSHAKE,

		/**
		 * Server is waiting for the client to send its identification packet.
		 */
		EXCHANGE_IDENTIFICATION,

		/**
		 * Session has an associated player.
		 */
		GAME;
	}

	@Getter
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

	@Getter
	@Setter
	private State state = State.EXCHANGE_HANDSHAKE;
	@Getter
	private Player player;

	/** True for client older than 1.7x */
	@Getter
	private final boolean compat;

	@Getter
	private ClientVersion clientVersion;

	private boolean pendingRemoval = false;
	@Getter
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
		this.clientVersion = compat ? new ClientVersion(78) : new ClientVersion(4);
	}

	/**
	 * Tries to login player, if there is no player associated with this session.
	 */
	public void loginPlayer(String name) {
		if (this.player != null)
			throw new IllegalStateException("Player is already defined: " + player.getName());
		
		if(server.getWorld().getPlayer(name) != null)
			server.getWorld().getPlayer(name).getSession().disconnect("You are logged from another location");
		
		if(clientVersion.getProtocol() > 26)
			send(new Packet18LoginCompression(Packet18LoginCompression.COMPRESSION_DISABLED));
		
		if(!compat)
			send(new Packet17LoginSuccess("0-0-0-0-0", name));
		
		ServerProperties p = server.getProperties();
		send(new PacketLogin(0, p.levelType, p.gamemode, Dimension.NORMAL, p.difficulty, p.maxBuildHeight, p.maxPlayers, p.hardcore));
		
		this.player = new Player(this, name);
		this.server.getWorld().getPlayers().add(player);
	}

	/**
	 * Handles any queued messages for this session and increments the timeout
	 * counter.
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
				send(new PacketKeepAlive(pingMessageId));
			} else 
				disconnect("Timed out");

			timeoutCounter = 0;
		} else 
			timeoutCounter++;

		return true;
	}

	/**
	 * Sends a packet to the client.
	 */
	public void send(Packet packet) {
		channel.writeAndFlush(packet);
	}
	
	/**
	 * Disconnects the session with the specified reason. This causes a
	 * {@link PacketKick} to be sent. When it has been delivered, the channel
	 * is closed.
	 */
	@SuppressWarnings("deprecation")
	public void disconnect(String reason) {
		channel.writeAndFlush(new PacketKick(reason)).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public String toString() {
		return Session.class.getName() + " [address=" + getIp() + "]";
	}

	/**
	 * Adds a packet to the unprocessed queue.
	 * @param <T> The type of packet.
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
}

