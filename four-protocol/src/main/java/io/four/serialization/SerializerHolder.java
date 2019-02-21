package io.four.serialization;


import io.four.serialization.fastjson.FastJSONSerialize;
import io.four.serialization.kryo.KryoSerialize;

import java.util.ServiceLoader;

public class SerializerHolder {
    private static final Serialize SERIALIZER = load(Serialize.class);
    static Serialize serialize1 = new FastJSONSerialize();
    static Serialize serialize2 = new KryoSerialize();

    public static Serialize serialize() {
        return SERIALIZER;
    }

    public static Serialize getFastJson() {
        return serialize1;
    }

    public static Serialize getKryo() {
        return serialize2;
    }

    private static <S> S load(Class<S> serviceClass) {
        return ServiceLoader.load(serviceClass).iterator().next();
    }
}
