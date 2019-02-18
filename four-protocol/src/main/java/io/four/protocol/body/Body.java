package io.four.protocol.body;

import io.four.serialization.Serialize;
import io.four.serialization.SerializerHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @author TheLudlows
 * @since 0.1
 */
public interface Body {

    int bodyLength();

    ByteBuf toByteBuf(ByteBuf buf);

    ByteBuf toByteBuf();


}

abstract class ByteBufBody implements Body {

    protected static Serialize serialize = SerializerHolder.serialize();

    public transient int length;

    @Override
    public int bodyLength() {
        return length;
    }

    @Override
    public ByteBuf toByteBuf(ByteBuf buf) {
        if (buf == null) {
            throw new NullPointerException();
        }
        int start = buf.writerIndex();
        toByteBufImpl(buf);
        length = buf.writerIndex() - start;
        return buf;
    }

    @Override
    public ByteBuf toByteBuf() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        toByteBufImpl(buf);
        length = buf.readableBytes();
        return buf;
    }



    protected abstract void toByteBufImpl(ByteBuf byteBuf);
}
