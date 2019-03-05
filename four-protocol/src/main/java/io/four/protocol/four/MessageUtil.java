package io.four.protocol.four;

import io.four.exception.TransportException;
import io.netty.buffer.ByteBuf;
import io.netty.util.Recycler;

import static io.four.protocol.four.Request.request;
import static io.four.protocol.four.Response.response;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class MessageUtil {

    private static final int MAX_MESSAGE_SIZE = Integer.MAX_VALUE;

    private static final Recycler<Request> REQUEST_RECYCLE = new Recycler<Request>() {
        @Override
        protected Request newObject(Handle<Request> handle) {
            return new Request(handle);
        }
    };

    private static final Recycler<Response> RESPONSE_RECYCLE = new Recycler<Response>() {
        @Override
        protected Response newObject(Handle<Response> handle) {
            return new Response(handle);
        }
    };

    public static Request toRequest(ByteBuf buf) throws TransportException {
        if (buf == null) {
            throw new NullPointerException();
        }
        buf.readShort();//AGG
        byte mType = buf.readByte();
        byte sType = buf.readByte();
        long messageId = buf.readLong();
        int size = buf.readInt();
        Request request = request(buf, REQUEST_RECYCLE.get());
        return (Request) request.setmType(mType)
                .setsType(sType)
                .setBodyLength(size)
                .setMessageId(messageId);
    }

    public static Response toResponse(ByteBuf buf) throws TransportException {
        if (buf == null) {
            throw new NullPointerException();
        }
        buf.readShort();//AGG
        byte mType = buf.readByte();
        byte sType = buf.readByte();
        long messageId = buf.readLong();
        int size = buf.readInt();
        Response response = response(buf, RESPONSE_RECYCLE.get());
        return (Response) response.setmType(mType)
                .setsType(sType)
                .setBodyLength(size)
                .setMessageId(messageId);
    }


    public static void writeRequest(Request request, ByteBuf buf) {
        request.toByteBuf(buf);
    }

    public static void writeResponse(Response response, ByteBuf buf) {
        response.toByteBuf(buf);
    }

    public static Request getRequest() {
        return REQUEST_RECYCLE.get();
    }

    public static Response getResponse() {
        return RESPONSE_RECYCLE.get();
    }

}
