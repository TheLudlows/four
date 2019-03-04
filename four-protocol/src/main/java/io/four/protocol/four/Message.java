package io.four.protocol.four;

import io.four.serialization.Serialize;
import io.four.serialization.SerializerHolder;
import io.netty.buffer.ByteBuf;

import static io.four.protocol.four.ProtocolConstant.AGG;

/**
 * @author TheLudlows
 * @since 0.1
 */
public interface Message {

    ByteBuf toByteBuf(ByteBuf buf);

}

abstract class BaseMessage implements Message {

    protected static Serialize serialize = SerializerHolder.serialize();

    // head start
    protected final byte agg = AGG;
    protected byte mType;
    protected byte sType;
    protected long messageId;
    protected int bodyLength;

    public BaseMessage() {
    }

    public BaseMessage(byte mType, byte sType, long messageId) {
        this.mType = mType;
        this.sType = sType;
        this.messageId = messageId;
    }

    public byte getAgg() {
        return agg;
    }

    public byte getmType() {
        return mType;
    }

    public BaseMessage setmType(byte mType) {
        this.mType = mType;
        return this;
    }

    public byte getsType() {
        return sType;
    }

    public BaseMessage setsType(byte sType) {
        this.sType = sType;
        return this;

    }

    public int getBodyLength() {
        return bodyLength;
    }

    public BaseMessage setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
        return this;

    }

    public long getMessageId() {
        return messageId;
    }

    public BaseMessage setMessageId(long messageId) {
        this.messageId = messageId;
        return this;
    }

    @Override
    public ByteBuf toByteBuf(ByteBuf buf) {
        if (buf == null) {
            throw new NullPointerException();
        }
        buf.writeByte(agg);
        buf.writeByte(mType);
        buf.writeByte(sType);
        buf.writeLong(messageId);
        int index = buf.writerIndex();
        buf.writeInt(1);
        int start = buf.writerIndex();
        toByteBufImpl(buf);
        int length = buf.writerIndex() - start;
        buf.setInt(index, length);
        return buf;
    }

    /**
     * write date to netty
     * @param byteBuf  {@link ByteBuf}
     */
    protected abstract void toByteBufImpl(ByteBuf byteBuf);
}
