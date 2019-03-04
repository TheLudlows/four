import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class TestProtocol {
    @Test
    public void testLength() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        //buf.writeInt(111);
    }
}
