package io.four.rpcHandler;

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
        //Log.info("received from " + ctx.channel().remoteAddress() + " data:" + request.toString());
        processor.process(request, ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.info("Server address:"+ctx.channel().localAddress()+" client:"+ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try{
            ctx.channel().close();
            Log.info("client "+ctx.channel().remoteAddress()+" is closed");
        } catch (Exception e) {
            Log.warn("close channel failed", e);
        }

    }
}
