package io.four.codec;

import io.four.protocol.four.TransportEntry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: TheLudlows
 * @since 0.1
 */
public class Encoder extends MessageToByteEncoder<TransportEntry> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TransportEntry entry, ByteBuf byteBuf) throws Exception {
        entry.toByteBuf(byteBuf);
    }
}
