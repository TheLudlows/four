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

    public List<Host> getHostList() {
        return hostList;
    }

    public int hostsSize() {
        return hostList.size();
    }

}

/**
 * robin load balance
 */
class DefaultLoadBalance extends BaseLoadBalance {

    private AtomicInteger index = new AtomicInteger();

    public DefaultLoadBalance(String serviceName) {
        super(serviceName);
    }

    @Override
    public Host next() throws NoAliveProviderException {
        if(getHostList() == null || getHostList().size() == 0) {
            throw new NoAliveProviderException("No alive provider exception");
        }
        final int n = index.getAndIncrement();
        final int length = getHostList().size();
        return getHostList().get(n % length);
    }

    @Override
    public Host removeAndNext() throws NoAliveProviderException {
        getHostList().remove(index);
        return next();
    }
}
