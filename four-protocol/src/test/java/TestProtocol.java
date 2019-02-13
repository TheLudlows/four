import io.four.protocol.body.Body;
import io.four.protocol.body.ResponseBody;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import static io.four.protocol.body.ResponseEnum.SUCCESS;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class TestProtocol {
    @Test
    public void testLength() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        //buf.writeInt(111);
        Body body = new ResponseBody(SUCCESS.getCode(),1L,SUCCESS.getDesc());
        body.toByteBuf(buf);
        System.out.println(body.bodyLength());
        System.out.println(ResponseBody.toBody(buf,String.class));
    }
}
