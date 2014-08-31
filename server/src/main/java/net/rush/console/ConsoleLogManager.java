package net.rush.console;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.fusesource.jansi.AnsiConsole;

public class ConsoleLogManager {

	private static Logger logger = Logger.getLogger("Minecraft");
	private static Logger global = Logger.getLogger("");	
	
	public static void register() {
		logger.setUseParentHandlers(false);

		Handler[] handlers = logger.getHandlers();

		for (int index = 0; index < handlers.length; ++index) {
			Handler handler = handlers[index];
			logger.removeHandler(handler);
		}

		for (java.util.logging.Handler handler : global.getHandlers()) {
			global.removeHandler(handler);
		}
		
		AnsiConsole.systemInstall();
		
		ConsoleLogFormatter formatter = new ConsoleLogFormatter();

		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(formatter);

		logger.addHandler(handler);

		// TODO add back
		/*try {
			FileHandler fileHandler = new FileHandler("server.log");
			fileHandler.setFormatter(formatter);
			logger.addHandler(fileHandler);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Failed to initiate server.log", ex);
		}*/
		
		global.addHandler(handler);
	}
}
