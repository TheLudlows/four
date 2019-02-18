package io.four.log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * log utils
 */
public class Log {
    private static Logger logger = Logger.getLogger("four  log");

    public static void info(String detail, Object... params) {
        log(logger, null, Level.INFO, detail, params);
    }

    public static void info(String detail, Throwable e) {
        log(logger, null, Level.INFO, detail, e);
    }

    public static void warn(String detail, Object... params) {
        log(logger, null, Level.WARNING, detail, params);
    }

    public static void warn(String detail, Throwable e) {
        log(logger, null, Level.WARNING, detail, e);
    }

    protected static void log(Logger logger, Handler handler, Level level, String detail, Object... params) {
        if (detail == null) {
            return;
        }
        if (params.length == 0) {
            logger.log(level, detail);
        } else {
            logger.log(level, detail, params);
        }
    }

}
