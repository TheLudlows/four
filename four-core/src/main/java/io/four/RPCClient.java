package io.four;

import io.four.config.BaseConfig;
import io.four.log.Log;
import io.four.protocol.four.Request;
import io.four.proxy.DefaultProxyFactory;
import io.four.registry.config.Host;
import io.four.registry.zookeeper.ZookeeperCenter;
import io.four.registry.zookeeper.ZookeeperDiscover;
import io.four.remoting.netty.NettyClient;
import io.four.rpcHandler.ClientChannelInitializer;
import io.netty.channel.Channel;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static io.four.InvokeFuturePool.poolMap;

public class RPCClient {
    private static final int CHANNEL_NUM = 4;

    public static RPCClient RPCCLIENT = new RPCClient("localhost:2181");

    private NettyClient nettyClient = new NettyClient(new ClientChannelInitializer());
    private ConcurrentHashMap<Host, Connection> connectMap = new ConcurrentHashMap();
    private boolean start = false;

    private RPCClient(String registerAddress) {
        ZookeeperCenter.DISCOVER = new ZookeeperDiscover(registerAddress);
    }

    public synchronized void start() {
        if (start) {
            return;
        }
        start = true;
        ZookeeperCenter.startDiscover();
        nettyClient.init();
    }


    public CompletableFuture send(Request request, Host host) {
        Connection connection = connectMap.get(host);
        if (connection == null) {
            synchronized (RPCClient.class) {
                connection = connectMap.get(host);
                if (connection == null) {
                    connection = doConnection(host);
                }
            }
        }
        InvokeFuture future = new InvokeFuture();
        connection.send(request.setFuture(future));
        return future;
    }

    public <T> T getProxy(Class clazz, BaseConfig config) throws Exception {
        String serviceName = config.getAlias() + "/" + clazz.getCanonicalName();
        List<Host> list = ZookeeperCenter.discover(serviceName);
        for (Host host : list) {
            doConnection(host);
        }
        return DefaultProxyFactory.getProxy(clazz, config);
    }

    private Connection doConnection(Host host) {
        Channel[] channels = new Channel[CHANNEL_NUM];
        for (int i = 0; i < CHANNEL_NUM; i++)
            channels[i] = nettyClient.connect(host);
        Connection connection = new Connection(channels);
        connectMap.put(host, connection);
        return connection;
    }

    /**
     * connect to all provider
     */
    public void close() {
        connectMap.forEach((key, connection) -> {
            try {
                connection.close();
            } catch (Exception e) {
                Log.warn("Close RPC client failed", e);
            }
        });

        poolMap.forEach((key, pool) -> {
            try {
                pool.close();
            } catch (Exception e) {
                Log.warn("Close wait pool failed", e);
            }
        });

        poolMap.clear();
        nettyClient.close();
        ZookeeperCenter.DISCOVER.close();
    }

}
