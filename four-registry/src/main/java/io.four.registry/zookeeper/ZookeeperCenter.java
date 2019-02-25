package io.four.registry.zookeeper;

import io.four.registry.Discover;
import io.four.registry.Register;

/**
 * @author TheLudlows
 */

public class ZookeeperCenter {
    public static final Discover DISCOVER = new ZookeeperDiscover("localhost");
    public static final Register REGISTER = new ZookeeperRegister("localhost");

}
