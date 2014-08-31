package net.rush.console;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;

public class ConsoleLogFormatter extends Formatter {

	private final SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");

	public String format(LogRecord record) {
		Throwable ex = record.getThrown();

		if (ex != null)
			ex.printStackTrace();		

		return toAnsiColors(date.format(record.getMillis()) + " [" + record.getLevel().getLocalizedName().toUpperCase() + "] " + formatMessage(record) + "&r\n");
	}

	private String toAnsiColors(String str) {
		str = str.replace("&", "§");
		str = str.replace("§0", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString())
				.replace("§1", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString())
				.replace("§2", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString())
				.replace("§3", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString())
				.replace("§4", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString())
				.replace("§5", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString())
				.replace("§6", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString())
				.replace("§7", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString())
				.replace("§8", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString())
				.replace("§9", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString())
				.replace("§a", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString())
				.replace("§b", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString())
				.replace("§c", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).bold().toString())
				.replace("§d", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString())
				.replace("§e", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString())
				.replace("§f", Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString())
				.replace("§k", Ansi.ansi().a(Attribute.BLINK_SLOW).toString())
				.replace("§l", Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString())
				.replace("§m", Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString())
				.replace("§n", Ansi.ansi().a(Attribute.UNDERLINE).toString())
				.replace("§o", Ansi.ansi().a(Attribute.ITALIC).toString())
				.replace("§r", Ansi.ansi().a(Attribute.RESET).toString());
		return str;
	}

}