package io.four.protocol.body;


import io.netty.buffer.ByteBuf;


/**
 * @author: TheLudlows
 * @since 0.1
 */
public class RequestBody extends ByteBufBody {

    private long requestId;

    private String serviceName;

    private Object[] args;

    private long timestamp;

    public static Body toBody(ByteBuf buf, Class<Object> objectClass) {
        return null;
    }


    @Override
    protected void toByteBufImpl(ByteBuf byteBuf) {
        byteBuf.writeLong(requestId);
        byteBuf.writeBytes(serviceName.getBytes());
        byteBuf.writeLong(timestamp);
        serialize.objectToByteBuf(args, byteBuf);
    }

    public RequestBody(long requestId, String serviceName, Object[] args) {
        this.requestId = requestId;
        this.serviceName = serviceName;
        this.args = args;
        this.timestamp = System.currentTimeMillis();
    }
}
