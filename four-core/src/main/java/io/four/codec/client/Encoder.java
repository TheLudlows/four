package io.four.codec.client;

import io.four.InvokeFuturePool;
import io.four.protocol.four.Request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class Encoder extends MessageToByteEncoder<Request> {

    private InvokeFuturePool invokeFuturePool;

    public Encoder(InvokeFuturePool invokeFuturePool) {
        this.invokeFuturePool = invokeFuturePool;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Request request, ByteBuf byteBuf) throws Exception {
        request.toByteBuf(byteBuf);
        invokeFuturePool.add(request);
        request.recycle();
        //Log.info("Write to bytBuf:"+request.toString());
    }
}
