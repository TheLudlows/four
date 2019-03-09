package io.four;


import io.four.config.BaseConfig;
import io.four.protocol.four.Request;
import io.four.proxy.DefaultProxyFactory;
import io.four.registry.config.Host;
import io.four.registry.zookeeper.ZookeeperCenter;
import io.four.remoting.netty.NettyClient;
import io.four.rpcHandler.ClientChannelInitializer;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;

public class RPCClient {

    private static NettyClient nettyClient = new NettyClient(new ClientChannelInitializer());
    public static void init() {
        // register init
        ZookeeperCenter.initAndStart("localhost:2181");
        nettyClient.init();
    }

    public static CompletableFuture send(Request request, Host host) {
        Channel channel = nettyClient.connect(host);
        assert channel != null;
        InvokeFuture future = new InvokeFuture();
        channel.writeAndFlush(request.setFuture(future));
        return future;
    }

    public static <T> T getProxy(Class clazz, BaseConfig config) throws Exception {
        return DefaultProxyFactory.getProxy(clazz,config);
    }

    /**
     * connect to all provider
     */
}
