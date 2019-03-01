package io.four.registry;

import io.four.registry.config.Host;

/**
 * @author TheLudlows
 */
public interface Register {

    /**
     * register service to config center
     */
    void register(String serverName, Host host);

    /**
     * unRegister service
     */
    void unRegister(String app, Host host);
}
