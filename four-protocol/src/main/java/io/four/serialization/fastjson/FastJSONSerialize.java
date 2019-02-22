package io.four.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.four.serialization.Serialize;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

import static java.lang.ThreadLocal.withInitial;

/**
 * @author TheLudlows
 * @since 0.1
 */

public class FastJSONSerialize implements Serialize {

    private static ThreadLocal<ByteBufInputStream> INPUTSREAM_HOLDER = withInitial(ByteBufInputStream::new);
    private static ThreadLocal<ByteBufOutputStream> OUTPUTSTREAM_HOLDER = withInitial(ByteBufOutputStream::new);

    @Override
    public byte[] objectToByte(Object obj) {
        return JSON.toJSONBytes(obj, SerializerFeature.SortField);
    }

    @Override
    public void objectToByteBuf(Object obj, ByteBuf buf) {
        try {
            JSON.writeJSONString(OUTPUTSTREAM_HOLDER.get().setByteBuf(buf), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void objectToByteBufSlow(Object obj, ByteBuf buf) {
        try {
            JSON.writeJSONString(new ByteBufOutputStream(buf), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object byteToObject(byte[] bytes, Class clazz) {
        return JSON.parseObject(bytes, clazz, Feature.SortFeidFastMatch);
    }

    @Override
    public Object byteBufToObject(ByteBuf buf, Class clazz) {
        try {
            return JSON.parseObject(INPUTSREAM_HOLDER.get().setBuffer(buf), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object byteBufToObjectSlow(ByteBuf buf, Class clazz) {
        try {
            return JSON.parseObject(new ByteBufInputStream(buf), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
