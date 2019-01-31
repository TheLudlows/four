package io.four.protocol.body;

import io.netty.buffer.ByteBuf;


/**
 * @Author: TheLudlows
 * @since 0.1
 */
public class ResponseBody extends ByteBufBody {

    private byte status;

    private long requestId;

    private Object serviceResult;

    public ResponseBody(byte status, long requestId, Object serviceResult) {
        this.status = status;
        this.requestId = requestId;
        this.serviceResult = serviceResult;
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

    public ResponseBody setStatus(byte status) {
        this.status = status;
        return this;
    }

    public long getRequestId() {
        return requestId;
    }

    public ResponseBody setRequestId(long requestId) {
        this.requestId = requestId;
        return this;
    }

    public Object getServiceResult() {
        return serviceResult;
    }

    public ResponseBody setServiceResult(Object serviceResult) {
        this.serviceResult = serviceResult;
        return this;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "status=" + status +
                ", requestId=" + requestId +
                ", serviceResult=" + serviceResult +
                '}';
    }

    public static Body toBody(ByteBuf buf, Class resultClazz) {
        if (buf == null) {
            throw new NullPointerException();
        }
        return new ResponseBody(buf.readByte(), buf.readLong(), serialize.byteBufToObject(buf, resultClazz));
    }
}
