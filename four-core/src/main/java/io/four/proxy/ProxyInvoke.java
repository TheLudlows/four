package io.four.proxy;

import io.four.protocol.four.EntryBuilder;
import io.four.protocol.four.TransportEntry;
import io.four.registry.config.Host;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TheLudlows
 */

public class ProxyInvoke {

    private static Map<String, LoadBalance> loadBalanceMap = new ConcurrentHashMap();

    public Object invoke(String serviceName,Object[] params) {
        // recycle
        TransportEntry entry = EntryBuilder.requestEntry(serviceName, params);
        LoadBalance loadBalance = loadBalanceMap.get(serviceName);
        if(loadBalance == null) {
            loadBalance = new DefaultLoadBalance(serviceName);
            loadBalanceMap.put(serviceName, loadBalance);
        }
        Host host = loadBalance.next();
        // send entry to client stub≤≤
        return null;
    }
}
