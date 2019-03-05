package io.four.codec.client;

import io.four.log.Log;
import io.four.protocol.four.Request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class Encoder extends MessageToByteEncoder<Request> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Request request, ByteBuf byteBuf) throws Exception {
        request.toByteBuf(byteBuf);
        Log.info("Write to bytebuf:"+request.toString());
    }
}
