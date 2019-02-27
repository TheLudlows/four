package io.four.codec;

import io.four.protocol.four.EntryBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class Decoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        out.add(EntryBuilder.bufToEntry(in));
    }
}
