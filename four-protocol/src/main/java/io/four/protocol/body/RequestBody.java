package io.four.protocol.body;


import io.netty.buffer.ByteBuf;

import java.util.Arrays;

import static io.four.protocol.four.ProtocolConstant.SERVERNAME_LENGTH;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class RequestBody extends ByteBufBody {

    private long requestId;

    private long timestamp;

    private String serviceName;

    private Object[] args;

    public RequestBody(long requestId, long timestamp, String serviceName, Object[] args) {
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.args = args;
        this.timestamp = timestamp;
    }


    public RequestBody(long requestId, String serviceName, Object[] args) {
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.args = args;
        this.timestamp = System.currentTimeMillis();
    }

    public static Body toBody(ByteBuf buf) {
        long requestId = buf.readLong();
        long timestamp = buf.readLong();
        byte[] bytes = new byte[SERVERNAME_LENGTH];
        buf.readBytes(bytes);
        return new RequestBody(requestId, timestamp, new String(bytes),
                (Object[]) serialize.byteBufToObject(buf, Object[].class));
    }

    @Override
    protected void toByteBufImpl(ByteBuf byteBuf) {
        byteBuf.writeLong(requestId);
        byteBuf.writeLong(timestamp);
        byte[] bytes = serviceName.getBytes();
        byteBuf.writeBytes(bytes);
        for (int i = 0; i < SERVERNAME_LENGTH - bytes.length; i++) {
            byteBuf.writeByte(0);
        }
        serialize.objectToByteBuf(args, byteBuf);
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "requestId=" + requestId +
                ", serviceName='" + serviceName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", timestamp=" + timestamp +
                ", length=" + length +
                '}';
    }
}
