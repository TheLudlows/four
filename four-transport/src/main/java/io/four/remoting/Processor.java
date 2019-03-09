package io.four.remoting;

import io.four.protocol.four.Request;
import io.netty.channel.ChannelHandlerContext;

/**
 *  process the request and do some filter ops
 */
public interface Processor {

    void process(Request request, ChannelHandlerContext ctx);
}
