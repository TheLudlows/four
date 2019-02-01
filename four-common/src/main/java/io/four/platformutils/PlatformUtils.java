package io.four.platformutils;

/**
 * @author: TheLudlows
 * @since 0.1
 */
public class PlatformUtils {

    public static final boolean SUPPORT_EPOLL;

    public static final int AVAILABLE_PROCESSORS;

    static {
        SUPPORT_EPOLL = System.getProperties().getProperty("os.name").startsWith("windows");
        AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    }
}
