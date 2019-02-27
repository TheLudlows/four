package io.four.protocol.four;

import io.four.protocol.body.Body;
import io.netty.buffer.ByteBuf;

import static io.four.protocol.four.ProtocolConstant.AGG;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class TransportEntry {

    private final byte agg = AGG;
    private byte mType;
    private byte sType;
    private long messageId;
    private int bodyLength;
    private Body body;


    public TransportEntry(byte mType, byte sType, long messageId, Body body) {
        this.mType = mType;
        this.sType = sType;
        this.messageId = messageId;
        this.body = body;
    }

    public TransportEntry(byte mType, byte sType, int bodyLength, long messageId, Body body) {
        this.mType = mType;
        this.sType = sType;
        this.bodyLength = bodyLength;
        this.messageId = messageId;
        this.body = body;
    }

    public byte getAgg() {
        return agg;
    }

    public byte getmType() {
        return mType;
    }

    public TransportEntry setmType(byte mType) {
        this.mType = mType;
        return this;
    }

    public byte getsType() {
        return sType;
    }

    public TransportEntry setsType(byte sType) {
        this.sType = sType;
        return this;
    }

    public long getMessageId() {
        return messageId;
    }

    public TransportEntry setMessageId(long messageId) {
        this.messageId = messageId;
        return this;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public TransportEntry setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
        return this;
    }

    public void toByteBuf(ByteBuf buf) {
        buf.writeByte(agg);
        buf.writeByte(mType);
        buf.writeByte(sType);
        buf.writeLong(messageId);
        int index = buf.writerIndex();
        buf.writeInt(1);
        body.toByteBuf(buf);
        buf.setInt(index, body.bodyLength());
    }

    public Body getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "TransportEntry{" +
                "agg=" + agg +
                ", mType=" + mType +
                ", sType=" + sType +
                ", messageId=" + messageId +
                ", size=" + bodyLength +
                ", body=" + body.toString() +
                '}';
    }
}
