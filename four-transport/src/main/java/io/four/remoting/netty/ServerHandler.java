package io.four.remoting.netty;

import io.four.log.Log;
import io.four.protocol.four.Request;
import io.four.remoting.Processor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class ServerHandler extends SimpleChannelInboundHandler<Request> {

    private Processor processor;

    public ServerHandler(Processor processor) {
        this.processor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        Log.info("received from " + ctx.channel().remoteAddress() + " data:" + request.toString());

        ctx.writeAndFlush(processor.process(request));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.info(ctx.channel().remoteAddress() + "客户端已连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Log.warn("Exception caught on " + ctx.channel(), cause);
        ctx.channel().close();
    }
}
