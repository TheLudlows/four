package io.four.registry;

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
    void register(String address,String serverName,int weight);

    /**
     * unRegister service
     */
    void unRegister(String address,String app,int weight);
}
