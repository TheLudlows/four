package io.four;

import io.four.registry.config.Host;
import io.four.registry.config.HostWithWeight;
import io.four.registry.zookeeper.ZookeeperDiscover;
import io.four.registry.zookeeper.ZookeeperRegister;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author TheLudlows
 */
public class TestZK {

    @Test
    public void client() {
        String zkAddress = "localhost:2181";
        ZookeeperDiscover discover = new ZookeeperDiscover(zkAddress);
        long start = System.currentTimeMillis();
        List<Host> list = discover.discover("service00001");
        //System.out.println(list.get(0));
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void server() {
        String zkAddress = "localhost:2181";
        ZookeeperRegister zookeeperRegister = new ZookeeperRegister(zkAddress);
        zookeeperRegister.register("service00001",
                new HostWithWeight("localhost", 10000, 1));
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
