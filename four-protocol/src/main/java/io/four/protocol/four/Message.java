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

    // message head
    protected final short agg = AGG;
    protected byte mType;
    protected byte sType;
    protected int bodyLength;

    public BaseMessage() {
    }

    public BaseMessage(byte mType, byte sType) {
        this.mType = mType;
        this.sType = sType;
    }

    public short getAgg() {
        return agg;
    }

    public short getmType() {
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

    @Override
    public ByteBuf toByteBuf(ByteBuf buf) {
        if (buf == null) {
            throw new NullPointerException();
        }
        buf.writeShort(agg);
        buf.writeByte(mType);
        buf.writeByte(sType);
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

    @Override
    public String toString() {
        return
                "agg=" + agg +
                ", mType=" + mType +
                ", sType=" + sType +
                ", bodyLength=" + bodyLength;

    }
}
