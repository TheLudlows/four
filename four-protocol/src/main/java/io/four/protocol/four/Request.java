package io.four.protocol.four;


import io.netty.buffer.ByteBuf;

import java.util.Arrays;

import static io.four.protocol.four.ProtocolConstant.*;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class Request extends BaseMessage {

    private long requestId;

    private long timestamp;

    private String serviceName;

    private Object[] args;

    public long getRequestId() {
        return requestId;
    }

    public Request setRequestId(long requestId) {
        this.requestId = requestId;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Request setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Request setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public Object[] getArgs() {
        return args;
    }

    public Request setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    public Request() {
    }

    public Request(long requestId, long timestamp, String serviceName, Object[] args) {
        super(REQUEST, FASTJSON, requestId);
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.args = args;
        this.timestamp = timestamp;
    }


    public Request(long requestId, String serviceName, Object[] args) {
        super(REQUEST, FASTJSON, requestId);
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.args = args;
        this.timestamp = System.currentTimeMillis();
    }

    public static Message request(ByteBuf buf, Request request) {
        long requestId = buf.readLong();
        long timeStamp = buf.readLong();
        byte[] bytes = new byte[SERVERNAME_LENGTH];
        buf.readBytes(bytes);
        Object[] params = (Object[])serialize.byteBufToObject(buf, Object[].class);
        return request.setRequestId(requestId)
                .setTimestamp(timeStamp)
                .setServiceName(new String(bytes))
                .setArgs(params);
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
        return "Request{" +
                "requestId=" + requestId +
                ", serviceName='" + serviceName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", timestamp=" + timestamp +
                '}';
    }
}
