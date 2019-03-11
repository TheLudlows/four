package io.four.rpcHandler;

import io.four.InvokeFuturePool;
import io.four.codec.client.Decoder;
import io.four.codec.client.Encoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

import static io.four.InvokeFuturePool.poolMap;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class ClientChannelInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) {
        InvokeFuturePool pool = new InvokeFuturePool();
        poolMap.put(ch, pool);
        ch.pipeline().addLast(new Encoder(pool));
        ch.pipeline().addLast(new Decoder());
        ch.pipeline().addLast(new ClientHandler(pool));
    }


}
