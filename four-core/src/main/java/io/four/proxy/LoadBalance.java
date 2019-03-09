package io.four.proxy;

import io.four.exception.NoAliveProviderException;
import io.four.registry.config.Host;
import io.four.registry.zookeeper.ZookeeperCenter;

import java.util.List;

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
    protected String serviceName;
    protected BaseLoadBalance(String serviceName) {
        this.serviceName = serviceName;
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

