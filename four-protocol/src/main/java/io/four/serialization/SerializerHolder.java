package io.four.serialization;


import java.util.ServiceLoader;

public class SerializerHolder {
    private static final Serialize SERIALIZER = load(Serialize.class);

    public static Serialize serialize() {
        return SERIALIZER;
    }

    private static <S> S load(Class<S> serviceClass) {
        return ServiceLoader.load(serviceClass).iterator().next();
    }
}
