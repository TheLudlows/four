package io.four.serialization.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.four.serialization.Serialize;
import io.netty.buffer.ByteBuf;


public class FastJSONSerialize implements Serialize {

    @Override
    public byte[] objectToByte(Object obj) {
        return JSON.toJSONBytes(obj, SerializerFeature.SortField);
    }

    @Override
    public void objectToByteBuf(Object obj, ByteBuf out) {

    }


    @Override
    public Object byteToObject(byte[] bytes, Class clazz) {
        return JSON.parseObject(bytes, clazz, Feature.SortFeidFastMatch);
    }

    @Override
    public Object byteBufToObject(ByteBuf buf, Class clazz) {
        return null;
    }
}
