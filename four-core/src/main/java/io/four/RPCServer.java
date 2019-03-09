package io.four;

import io.four.config.BaseConfig;
import io.four.invoker.InvokerFactory;
import io.four.log.Log;
import io.four.registry.config.Host;
import io.four.registry.config.HostWithWeight;
import io.four.registry.zookeeper.ZookeeperCenter;
import io.four.remoting.netty.NettyServer;
import io.four.rpcHandler.ServerChannelInitializer;
import io.netty.channel.ChannelHandler;

/**
 * RPC server
 */
public class RPCServer {
    private static ChannelHandler handler = new ServerChannelInitializer();
    private static NettyServer server = new NettyServer(handler, 7777);
    private static Host host = new HostWithWeight("localhost:7777",5);
    //start netty server
    public static void start() {
        server.start();
        ZookeeperCenter.initAndStart("localhost:2181");
    }

    public static void register(Class clazz, Object service, BaseConfig config) {
        // generate invoker
        try {
            InvokerFactory.generateInvoker(clazz,service);
            ZookeeperCenter.register(config.getAlias() + "/" + clazz.getName(), host);
        }catch (Exception e) {
            Log.warn("RPC server start error",e);
        }
    }
}
