package io.four.remoting.netty;

import io.four.log.Log;
import io.four.protocol.four.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class ClientHandler extends SimpleChannelInboundHandler<Response> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response entry) throws Exception {
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
