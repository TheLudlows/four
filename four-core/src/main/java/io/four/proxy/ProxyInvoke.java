package io.four.proxy;

import io.four.InvokeChain.InvokeChain;
import io.four.config.BaseConfig;
import io.four.protocol.four.EntryBuilder;
import io.four.protocol.four.TransportEntry;
import io.four.registry.config.Host;

/**
 * ProxyInvoke is used to invoke rpc client witch will call NettyClient send
 * we want do load balance flow control in this model
 *
 * @author TheLudlows
 */

public class ProxyInvoke {
    private InvokeChain invokeChain;
    private LoadBalance loadBalance;
    private BaseConfig baseConfig;

    public Object invoke(String serviceName, Object[] params) throws NoAliveProviderException {
        // recycle
        TransportEntry entry = EntryBuilder.requestEntry(serviceName, params);
        if (loadBalance == null || loadBalance.next() == null) {
            throw new NoAliveProviderException("No alive provider for" + serviceName);
        }
        Host host = loadBalance.next();
        // send entry to client stub
        //RPCClient.sendRquest()
        return null;
    }

    protected ProxyInvoke(String serviceName, BaseConfig baseConfig) {
        this.baseConfig = baseConfig;
        loadBalance = new DefaultLoadBalance(baseConfig.getAlias() + "/" + serviceName);
    }
}
