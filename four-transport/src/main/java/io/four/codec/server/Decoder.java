package io.four.codec.server;


import io.four.protocol.four.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import static io.four.protocol.four.ProtocolConstant.HEAD_LENGGTH;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class Decoder extends LengthFieldBasedFrameDecoder {


    public Decoder() {
        super(Integer.MAX_VALUE, HEAD_LENGGTH, 0x4, 0, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) {
        try {
            ByteBuf buf = (ByteBuf) super.decode(ctx, in);
            MessageUtil.toRequest(buf);
            buf.release();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
