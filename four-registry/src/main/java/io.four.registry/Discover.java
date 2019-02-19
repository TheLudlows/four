package io.four.registry;

import io.four.registry.config.HostWithWeight;

import java.util.List;

/**
 * @author TheLudlows
 */
public interface Discover {
    /**
     * server discover
     * @param serviceName
     * @return
     */
    List<HostWithWeight> discover(String serviceName);
}
