package io.four.codec.client;

import io.four.InvokeFuturePool;
import io.four.log.Log;
import io.four.protocol.four.Request;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

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
    protected void encode(ChannelHandlerContext ctx, Request request, ByteBuf byteBuf) throws Exception {
        if (invokeFuturePool.add(request) == null) {
            Log.info("Pool full !" + ctx.channel().toString());
            return;
        }
        request.toByteBuf(byteBuf);
        request.recycle();
        //Log.info("Write to bytBuf:"+request.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        try {
            invokeFuturePool.close();
        } catch (Exception e) {
            Log.warn("close invokeFuturePool failed", e);
        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);

        try {
            invokeFuturePool.close();
        } catch (Exception e) {
            Log.warn("close invokeFuturePool failed", e);
        }
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        super.connect(ctx, remoteAddress, localAddress, promise);
        ctx.executor().scheduleWithFixedDelay(() -> invokeFuturePool.cleanTimeout(),
                1000, 1000, TimeUnit.MILLISECONDS);
        Log.info("start to clean timeout future");
    }
}
