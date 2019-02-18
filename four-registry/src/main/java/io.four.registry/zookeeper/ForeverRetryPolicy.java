package io.four.registry.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.RetrySleeper;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author TheLudlows
 */
public class ForeverRetryPolicy implements RetryPolicy {

    private static final int BASE_SLEEP_TIME = 2000;
    private static final int MAX_SLEEP_TIME = 10000;

    private int baseSleepTime;
    private int maxSleepTime;

    public ForeverRetryPolicy() {
        this(BASE_SLEEP_TIME, MAX_SLEEP_TIME);
    }

    public ForeverRetryPolicy(int baseSleepTime, int maxSleepTime) {
        this.baseSleepTime = Optional.ofNullable(baseSleepTime).orElse(BASE_SLEEP_TIME);
        this.maxSleepTime = Optional.ofNullable(maxSleepTime).orElse(MAX_SLEEP_TIME);
    }

    @Override
    public boolean allowRetry(int retryCount, long elapsedTimeMs, RetrySleeper sleeper) {
        try {
            sleeper.sleepFor(getSleepTimeMs(retryCount, elapsedTimeMs), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        return true;
    }

    private long getSleepTimeMs(int retryCount, long elapsedTimeMs) {
        if (retryCount < 0) {
            return maxSleepTime;
        }

        long sleepMs = baseSleepTime * (retryCount + 1);

        if (sleepMs > maxSleepTime || sleepMs <= 0) {
            sleepMs = maxSleepTime;
        }

        return sleepMs;
    }

}
