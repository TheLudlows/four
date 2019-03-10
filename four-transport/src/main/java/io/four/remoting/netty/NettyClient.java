package io.four.remoting.netty;

import io.four.registry.config.Host;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import static io.four.platformutils.PlatformUtils.AVAILABLE_PROCESSORS;
import static io.four.platformutils.PlatformUtils.SUPPORT_EPOLL;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class NettyClient {

    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private ChannelHandler handler;

    public NettyClient(ChannelHandler handler) {
        this.handler = handler;
        init();
    }

    public void init() {
        eventLoopGroup = SUPPORT_EPOLL ?
                new EpollEventLoopGroup(AVAILABLE_PROCESSORS) : new NioEventLoopGroup(AVAILABLE_PROCESSORS);

        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_RCVBUF, 256 * 1024)
                .option(ChannelOption.SO_SNDBUF, 256 * 1024)
                .option(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(1024 * 1024, 2048 * 1024));

        if (SUPPORT_EPOLL) {
            bootstrap.option(EpollChannelOption.SO_REUSEPORT, true);
            bootstrap.channel(EpollSocketChannel.class);
        } else {
            bootstrap.channel(NioSocketChannel.class);
        }

        bootstrap.handler(handler);
    }

    public void close(){
    }

    public Channel connect(Host host) {
        return doConnect(host);
    }

    private Channel doConnect(Host host) {
        InetSocketAddress isa = new InetSocketAddress(host.getIp(), host.getPort());
        Channel channel = null;

        synchronized (this) {

            try {
                channel = this.bootstrap.connect(isa).sync().channel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return channel;
        }
    }
}
