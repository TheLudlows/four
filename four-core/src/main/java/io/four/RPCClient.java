package io.four;


import io.four.config.BaseConfig;
import io.four.log.Log;
import io.four.protocol.four.Request;
import io.four.proxy.DefaultProxyFactory;
import io.four.registry.Discover;
import io.four.registry.config.Host;
import io.four.registry.zookeeper.ZookeeperDiscover;
import io.four.remoting.netty.NettyClient;
import io.four.rpcHandler.ClientChannelInitializer;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static io.four.InvokeFuturePool.poolMap;

public class RPCClient {

    public static RPCClient RPCCLIENT = new RPCClient("localhost:2181");

    private NettyClient nettyClient = new NettyClient(new ClientChannelInitializer());
    private ConcurrentHashMap<Host, Connection> connectMap = new ConcurrentHashMap();
    private boolean start = false;
    private Discover discover;
    public RPCClient(String registerAddress) {
        discover = new ZookeeperDiscover(registerAddress);
    }

    public synchronized void start() {
        if(start) {
           return;
        }
        discover.start();
        nettyClient.init();
    }


    public CompletableFuture send(Request request, Host host) {
        Connection connection = connectMap.get(host);
        if( connection == null) {
            synchronized (RPCClient.class) {
                connection = connectMap.get(host);
                if (connection == null) {
                    Channel channel = nettyClient.connect(host);
                    connection = new Connection(channel,poolMap.get(channel));
                    connectMap.put(host, connection);
                }
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
    public void close() {
        connectMap.forEach((key, connection) -> {
            try {
                connection.close();
            } catch (Exception e) {
                Log.warn("Close RPC client Failed",e);
            }
        });
        poolMap.clear();
        nettyClient.close();
        discover.close();
    }

}
