package io.four.registry;

/**
 * @author TheLudlows
 */
public interface Discover {
    /**
     * server discover
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
