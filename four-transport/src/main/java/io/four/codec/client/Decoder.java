package io.four.codec.client;

import io.four.log.Log;
import io.four.protocol.four.MessageUtil;
import io.four.protocol.four.Response;
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
        super(Integer.MAX_VALUE, HEAD_LENGGTH, 0x4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = (ByteBuf) super.decode(ctx, in);
        Response response = MessageUtil.toResponse(buf);
        Log.info(response.toString());
        return response;
    }
}
