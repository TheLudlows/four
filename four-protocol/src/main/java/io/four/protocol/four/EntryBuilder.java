package io.four.protocol.four;

import io.four.exception.TransportException;
import io.four.protocol.body.Body;
import io.four.protocol.body.BufBodyUtil;
import io.four.protocol.body.RequestBody;
import io.four.serialization.Serialize;
import io.four.serialization.SerializerHolder;
import io.four.serialization.fastjson.FastJSONSerialize;
import io.four.serialization.kryo.KryoSerialize;
import io.netty.buffer.ByteBuf;

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

    public static TransportEntry requestEntry(String serviceName, Object[] args) {
        long id = REQUEST_ADDER.getAndIncrement();

        return new TransportEntry(REQUEST, serializeType, id,
                new RequestBody(id, serviceName, args));
    }

    public static TransportEntry responseEntry() {
        return null;
    }

    public static TransportEntry bufToEntry(ByteBuf buf) throws TransportException {
        buf.readByte();//AGG
        byte mType = buf.readByte();
        byte sType = buf.readByte();
        long messageId = buf.readLong();
        int size = buf.readInt();
        checkBodySize(size);
        Body body = BufBodyUtil.bufToBody(buf.readBytes(size), mType);
       return new TransportEntry(mType, sType, size, messageId, body);
    }

    private static void checkBodySize(int size) throws TransportException {
        if (size > MAX_BODY_SIZE) {
            throw new TransportException("body of request is bigger than limit value " + MAX_BODY_SIZE);
        }
    }

    private EntryBuilder() {
    }

}
