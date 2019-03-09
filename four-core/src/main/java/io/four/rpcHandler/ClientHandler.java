package io.four.rpcHandler;

import io.four.InvokeFuturePool;
import io.four.log.Log;
import io.four.protocol.four.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class ClientHandler extends SimpleChannelInboundHandler<Response> {
    private InvokeFuturePool invokeFuturePool;

    public ClientHandler(InvokeFuturePool invokeFuturePool) {
        this.invokeFuturePool = invokeFuturePool;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {
        invokeFuturePool.finish(response);
        response.recycle();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.info("error{}", cause);
    }

}
