package io.four;

import io.four.config.BaseConfig;
import io.four.invoker.InvokerFactory;
import io.four.log.Log;
import io.four.registry.config.Host;
import io.four.registry.config.HostWithWeight;
import io.four.registry.zookeeper.ZookeeperCenter;
import io.four.registry.zookeeper.ZookeeperRegister;
import io.four.remoting.netty.NettyServer;
import io.four.rpcHandler.ServerChannelInitializer;
import io.netty.channel.ChannelHandler;

/**
 * RPC server
 */
public class RPCServer {
    private NettyServer server;
    private String address;
    private boolean start;
    //start netty server
    public RPCServer(String address, int port) {
        this.address = "localhost:" + port;
        ChannelHandler handler = new ServerChannelInitializer();
        ZookeeperCenter.REGISTER = new ZookeeperRegister(address);
        this.server = new NettyServer(handler, port);
    }

    public synchronized void start() {
        if(start) {
            return;
        }
        ZookeeperCenter.startRegister();
        server.start();
    }

    public void register(Class clazz, Object service, BaseConfig config, int weight) {
        // generate invoker
        try {
            InvokerFactory.generateInvoker(clazz, service);
            ZookeeperCenter.register(config.getAlias() + "/" + clazz.getName(), new HostWithWeight(address, weight));
        } catch (Exception e) {
            Log.warn("RPC server start error", e);
        }
    }
}