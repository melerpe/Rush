package net.rush.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.rush.Server;

public class MinecraftHandler extends SimpleChannelInboundHandler<Packet> {

	private static final Logger logger = Logger.getLogger("Minecraft");
	private final Server server;

	public Session session;
	private boolean compat;

	public MinecraftHandler(Server server, boolean compat) {
		this.server = server;
		this.compat = compat;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();

		this.session = new Session(server, ch, compat);
		server.getSessionRegistry().add(session);

		logger.info("Channel connected: " + ch + ".");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		server.getSessionRegistry().remove(session);

		logger.info("Channel disconnected: " + ctx.channel() + ".");
	}
		
	@Override
	public void messageReceived(ChannelHandlerContext ctx, Packet packet) throws Exception {
		session.messageReceived(packet);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (cause instanceof IOException)
			logger.info("End of stream ");
		else
			logger.log(Level.WARNING, "Exception caught, closing channel: " + ctx.channel() + "...", cause);
		ctx.close();
	}

}

