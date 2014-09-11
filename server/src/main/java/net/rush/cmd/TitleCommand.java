package net.rush.cmd;

import net.rush.model.CommandSender;
import net.rush.model.Player;
import net.rush.protocol.packets.Packet18Title.TitleAction;

/**
 * A command that displays the new title on 1.8 clients.
 */
public final class TitleCommand extends Command {

	public TitleCommand() {
		super("title");
	}

	@Override
	public void execute(CommandSender player, String[] args) {
		if(!(player instanceof Player)) {
			player.sendMessage("This command cannot be used from console.");
			return;
		}
		Player pl = (Player) player;
		
		if(!pl.getSession().isCompat() && pl.getSession().getClientVersion().getProtocol() > 46) {
			pl.setTitle(TitleAction.TITLE, "Rush is great!");
			pl.setTitle(TitleAction.SUBTITLE, "Pay a fee of 5.99$ to remove this message");
		} else
			pl.sendMessage("&cTitle can only be displayed on 1.8 clients");
	}

}

