package net.rush.cmd;

import org.bukkit.ChatColor;

import net.rush.model.CommandSender;

/**
 * Server reload command. Reloading some parts of the server.
 * Restarting is highly advised and no warranty is given with the usage of this command.
 */
public final class ReloadCommand extends Command {

	public ReloadCommand() {
		super("reload");
	}

	@Override
	public void execute(CommandSender player, String[] args) {
		player.getServer().reload();
		player.sendMessage(ChatColor.GREEN + "Server reloaded (Currently just properties)."); // TODO
	}

}

