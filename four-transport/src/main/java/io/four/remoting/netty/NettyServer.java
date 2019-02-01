package io.four.remoting.netty;

import io.four.log.Log;
import io.four.remoting.Remoting;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static io.four.PlatformUtils.PlatformUtils.AVAILABLE_PROCESSORS;
import static io.four.PlatformUtils.PlatformUtils.SUPPORT_EPOLL;

/**
 * @author: TheLudlows
 * @since 0.1
 */
public class NettyServer implements Remoting {
    private  int port = 7777;
    private  EventLoopGroup boss;
    private  EventLoopGroup worker;

    public static  boolean start = false;

    //private  ServerInvokerFactory invokerFactory;
    //private  CopyOnWriteArrayList<RpcServerFilter> filters;
    private volatile Channel channel;

    @Override
    public void start()  {
        synchronized (this) {
            if(start) {
                return;
            }
            start = true;
        }

        if(SUPPORT_EPOLL) {
            boss = new EpollEventLoopGroup(AVAILABLE_PROCESSORS);
            worker = new EpollEventLoopGroup(AVAILABLE_PROCESSORS << 1);
        } else {
            boss = new NioEventLoopGroup(AVAILABLE_PROCESSORS);
            worker = new NioEventLoopGroup(AVAILABLE_PROCESSORS << 1);
        }
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,worker)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_RCVBUF, 256 * 1024);

        if (SUPPORT_EPOLL) {
            bootstrap.option(EpollChannelOption.SO_REUSEPORT, true);
            bootstrap.channel(EpollServerSocketChannel.class);
        } else{
            bootstrap.channel(NioServerSocketChannel.class);
        }

        bootstrap.childHandler(new ServerChannelInitializer());

        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.SO_RCVBUF, 256 * 1024)
                .childOption(ChannelOption.SO_SNDBUF, 256 * 1024)
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(1024 * 1024, 2048 * 1024));
        try {
            channel = bootstrap.bind(port).sync().channel();
            Log.info("Server started. Listening on: "+ port);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            Log.warn("Server start failed!");
        }


    }

    @Override
    public void close() {
        try {
            channel.closeFuture().sync();
            Log.info("Server closed!");
        } catch (InterruptedException e) {

        }
    }
}
