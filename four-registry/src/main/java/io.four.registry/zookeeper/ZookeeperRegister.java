package io.four.registry.zookeeper;

import io.four.hex.HexUtils;
import io.four.log.Log;
import io.four.registry.Register;
import io.four.registry.config.Host;
import io.four.registry.config.HostWithWeight;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
    public void register(String address, String serviceName, int weight, Host serverAddress) {
        Objects.requireNonNull(serviceName);

        final String path = "/four/" + "/" + serviceName + "/" +
                HexUtils.toHex(serverAddress.toString().getBytes(StandardCharsets.UTF_8));
        byte[] data = new HostWithWeight(serverAddress, weight).toString().getBytes();

        try {
            if (curator.checkExists().forPath(path) != null) {
                Log.info("Node exist: " + path + ", " + serverAddress);
                curator.delete().forPath(path);
            }
        } catch (Exception e) {
            Log.warn("Delete Node error, " + path + ", " + serverAddress, e);

        }

        try {
            curator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, data);

            Log.info("Register success, " + path + ", " + serverAddress + "@" + weight);
        } catch (Exception e) {
            Log.warn("Register failed, " + path + ", " + serverAddress + "@" + weight, e);
        }

        // 断线重连
    }

    @Override
    public void unRegister(String address, String app, int weight) {

    }

}
