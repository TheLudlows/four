package io.four.registry;

import io.four.registry.config.Host;

/**
 * @author TheLudlows
 */
public interface Register {
    /**
     * init and start client
     */
    void initAndStart();
    /**
     *register service to config center
     */
    void register(String address,String serverName,int weight,  Host serverAddress);

    /**
     * unRegister service
     */
    void unRegister(String address,String app,int weight);
}
