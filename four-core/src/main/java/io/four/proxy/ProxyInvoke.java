package io.four.proxy;

import io.four.InvokeChain.InvokeChain;
import io.four.RPCClient;
import io.four.config.BaseConfig;

import io.four.exception.NoAliveProviderException;
import io.four.protocol.four.MessageUtil;
import io.four.protocol.four.Request;
import io.four.registry.config.Host;

/**
 * ProxyInvoke is used to invoke rpc client witch will call NettyClient send
 * we want do load balance flow control in this model
 *
 * @author TheLudlows
 */

public class ProxyInvoke {
    private  InvokeChain invokeChain;
    private final LoadBalance loadBalance;
    private final BaseConfig baseConfig;

    public Object invoke(String serviceName, Object[] params) throws NoAliveProviderException {
        // recycle
        Request request = MessageUtil.getRequest();
        request.setServiceName(serviceName)
                .setArgs(params);
        Host host;
        if (loadBalance == null) {
            throw new NoAliveProviderException("No alive provider for" + serviceName);
        } else {
            if ((host = loadBalance.next()) == null) {
                for (int i = 0; i < loadBalance.hostsSize() - 1; i++) {
                    host = loadBalance.removeAndNext();
                }
                throw new NoAliveProviderException("No alive provider for" + serviceName);
            }
        }
        // FilterChain.invoke
        return RPCClient.send(request, this);
    }

    protected ProxyInvoke(String serviceName, BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
        loadBalance = new DefaultLoadBalance(baseConfig.getAlias() + "/" + serviceName);
    }

    public InvokeChain getInvokeChain() {
        return invokeChain;
    }

    public LoadBalance getLoadBalance() {
        return loadBalance;
    }

    public BaseConfig getBaseConfig() {
        return baseConfig;
    }
}
