package io.four;


import io.four.log.Log;
import io.four.protocol.four.Request;
import io.four.protocol.four.Response;
import io.four.proxy.LoadBalance;
import io.four.proxy.NoAliveProviderException;
import io.four.proxy.ProxyInvoke;
import io.four.registry.config.Host;
import io.four.remoting.netty.NettyClient;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;

public class RPCClient {

    private static NettyClient nettyClient = new NettyClient();

    public static void init() {
        // register init
        // discover init
        nettyClient.init();
    }

    public static CompletableFuture send(Request request, ProxyInvoke proxyInvoke) throws NoAliveProviderException {
        Channel channel = null;
        LoadBalance loadBalance = proxyInvoke.getLoadBalance();
        InvokeFuture future = new InvokeFuture();
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
        InvokeFuturePool.add(future);
        return future.setTime(request.getRequestId())
                .setTime(request.getTimestamp());

    }
}
