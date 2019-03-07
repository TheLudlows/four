package io.four;

import java.util.concurrent.TimeUnit;

/**
 * 1000% of System.currentTimeMillis()
 *
 * But not Precision
 */
public class TimeUtil {

    private static volatile long currentTimeMillis;

    static {
        currentTimeMillis = System.currentTimeMillis();
        Thread daemon = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    currentTimeMillis = System.currentTimeMillis();
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (Throwable e) {
                        // do nothing
                    }
                }
            }
        });
        daemon.setDaemon(true);
        daemon.setName("time-thread");
        daemon.start();
    }

    public static long currentTimeMillis() {
        return currentTimeMillis;
    }
}