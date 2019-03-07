package io.four.protocol.four;

import io.four.exception.TransportException;
import io.netty.buffer.ByteBuf;
import io.netty.util.Recycler;

import java.util.concurrent.atomic.AtomicLong;

import static io.four.protocol.four.Request.request;
import static io.four.protocol.four.Response.response;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class MessageUtil {
    private static AtomicLong ID_ADDER = new AtomicLong();
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

    public static Request toRequest(ByteBuf buf) {
        if (buf == null) {
            throw new NullPointerException();
        }
        buf.readShort();//AGG
        byte mType = buf.readByte();
        byte sType = buf.readByte();
        int size = buf.readInt();
        Request request = request(buf, REQUEST_RECYCLE.get());
        return (Request) request.setmType(mType)
                .setsType(sType)
                .setBodyLength(size);
    }

    public static Response toResponse(ByteBuf buf) throws TransportException {
        if (buf == null) {
            throw new NullPointerException();
        }
        buf.readShort();//AGG
        byte mType = buf.readByte();
        byte sType = buf.readByte();
        int size = buf.readInt();
        Response response = response(buf, RESPONSE_RECYCLE.get());
        return (Response) response.setmType(mType)
                .setsType(sType)
                .setBodyLength(size);
    }

    public static void writeRequest(Request request, ByteBuf buf) {
        request.toByteBuf(buf);
    }

    public static void writeResponse(Response response, ByteBuf buf) {
        response.toByteBuf(buf);
    }

    public static Request getRequest() {
        return REQUEST_RECYCLE.get()
                .setRequestId(ID_ADDER.getAndIncrement());
    }

    public static Response getResponse() {
        return RESPONSE_RECYCLE.get();
    }

}
