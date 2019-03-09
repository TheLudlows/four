package io.four.protocol.four;


import io.netty.buffer.ByteBuf;
import io.netty.util.Recycler;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static io.four.protocol.four.ProtocolConstant.*;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class Request extends BaseMessage {

    private long requestId;

    private long timestamp;

    private String serviceName;

    private byte methodIndex;

    private Object[] args;

    private Recycler.Handle<Request> handle;

    private transient CompletableFuture future;

    public void recycle() {
        args = null;
        serviceName = null;
        handle.recycle(this);
    }

    protected Request(Recycler.Handle handle) {
        super(REQUEST, FASTJSON);
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

    public byte getMethodIndex() {
        return methodIndex;
    }

    public Request setMethodIndex(byte methodIndex) {
        this.methodIndex = methodIndex;
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
        super(REQUEST, FASTJSON);
    }

    public Object getFuture() {
        return future;
    }

    public Request setFuture(CompletableFuture future) {
        this.future = future;
        return this;
    }

    public static Request request(ByteBuf buf, Request request) {
        long requestId = buf.readLong();
        long timeStamp = buf.readLong();
        byte length = buf.readByte();
        byte[] serviceByte = new byte[length];
        buf.readBytes(serviceByte);
        byte methodId = buf.readByte();
        Object[] params = (Object[]) serialize.byteBufToObject(buf, Object[].class);
        return request.setRequestId(requestId)
                .setTimestamp(timeStamp)
                .setServiceName(new String(serviceByte))
                .setMethodIndex(methodId)
                .setArgs(params);
    }

    @Override
    protected void toByteBufImpl(ByteBuf byteBuf) {
        byteBuf.writeLong(requestId);
        byteBuf.writeLong(timestamp);
        byte[] bytes = serviceName.getBytes();
        byteBuf.writeByte(bytes.length);
        byteBuf.writeBytes(bytes);
        byteBuf.writeByte(methodIndex);
        serialize.objectToByteBuf(args, byteBuf);
    }

    @Override
    public String toString() {
        return "Request{" + super.toString() +
                ",requestId=" + requestId +
                ", methodIndex=" + methodIndex +
                ", serviceName='" + serviceName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", timestamp=" + timestamp +
                '}';
    }
}
