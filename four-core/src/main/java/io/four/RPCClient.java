package io.four;


import io.four.log.Log;
import io.four.protocol.four.Request;
import io.four.proxy.LoadBalance;
import io.four.exception.NoAliveProviderException;
import io.four.proxy.ProxyInvoke;
import io.four.registry.config.Host;
import io.four.remoting.netty.NettyClient;
import io.four.rpcHandler.ClientChannelInitializer;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;

public class RPCClient {



    private static NettyClient nettyClient = new NettyClient(new ClientChannelInitializer());

    public static void init() {
        // register init
        // discover init
        nettyClient.init();
    }

    public static CompletableFuture send(Request request, ProxyInvoke proxyInvoke) throws NoAliveProviderException {
        Channel channel = null;
        LoadBalance loadBalance = proxyInvoke.getLoadBalance();
        Host host;
        try {
            host = loadBalance.next();
            channel = nettyClient.connect(host);
        } catch (Exception e) {
            for (int i = 0; i < loadBalance.hostsSize(); i++) {
                host = loadBalance.next();
                try {
                    channel = nettyClient.connect(loadBalance.next());
                } catch (Exception e1) {
                    Log.info("The Provider Not alive " + host.toString());
                }
            }
        }
        assert channel != null;
        channel.writeAndFlush(request);
        return InvokeFuturePool.add(request);
    }
}
