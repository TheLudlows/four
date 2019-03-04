package io.four.remoting.netty;

import io.four.log.Log;
import io.four.protocol.four.Request;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class ServerHandler extends SimpleChannelInboundHandler<Request> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        Log.info("received from " + ctx.channel().remoteAddress() + "data:" + request.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.info(ctx.channel().remoteAddress() + "客户端已连接");
    }
}
