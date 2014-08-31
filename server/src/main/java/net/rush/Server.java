package net.rush;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.rush.cmd.CommandManager;
import net.rush.console.ConsoleCommandSender;
import net.rush.console.ConsoleLogManager;
import net.rush.console.ThreadConsoleReader;
import net.rush.gui.ServerGUI;
import net.rush.gui.contentpane.RushGui;
import net.rush.io.McRegionChunkIoService;
import net.rush.model.Player;
import net.rush.net.MinecraftHandler;
import net.rush.net.PacketDecoder;
import net.rush.net.PacketEncoder;
import net.rush.net.Session;
import net.rush.net.SessionRegistry;
import net.rush.packets.KickPacketWriter;
import net.rush.packets.Packet;
import net.rush.packets.Varint21FrameDecoder;
import net.rush.packets.Varint21LengthFieldPrepender;
import net.rush.packets.legacy.CompatChecker;
import net.rush.packets.legacy.LegacyCompatProvider;
import net.rush.packets.legacy.LegacyDecoder;
import net.rush.packets.legacy.LegacyEncoder;
import net.rush.packets.misc.Protocol;
import net.rush.task.TaskScheduler;
import net.rush.util.NumberUtils;
import net.rush.world.AlphaWorldGenerator;
import net.rush.world.World;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.PluginLoadOrder;

/**
 * The core class of the Rush server.
 */
public final class Server {

	public final String serverId;
	public boolean saveEnabled = true;
	public boolean isRunning = true;
	
	private static Server server;

	private final Logger logger = Logger.getLogger("Minecraft");
	private final ConsoleCommandSender consoleSender = new ConsoleCommandSender(this);
	private final RushGui gui;
	private final ServerProperties properties;
	private final TaskScheduler scheduler = new TaskScheduler(this);
	private final CommandManager commandManager = new CommandManager(this);
	private final World world;

	/** The {@link ServerBootstrap} used to initialize Netty. */
	private final ServerBootstrap bootstrap = new ServerBootstrap();

	/** A group containing all of the channels. */
	private final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	private final EventLoopGroup eventGroup = new NioEventLoopGroup(3/*, new ThreadFactoryBuilder().setNameFormat("Netty IO Thread - %1$d").build()*/); // TODO configurable

	/** A list of all the active {@link Session}s. */
	private final SessionRegistry sessions = new SessionRegistry();
	
	// Craftbukkit >
	
	public CraftServer bukkit_server;
	
	// < Craftbukkit
	
	/**
	 * @deprecated Old javadocs. Reading following is deprecated. De-pre-ca-ted. (Following Bukkit- policy compliant terms)
	 * Creates a new server on TCP port 25565 and starts listening for
	 * connections.
	 * 
	 * Creates a new server, parses arguments set up console log formatter and reader.
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		try {
			
			boolean jline = true;
			boolean gui = true;

			for (String arg : args) {
				if ("-nojline".equalsIgnoreCase(arg) || "--nojline".equalsIgnoreCase(arg))
					jline = false;
				if ("-nogui".equalsIgnoreCase(arg) || "--nogui".equalsIgnoreCase(arg))
					gui = false;
			}
			
	        System.setProperty( "java.net.preferIPv4Stack", "true" );
	        ResourceLeakDetector.setLevel(io.netty.util.ResourceLeakDetector.Level.DISABLED);
			
			ConsoleLogManager.register();
			
			if(gui)
				ServerGUI.initGui();
			
			Server server = new Server();

			new ThreadConsoleReader(server, jline).start();

		} catch (Throwable t) {
			Logger.getGlobal().log(Level.SEVERE, "Error during server initializing", t);
		}
	}

	/**
	 * Creates and initializes a new server.
	 */
	public Server() {
		server = this;
		
		logger.info("Initializing Rush for Minecraft 1.6.4 - 1.7.10");
		long initialTime = System.currentTimeMillis();

		if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
			logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar project-rush.jar\"");

		logger.info("Loading properties");
		properties = new ServerProperties("server.properties");
		properties.reload();

		world = new World(properties.levelName);
		world.setChunkManager(new McRegionChunkIoService(new File(properties.levelName)), new AlphaWorldGenerator(world));

		logger.info("Generating server id");
		serverId = Long.toString(new Random().nextLong(), 16);

		bukkit_server = new CraftServer(server);
		
		logger.info("Starting Minecraft server on " + (properties.serverIp.length() == 0 ? "*" : properties.serverIp) + ":" + properties.port);
		new NettyNetworkThread().start();
		
		Runtime.getRuntime().addShutdownHook(new ServerShutdownHandler());
		
		byte radius = 5;
		for (int x = -radius; x <= radius; ++x) {
			logger.info("Preparing spawn area: " + (x + radius) * 100 / (radius + radius + 1) + "%");

			for (int z = -radius; z <= radius; ++z) {
				world.getChunks().getChunk(((int)world.getSpawnPosition().x >> 4) + x, ((int)world.getSpawnPosition().z >> 4) + z);
			}
		}

		bukkit_server.enablePlugins(PluginLoadOrder.POSTWORLD);
		
		scheduler.start();
		gui = new RushGui();

		logger.info("Ready for connections. (Took " + NumberUtils.msToSeconds(System.currentTimeMillis() - initialTime) + "s !)");
	}

	/**
	 * Gets the channel group.
	 * @return The {@link ChannelGroup}.
	 */
	public ChannelGroup getChannelGroup() {
		return group;
	}

	/**
	 * Gets the session registry.
	 * @return The {@link SessionRegistry}.
	 */
	public SessionRegistry getSessionRegistry() {
		return sessions;
	}

	/**
	 * Gets the task scheduler.
	 * @return The {@link TaskScheduler}.
	 */
	public TaskScheduler getScheduler() {
		return scheduler;
	}

	/**
	 * Gets the command manager.
	 * @return The {@link CommandManager}.
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * Gets the world this server manages.
	 * @return The {@link World} this server manages.
	 */
	public World getWorld() {
		return world;
	}
	
	/**
	 * Broadcasts a message to every player.
	 * 
	 * @param text The message text.
	 */
	public void broadcastMessage(String text) {
		for (Player player : getWorld().getPlayers())
			player.sendMessage(text);
	}

	public RushGui getGui() {
		return gui;
	}

	public ConsoleCommandSender getConsoleSender() {
		return consoleSender;
	}

	public Logger getLogger() {
		return logger;
	}

	public ServerProperties getProperties() {
		return properties;
	}

	public static Server getServer() {
		return server;
	}
	
	public void broadcastPacket(Packet packet) {
		for(Player pl : getWorld().getPlayers())
			pl.getSession().send(packet);
	}
	
	public boolean isPrimaryThread() {
		return scheduler.isPrimaryThread();
	}

	/**
	 * A {@link Runnable} which saves chunks on shutdown.
	 */
	private class ServerShutdownHandler extends Thread {

		@Override
		public void run() {
			stopServer();
		}
	}
	
	public void stopServer() {
		isRunning = false;
		
		logger.info("Server is shutting down.");
		// Save chunks on shutdown.
		if (saveEnabled) {
			logger.info("Saving chunks...");
			try {
				world.save();
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Failed to save some chunks.", ex);
			}
		}
		logger.info("Shutdown complete.");
	}

	public void reload() {
		logger.info("Reloading properties ..");
		getProperties().reload();
	}
	
	private class NettyNetworkThread extends Thread {

		@Override
		public void run() {

			try {
				bootstrap.group(eventGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {

						ch.config().setOption(ChannelOption.IP_TOS, 0x18);
						ch.config().setOption(ChannelOption.TCP_NODELAY, false);

						ch.pipeline().addLast("timer", new ReadTimeoutHandler(30));

						if (LegacyCompatProvider.isProvidingCompat(ch.remoteAddress())) {
							ch.pipeline()
							.addLast("decoder", new LegacyDecoder()) // 1.6 decoder - reader
							.addLast("encoder", new LegacyEncoder()) // 1.6 encoder - writer
							.addLast("handler", new MinecraftHandler(server, true));
						} else {			
							if(LegacyCompatProvider.isThrottled(ch.remoteAddress()))
								return;
							
							ch.pipeline()
							
							.addLast("old", new KickPacketWriter())
							.addLast("legacy", new CompatChecker())

							.addLast("lengthdecoder", new Varint21FrameDecoder())
							.addLast("decoder", new PacketDecoder(Protocol.HANDSHAKE))

							.addLast("lengthencoder", new Varint21LengthFieldPrepender())
							.addLast("encoder", new PacketEncoder(Protocol.HANDSHAKE))

							.addLast("handler", new MinecraftHandler(server, false));
						}
					}
				});

				SocketAddress address = properties.serverIp.length() == 0 ? new InetSocketAddress(properties.port) : new InetSocketAddress(properties.serverIp, properties.port);

				try {
					bootstrap.bind(address).sync().channel().closeFuture().sync();
				} catch (Throwable ex) {
					logger.warning("**** FAILED TO BIND TO PORT!");
					logger.warning("The exception was: " + ex.getCause().toString());
					logger.warning("Perhaps a server is already running on that port?");
					System.exit(0);
				}
			} finally {
				eventGroup.shutdownGracefully();
			}
		}
	}
}
