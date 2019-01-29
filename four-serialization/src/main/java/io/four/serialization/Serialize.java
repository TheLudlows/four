package io.four.serialization;

import io.netty.buffer.ByteBuf;

/**
 * We suggested use DirectByteBuf
 * {@link ByteBuf}
 */

public interface Serialize<T> {

    byte[] objectToByte(T obj);

    T byteToObject(byte[] bytes, Class<T> clazz);

    void objectToByteBuf(T obj, ByteBuf out);


    T byteBufToObject(ByteBuf buf, Class<T> clazz);
}
