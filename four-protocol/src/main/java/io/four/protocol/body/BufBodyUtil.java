package io.four.protocol.body;

import io.four.exception.NoSuchTypeBodyException;
import io.netty.buffer.ByteBuf;

import static io.four.protocol.four.ProtocolConstant.REPONSE;
import static io.four.protocol.four.ProtocolConstant.REQUEST;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class BufBodyUtil {
    public static Body bufToBody(ByteBuf buf, byte type) throws NoSuchTypeBodyException {
        switch (type) {
            case REPONSE:
                return ResponseBody.toBody(buf, Object.class);
            case REQUEST:
                return RequestBody.toBody(buf);
            default:
                throw new NoSuchTypeBodyException("no sun type body");
        }
    }
}
