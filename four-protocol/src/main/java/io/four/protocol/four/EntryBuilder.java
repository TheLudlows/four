package io.four.protocol.four;

import io.four.serialization.Serialize;
import io.four.serialization.SerializerHolder;
import io.four.serialization.fastjson.FastJSONSerialize;
import io.four.serialization.kryo.KryoSerialize;

import java.util.concurrent.atomic.AtomicLong;

import static io.four.protocol.four.ProtocolConstant.*;

/**
 * @author TheLudlows
 * @since 0.1
 */

public class EntryBuilder {
    private static final int MAX_BODY_SIZE = 1024 << 4;


    private static final AtomicLong REQUEST_ADDER = new AtomicLong();
    private static byte serializeType;

    static {
        Serialize serialize = SerializerHolder.serialize();
        if (serialize instanceof FastJSONSerialize) {
            serializeType = FASTJSON;
        } else if (serialize instanceof KryoSerialize) {
            serializeType = KRYO;
        } else {
            //do something
        }
    }


}
