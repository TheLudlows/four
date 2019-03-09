package io.four.registry;

import io.four.registry.config.Host;

import java.util.List;

/**
 * @author TheLudlows
 */
public interface Discover {
    /**
     * server discover
     *
     * @param serviceName
     * @return
     */
    List<Host> discover(String serviceName);

    /**
     * Start Discover
     */

    void start();
}
