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
    private  ChannelHandler handler = new ServerChannelInitializer();
    private  NettyServer server;
    private  String address;
    //start netty server
    public RPCServer(String address) {
        this.address = address;
        Host host = new Host(address);
        this.server = new NettyServer(handler,host.getPort());
    }
    public  void start() {
        server.start();
        ZookeeperCenter.initAndStart("localhost:2181");
    }

    public void register(Class clazz, Object service, BaseConfig config, int weight) {
        // generate invoker
        try {
            InvokerFactory.generateInvoker(clazz,service);
            ZookeeperCenter.register(config.getAlias() + "/" + clazz.getName(), new HostWithWeight(address,weight));
        }catch (Exception e) {
            Log.warn("RPC server start error",e);
        }
    }
}
