package io.four.proxy;


import io.four.registry.config.Host;
import io.four.registry.zookeeper.ZookeeperCenter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * load balance
 */
public interface LoadBalance {

    Host next() throws NoAliveProviderException;

    Host removeAndNext() throws NoAliveProviderException;

    int hostsSize();
}

abstract class BaseLoadBalance implements LoadBalance {

    private List<Host> hostList;

    protected BaseLoadBalance(String serviceName) {
        this.hostList = ZookeeperCenter.discover(serviceName);
    }

    public BaseLoadBalance(List list) {
        this.hostList = list;
    }

    public List<Host> getHostList() {
        return hostList;
    }

    public int hostsSize() {
        return hostList.size();
    }

}

