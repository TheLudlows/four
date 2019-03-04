package io.four.protocol.four;

import io.four.exception.TransportException;
import io.netty.buffer.ByteBuf;

import static io.four.protocol.four.Request.request;
import static io.four.protocol.four.Response.response;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class MessageUtil {
    private static final int MAX_BODY_SIZE = 1024 << 4;

    public static Request toRequest(ByteBuf buf) throws TransportException {
        if (buf == null) {
            throw new NullPointerException();
        }
        buf.readByte();//AGG
        byte mType = buf.readByte();
        byte sType = buf.readByte();
        long messageId = buf.readLong();
        int size = buf.readInt();
        checkBodySize(size);
        Request request = new Request();
        request(buf, request);

        return (Request) request.setmType(mType)
                .setsType(sType)
                .setBodyLength(size)
                .setMessageId(messageId);

    }

    public static Response toResponse(ByteBuf buf) throws TransportException {
        if (buf == null) {
            throw new NullPointerException();
        }
        buf.readByte();//AGG
        byte mType = buf.readByte();
        byte sType = buf.readByte();
        long messageId = buf.readLong();
        int size = buf.readInt();
        checkBodySize(size);
        Response response = new Response();
        response(buf, response);
        return (Response) response.setmType(mType)
                .setsType(sType)
                .setBodyLength(size)
                .setMessageId(messageId);
    }


    private static void checkBodySize(int size) throws TransportException {
        if (size > MAX_BODY_SIZE) {
            throw new TransportException("body of request is bigger than limit value " + MAX_BODY_SIZE);
        }
    }
}
