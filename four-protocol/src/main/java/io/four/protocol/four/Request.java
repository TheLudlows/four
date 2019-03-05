package io.four.protocol.four;


import io.netty.buffer.ByteBuf;
import io.netty.util.Recycler;

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

    private Recycler.Handle<Request> handle;

    public void recycle() {
        args = null;
        serviceName = null;
        handle.recycle(this);
    }

    protected Request(Recycler.Handle handle) {
        this.handle = handle;
    }


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

    public static Request request(ByteBuf buf, Request request) {
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
        byteBuf.writeBytes(SERVERNAME_BYTES, 0, SERVERNAME_LENGTH - bytes.length);
        serialize.objectToByteBuf(args, byteBuf);
    }

    @Override
    public String toString() {
        return "Request{" + super.toString() +
                ",requestId=" + requestId +
                ", serviceName='" + serviceName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", timestamp=" + timestamp +
                '}';
    }
}
