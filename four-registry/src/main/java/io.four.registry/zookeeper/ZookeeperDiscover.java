package io.four.registry.zookeeper;

import io.four.log.Log;
import io.four.registry.Discover;
import io.four.registry.config.Host;
import io.four.registry.config.HostWithWeight;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.four.registry.config.HostWithWeight.*;
import static io.four.registry.config.RegistryConstant.*;
import static org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode.BUILD_INITIAL_CACHE;

/**
 * @author TheLudlows
 */
public class ZookeeperDiscover implements Discover {
    private static Map<String, List<HostWithWeight>> cacheMap = new ConcurrentHashMap();
    private String zkAddress;
    private CuratorFramework curator;

    public ZookeeperDiscover(String zkAddress) {
        this.zkAddress = zkAddress;
        curator = CuratorFrameworkFactory.newClient(zkAddress, 1000 * 10,
                1000 * 3, new ForeverRetryPolicy());
        curator.start();
    }

    @Override
    public List<Host> discover(String serviceName) {
        List list = cacheMap.get(serviceName);
        if (list != null && list.size() > 0) {
            return list;
        }
        final String path = RPC_CONSTANTS + DEFAULT_ALIAS + serviceName;
        try {
            List<String> listStr = curator.getChildren().forPath(path);
            list = buildHWWs(listStr);
        } catch (Exception e) {
            Log.info("Get server failed:" + path);
        }
        registerWatcher(path);
        if (list != null) {
            cacheMap.put(serviceName, list);
        }
        return list;
    }

    private void registerWatcher(String path) {
        PathChildrenCache childrenCache = new PathChildrenCache(curator, path, false);
        PathChildrenCacheListener listener = (curator, event) -> {
            Log.info("Zk Node child list changed :" + path + "," + event.getType());
            switch (event.getType()) {
                case INITIALIZED:
                    break;
                case CHILD_ADDED:
                    cacheMap.get(path)
                            .add(buildHWW(event.getData().getPath()));
                    break;
                case CHILD_REMOVED:
                    cacheMap.put(path, buildHWWs(curator.getChildren().forPath(path)));
                    break;
                default:
            }
        };

        childrenCache.getListenable().addListener(listener);
        try {
            childrenCache.start(BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            Log.info("Add Watcher Failed:" + path);
        }
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public ZookeeperDiscover setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
        return this;
    }
}
