package io.four.protocol.four;

import io.four.protocol.body.RequestBody;

import java.util.concurrent.atomic.AtomicLong;

import static io.four.protocol.four.ProtocolConstant.FASTJSON;
import static io.four.protocol.four.ProtocolConstant.REQUEST;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class EntryBuilder {

    private static AtomicLong requestId = new AtomicLong(0);

    public static TransportEntry requestEntry(String serviceName, Object[] args) {
        long id = requestId.incrementAndGet();
        return new TransportEntry(REQUEST, FASTJSON, id,
                new RequestBody(id, serviceName, args));
    }

    public static TransportEntry responseEntry() {
        return null;
    }

}
