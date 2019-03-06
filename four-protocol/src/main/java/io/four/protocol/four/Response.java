package io.four.protocol.four;

import io.netty.buffer.ByteBuf;
import io.netty.util.Recycler;

import static io.four.protocol.four.ProtocolConstant.FASTJSON;
import static io.four.protocol.four.ProtocolConstant.REQUEST;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class Response extends BaseMessage {

    private byte status;

    private long requestId;

    private Object serviceResult;

    private transient Recycler.Handle handle;

    protected Response(Recycler.Handle handle) {
        this.handle = handle;
    }

    public void recycle() {
        serviceResult = null;
        handle.recycle(this);
    }

    public Response() {
    }

    public Response(byte status, long requestId, Object serviceResult) {
        super(REQUEST, FASTJSON, requestId);
        this.status = status;
        this.requestId = requestId;
        this.serviceResult = serviceResult;
    }

    public static Response response(ByteBuf buf, Response response) {
        if (buf == null) {
            throw new NullPointerException();
        }
        return response.setStatus(buf.readByte())
        .setRequestId(buf.readLong())
        .setServiceResult(serialize.byteBufToObject(buf, Object.class));
    }

    @Override
    protected void toByteBufImpl(ByteBuf byteBuf) {
        byteBuf.writeByte(status);
        byteBuf.writeLong(requestId);
        serialize.objectToByteBuf(serviceResult, byteBuf);
    }

    public byte getStatus() {
        return status;
    }

    public Response setStatus(byte status) {
        this.status = status;
        return this;
    }

    public long getRequestId() {
        return requestId;
    }

    public Response setRequestId(long requestId) {
        this.requestId = requestId;
        return this;
    }

    public Object getServiceResult() {
        return serviceResult;
    }

    public Response setServiceResult(Object serviceResult) {
        this.serviceResult = serviceResult;
        return this;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", requestId=" + requestId +
                ", serviceResult=" + serviceResult +
                '}';
    }
}
