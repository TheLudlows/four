package io.four.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.four.serialization.Serialize;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.IOException;

/**
 *  @author TheLudlows
 *  @since 0.1
 */

public class FastJSONSerialize implements Serialize {

    @Override
    public byte[] objectToByte(Object obj) {
        return JSON.toJSONBytes(obj, SerializerFeature.SortField);
    }

    @Override
    public void objectToByteBuf(Object obj, ByteBuf buf) {
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
            return JSON.parseObject(new ByteBufInputStream(buf), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
