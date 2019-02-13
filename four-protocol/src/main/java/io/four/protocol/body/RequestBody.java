package io.four.protocol.body;


import io.netty.buffer.ByteBuf;

import java.util.Arrays;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class RequestBody extends ByteBufBody {

    private long requestId;

    private long serviceId;

    private Object[] args;

    private long timestamp;

    public static Body toBody(ByteBuf buf) {
        return new RequestBody(
                buf.readLong(),
                buf.readLong(),
                buf.readLong(),
                (Object[])serialize.byteBufToObject(buf, Object[].class));
    }


    @Override
    protected void toByteBufImpl(ByteBuf byteBuf) {
        byteBuf.writeLong(requestId);
        byteBuf.writeLong(serviceId);
        byteBuf.writeLong(timestamp);
        serialize.objectToByteBuf(args, byteBuf);
    }

    public RequestBody(long requestId, long serviceId, long timestamp, Object[] args) {
        this.requestId = requestId;
        this.serviceId = serviceId;
        this.args = args;
        this.timestamp = timestamp;
    }

    public RequestBody(long requestId, long serviceId, Object[] args) {
        this.requestId = requestId;
        this.serviceId = serviceId;
        this.args = args;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "requestId=" + requestId +
                ", serviceName='" + serviceId + '\'' +
                ", args=" + Arrays.toString(args) +
                ", timestamp=" + timestamp +
                ", length=" + length +
                '}';
    }
}
