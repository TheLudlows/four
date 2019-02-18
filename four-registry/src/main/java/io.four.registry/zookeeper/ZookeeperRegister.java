package io.four.registry.zookeeper;

import io.four.registry.Register;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;

/**
 * @author TheLudlows
 */
public class ZookeeperRegister implements Register {
    private CuratorFramework curator;
    private String connectString = "";

    @Override
    public void initAndStart() {
        curator = CuratorFrameworkFactory.newClient(connectString, 1000 * 10, 1000 * 3, new ForeverRetryPolicy());
        curator.start();
    }

    @Override
    public void register(String address, String serverName, int weight) {

    }

    @Override
    public void unRegister(String address, String app, int weight) {

    }
}
