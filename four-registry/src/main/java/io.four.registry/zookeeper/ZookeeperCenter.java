package io.four.registry.zookeeper;

import io.four.registry.Discover;
import io.four.registry.Register;
import io.four.registry.config.Host;

import java.util.List;

/**
 * @author TheLudlows
 */

public class ZookeeperCenter {

    private static Discover DISCOVER;
    private static Register REGISTER;
    private static boolean init = false;

    public static synchronized void initAndStart(String address) {
        if(init) {
            return;
        }
        DISCOVER = new ZookeeperDiscover(address);
        REGISTER = new ZookeeperRegister(address);
        DISCOVER.start();
        REGISTER.start();
    }

    public static List<Host> discover(String serverName) {
        if(DISCOVER == null) {
            throw new RuntimeException("Not init Zookeeper center first");
        }
        return DISCOVER.discover(serverName);
    }

    public static void register(String serviceName, Host host){
        if(REGISTER == null) {
            throw new RuntimeException("Not init Zookeeper center first");
        }
        REGISTER.register(serviceName,host);
    }




}
