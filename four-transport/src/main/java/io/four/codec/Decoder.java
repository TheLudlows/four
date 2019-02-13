package io.four.codec;

import io.four.exception.TransportException;
import io.four.protocol.body.Body;
import io.four.protocol.body.BufBodyUtil;
import io.four.protocol.four.TransportEntry;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class Decoder extends ByteToMessageDecoder {

    private static final int MAX_BODY_SIZE = 1024 << 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.readByte();//AGG
        byte mType = in.readByte();
        byte sType = in.readByte();
        long messageId = in.readLong();
        int size = in.readInt();
        checkBodySize(size);
        Body body = BufBodyUtil.bufToBody(in.readBytes(size), mType);
        out.add(new TransportEntry(mType, sType, size, messageId, body));
    }

    private void checkBodySize(int size) throws TransportException {
        if (size > MAX_BODY_SIZE) {
            throw new TransportException("body of request is bigger than limit value " + MAX_BODY_SIZE);
        }
    }

}
