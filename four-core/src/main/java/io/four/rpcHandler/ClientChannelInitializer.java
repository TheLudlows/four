package io.four.rpcHandler;

import io.four.codec.client.Decoder;
import io.four.codec.client.Encoder;
import io.four.remoting.netty.ClientHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class ClientChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline().addLast(new Encoder());

        ch.pipeline().addLast(new Decoder());

        ch.pipeline().addLast(new ClientHandler());
    }
}