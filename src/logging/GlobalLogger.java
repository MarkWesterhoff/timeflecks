package logging;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Singleton class to hold to global logger for the TimeFlecks application.
 * @author Andrew
 */
public class GlobalLogger {
	private static Logger logger;

	private GlobalLogger() { }

	public static Logger getLogger() {
		if (logger == null) {
			logger = Logger.getLogger(GlobalLogger.class.getName());
			logger.setLevel(Level.INFO);
		}
		return logger;
	}
}
