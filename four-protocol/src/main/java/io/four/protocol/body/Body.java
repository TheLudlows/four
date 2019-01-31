package io.four.protocol.body;

import io.four.serialization.Serialize;
import io.four.serialization.SerializerHolder;
import io.netty.buffer.ByteBuf;

/**
 * @author: TheLudlows
 * @since 0.1
 */
public interface Body {

    int bodyLength();

    ByteBuf toByteBuf(ByteBuf buf);

}

abstract class ByteBufBody implements Body {
    static Serialize serialize = SerializerHolder.serialize();
    public int length;

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

    protected abstract void toByteBufImpl(ByteBuf byteBuf);
}
