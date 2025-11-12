package utils;

import java.util.logging.*;

public class Log {

    private static final Logger LOGGER = Logger.getLogger(Log.class.getName());

    // ANSI Colors
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_LIGHT_GRAY = "\u001B[37m";
    private static final String ANSI_AMBER = "\u001B[33m";
    private static final String ANSI_BRIGHT_ORANGE = "\u001B[38;5;208m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";

    static {
        initializeLogger();
    }

    private static void initializeLogger() {
        LOGGER.setUseParentHandlers(false);

        // Prevent duplicate handlers
        if (LOGGER.getHandlers().length == 0) {
            Handler handler = new ConsoleHandler();
            handler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    String color = ANSI_RESET;
                    if (record.getLevel() == Level.SEVERE) {
                        color = ANSI_BRIGHT_ORANGE;
                    } else if (record.getLevel() == Level.WARNING) {
                        color = ANSI_AMBER;
                    } else if (record.getLevel() == Level.INFO) {
                        color = ANSI_LIGHT_GRAY;
                    } else if (record.getLevel() == Level.FINE) {
                        color = ANSI_BLUE;
                    } else if (record.getLevel() == Level.CONFIG) {
                        color = ANSI_GREEN;
                    }

                    return color + super.formatMessage(record) + ANSI_RESET + "\n";

                }
            });
            handler.setLevel(Level.ALL);
            LOGGER.addHandler(handler);
            LOGGER.setLevel(Level.ALL);
        }
    }

    // --- Logging Methods ---
    public static void info(String message) {
        LOGGER.info("INFO: " + message);
    }


    public static void warn(String message) {
        LOGGER.warning(message);
    }

    public static void warn(String message, Throwable t) {
        LOGGER.log(Level.WARNING, message, t);
    }

    public static void error(String message) {
        LOGGER.severe(message);
    }

    public static void error(String message, Throwable t) {
        LOGGER.log(Level.SEVERE, message, t);
    }

    public static void debug(String message) {
        LOGGER.fine(message);
    }

    public static void config(String message) {
        LOGGER.config(message);
    }
}
