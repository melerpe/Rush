package net.rush.model;
import net.rush.Server;

public abstract interface CommandSender {
	
	public abstract void sendMessage(String paramString);
	public abstract String getName();
	public abstract Server getServer();
}