package net.rush.cmd;

import net.rush.model.CommandSender;

/**
 * A command that saves all known chunks in the ChunkManager.

 */
public final class SaveAllCommand extends Command {

	/**
	 * Creates the {@code /save-all} command.
	 */
	public SaveAllCommand() {
		super("save-all");
	}

	@Override
	public void execute(CommandSender player, String[] args) {
		// Should this start a separate thread instead?
		player.getServer().getWorld().save();
		player.sendMessage("&3Rush // &fChunks were successfully saved.");
	}

}

