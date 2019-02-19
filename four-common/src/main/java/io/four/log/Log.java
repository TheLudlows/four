package io.four.log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * log utils
 */
public class Log {
    private static Logger logger = Logger.getLogger("Four");

    static {
        logger.setLevel(Level.ALL);
    }

    public static void info(String detail, Object... params) {
        logger.log(Level.INFO, detail, params);
    }

    public static void info(String detail, Throwable e) {
        logger.log(Level.INFO, detail, e);
    }

    public static void warn(String detail, Throwable e) {
        logger.log(Level.WARNING, detail, e);
    }

}
