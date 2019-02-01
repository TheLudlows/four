package io.four.remoting.netty;

import io.four.protocol.four.TransportEntry;
import io.four.remoting.Remoting;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import static io.four.platformutils.PlatformUtils.AVAILABLE_PROCESSORS;
import static io.four.platformutils.PlatformUtils.SUPPORT_EPOLL;

/**
 * @author: TheLudlows
 * @since 0.1
 */
public class NettyClient implements Remoting {

    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;

    public final static ConcurrentHashMap<String /* addr */, Channel> CHANNELS = new ConcurrentHashMap();

    @Override
    public void start() {
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

        bootstrap.handler(new ClientChannelInitializer());
    }

    @Override
    public void close() {

    }

    public Channel connect(String address) throws InterruptedException {
        if (!channelExist(address)) {
            doConnect(address);
        }
        return CHANNELS.get(address);
    }

    private void doConnect(String address) throws InterruptedException {
        String[] s = address.split(":");
        InetSocketAddress isa = new InetSocketAddress(s[0], Integer.valueOf(s[1]));
        Channel channel = null;

        synchronized (this) {
            if (channelExist(address)) {
                return;
            }
            try {
                channel = this.bootstrap.connect(isa).sync().channel();
            } catch (Exception e) {
                e.printStackTrace();
            }
            CHANNELS.put(address, channel);
        }
    }

    boolean channelExist(String address) {
        Channel channel = CHANNELS.get(address);
        if (channel != null) {
            if (!channel.isActive()) {
                CHANNELS.remove(address);
                channel.close();
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public ChannelFuture send(TransportEntry entry, String address) throws InterruptedException {
        Channel channel = connect(address);
        return channel.writeAndFlush(entry);
    }
}
