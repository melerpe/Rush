package net.rush.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.util.logging.Level;

import net.rush.Server;

public class MinecraftHandler extends SimpleChannelInboundHandler<Packet> {

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

		server.getLogger().info("Channel connected: " + ch + ".");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		server.getSessionRegistry().remove(session);

		server.getLogger().info("Channel disconnected: " + ctx.channel() + ".");
	}	
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
		session.messageReceived(packet);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (cause instanceof IOException)
			server.getLogger().info("End of stream ");
		else
			server.getLogger().log(Level.WARNING, "Exception caught, closing channel: " + ctx.channel() + "...", cause);
		ctx.close();
	}

}

