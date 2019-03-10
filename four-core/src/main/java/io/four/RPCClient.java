package io.four;


import io.four.config.BaseConfig;
import io.four.log.Log;
import io.four.protocol.four.Request;
import io.four.proxy.DefaultProxyFactory;
import io.four.registry.config.Host;
import io.four.registry.zookeeper.ZookeeperCenter;
import io.four.remoting.netty.NettyClient;
import io.four.rpcHandler.ClientChannelInitializer;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RPCClient {

    private static NettyClient nettyClient = new NettyClient(new ClientChannelInitializer());
    private static ConcurrentHashMap<Host, Connection> connectMap = new ConcurrentHashMap();

    public static void init() {
        // register init
        ZookeeperCenter.initAndStart("localhost:2181");
        nettyClient.init();
    }

    public static CompletableFuture send(Request request, Host host) {
        Connection connection = connectMap.get(host);
        synchronized (RPCClient.class) {
            connection = connectMap.get(host);
            if(connection ==null) {
                Channel channel = nettyClient.connect(host);
                connection = new Connection(channel);
                connectMap.put(host, connection);
            }
        }
        InvokeFuture future = new InvokeFuture();
        connection.send(request.setFuture(future));
        return future;
    }

    public static <T> T getProxy(Class clazz, BaseConfig config) throws Exception {
        return DefaultProxyFactory.getProxy(clazz,config);
    }

    /**
     * connect to all provider
     */
    public static void close() {
        connectMap.forEach((key, connection) -> {
            try {
                connection.close();
            } catch (Exception e) {
                Log.warn("Close RPC client Failed",e);
            }
        });
    }

}
