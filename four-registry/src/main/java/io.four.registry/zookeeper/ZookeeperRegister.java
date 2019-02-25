package io.four.registry.zookeeper;

import io.four.log.Log;
import io.four.registry.Register;
import io.four.registry.config.HostWithWeight;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author TheLudlows
 */
public class ZookeeperRegister implements Register {

    private static ConcurrentMap<String, PathChildrenCache> cacheMap = new ConcurrentHashMap();
    private CuratorFramework curator;
    private String zkAddress;

    public ZookeeperRegister(String zkAddress) {
        this.zkAddress = zkAddress;
        curator = CuratorFrameworkFactory.newClient(zkAddress,
                1000 * 10, 1000 * 3, new ForeverRetryPolicy());
        curator.start();
    }

    @Override
    public void register(String serviceName, HostWithWeight host) {
        Objects.requireNonNull(serviceName);
        final String path = "/four/" + serviceName + "/" + host.toString();
        try {
            if (curator.checkExists().forPath(path) != null) {
                curator.delete().forPath(path);
                Log.info("Node exist:" + path);
            }
        } catch (Exception e) {
            Log.info("Delete Node failed", e);
        }

        try {//EPHEMERAL node
            curator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, null);
            Log.info("Register success!");
        } catch (Exception e) {
            Log.warn("Register Failed,", e);
        }
        // Broken line register again
        if (!cacheMap.containsKey(path)) {
            addRegister(serviceName, host);
        }
    }

    @Override
    public void unRegister(String address, String serviceName, HostWithWeight host) {
        final String path = "/four/" + "/" + serviceName + "/" + host.toString();
        try {
            curator.delete().forPath(path);
        } catch (Exception e) {
            Log.warn("Delete Node failed", e);
        }
    }

    private void addRegister(String serviceName, HostWithWeight host) {
        String path = "/four/" + serviceName + "/" + host.toString();
        if (cacheMap.containsKey(path)) {
            return;
        }
        synchronized (this) {
            if (cacheMap.containsKey(path)) {
                return;
            }
            PathChildrenCache watcher = new PathChildrenCache(curator, path, false);
            PathChildrenCacheListener listener = (client, event) -> {
                switch (event.getType()) {
                    case CONNECTION_RECONNECTED:
                        Log.info("Re register, " + path + ", " + host.toString());
                        register(serviceName, host);
                        break;
                    default:
                        break;
                }
            };
            watcher.getListenable().addListener(listener);
            try {
                watcher.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
                cacheMap.put(path, watcher);
            } catch (Exception e) {
                Log.warn("Add register listener failed", e);
            }
        }
    }

}
