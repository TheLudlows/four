package io.four.registry;

import io.four.registry.config.HostWithWeight;

/**
 * @author TheLudlows
 */
public interface Register {

    /**
     * register service to config center
     */
    void register(String serverName, HostWithWeight host);

    /**
     * unRegister service
     */
    void unRegister(String address, String app, HostWithWeight host);
}
