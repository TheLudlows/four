package io.four.serialization;

import io.netty.buffer.ByteBuf;

/**
 * We suggested use DirectByteBuf
 * {@link ByteBuf}
 * {@link io.four.serialization.fastjson.FastJSONSerialize}
 */

public interface Serialize<T> {

    byte[] objectToByte(T obj);

    T byteToObject(byte[] bytes, Class<T> clazz);

    void objectToByteBuf(T obj, ByteBuf out);

    T byteBufToObject(ByteBuf buf, Class<T> clazz);

    Object byteBufToObjectSlow(ByteBuf buf, Class clazz);

    void objectToByteBufSlow(Object obj, ByteBuf buf);
}
