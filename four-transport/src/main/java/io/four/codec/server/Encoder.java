package io.four.codec.server;

import io.four.protocol.four.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class Encoder extends MessageToByteEncoder<Response> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Response response, ByteBuf byteBuf) throws Exception {
        response.toByteBuf(byteBuf);
    }
}
