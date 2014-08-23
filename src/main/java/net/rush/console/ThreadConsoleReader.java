package net.rush.console;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;

import jline.console.ConsoleReader;
import net.rush.Server;

public class ThreadConsoleReader extends Thread {

	private final Server server;
	private final boolean jline;
	private final ConsoleReader reader;

	public ThreadConsoleReader(Server server, boolean jline) throws IOException {
		System.setOut(new PrintStream(new LoggerOutputStream(server.getLogger(), Level.INFO), true));
		System.setErr(new PrintStream(new LoggerOutputStream(server.getLogger(), Level.SEVERE), true));
		
		this.server = server;
		this.jline = jline;
		this.reader = new ConsoleReader(System.in, System.out);
		this.reader.setExpandEvents(false);
		this.setDaemon(true);
		this.setName("Console Reader Thread");
	}

	public void run() {
		String msg;
		
		try {
			while (server.isRunning) {
				if(jline)
					msg = reader.readLine(">", null);
				else
					msg = reader.readLine();
				
				if (msg != null && msg.length() > 0)
					server.getCommandManager().execute(server.getConsoleSender(), "/" + msg);
				
			}
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger("").log(java.util.logging.Level.SEVERE, "Error logging message", ex);
		}
	}
}
