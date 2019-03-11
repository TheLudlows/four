package io.four.proxy;

import io.four.exception.NoAliveProviderException;
import io.four.registry.config.Host;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * robin load balance
 */

public class DefaultLoadBalance extends BaseLoadBalance {


    private AtomicInteger index = new AtomicInteger();
    protected DefaultLoadBalance(String serviceName) {
        super(serviceName);
    }

    public DefaultLoadBalance(List list) {
        super(list);
    }

    @Override
    public Host next() throws NoAliveProviderException {
        if (getHostList() == null || getHostList().size() == 0) {
            throw new NoAliveProviderException("No alive provider for" + serviceName);
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

