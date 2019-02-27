package io.four.proxy;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.four.registry.zookeeper.ZookeeperCenter.DISCOVER;

/**
 * load balance
 */
public interface LoadBalance {

    int next();
}

abstract class BaseLoadBalance implements LoadBalance {

    protected String serviceName;
    protected List hostList;

    protected BaseLoadBalance(String serviceName) {
        this.serviceName = serviceName;
        this.hostList = DISCOVER.discover(serviceName);
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
    public int next() {
        final int n = index.getAndIncrement();
        final int length = hostList.size();
        return n % length;
    }
}
