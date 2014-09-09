package net.rush.cmd;

import net.rush.model.CommandSender;

import org.bukkit.ChatColor;

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

