package io.four.protocol.four;

import io.four.protocol.body.Body;
import io.netty.buffer.ByteBuf;

import static io.four.protocol.four.ProtocolConstant.AGG;

/**
 * @author: TheLudlows
 * @since 0.1
 */
public class TransportEntry {

    private final byte agg = AGG;

    private byte mType;

    private byte sType;

    private long messageId;

    private int size;

    Body body;

    public TransportEntry(byte mType, byte sType, int size, long messageId, Body body) {
        this.mType = mType;
        this.sType = sType;
        this.size = size;
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

    public int getSize() {
        return size;
    }

    public TransportEntry setSize(int size) {
        this.size = size;
        return this;
    }

    public void toByteBuf(ByteBuf buf) {
        buf.writeByte(agg);
        buf.writeByte(mType);
        buf.writeByte(sType);
        buf.writeLong(messageId);
        buf.writeInt(size);
        body.toByteBuf(buf);
    }
}
