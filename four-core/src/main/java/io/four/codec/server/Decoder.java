package io.four.codec.server;


import io.four.protocol.four.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class Decoder extends LengthFieldBasedFrameDecoder {


    public Decoder() {
        super(Integer.MAX_VALUE, 0x4, 0x4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        ByteBuf buf = (ByteBuf) super.decode(ctx, in);
        if (buf != null) {
            try {
                return MessageUtil.toRequest(buf);
            } finally {
                buf.release();
            }
        }
        return null;
    }
}
