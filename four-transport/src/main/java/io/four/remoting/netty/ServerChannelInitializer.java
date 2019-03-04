package io.four.remoting.netty;

import io.four.codec.server.Decoder;
import io.four.codec.server.Encoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class ServerChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline().addLast(new Encoder());

        ch.pipeline().addLast(new Decoder());

        ch.pipeline().addLast(new ServerHandler());
    }
}
