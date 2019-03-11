package io.four.registry.zookeeper;

import io.four.registry.Discover;
import io.four.registry.Register;
import io.four.registry.config.Host;

import java.util.List;

/**
 * @author TheLudlows
 */

public class ZookeeperCenter {

    public static Discover DISCOVER;
    public static Register REGISTER;
    private static boolean registerStart = false;
    private static boolean discoverStart = false;


    public static synchronized void startRegister() {
        if(registerStart) {
            throw new RuntimeException("Register center Start already ");
        }
        registerStart = true;
        REGISTER.start();
    }

    public static synchronized void startDiscover() {
        if(discoverStart) {
            throw new RuntimeException("Discover center Start already ");
        }
        discoverStart = true;
        DISCOVER.start();
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
