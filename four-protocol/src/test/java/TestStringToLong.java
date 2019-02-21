import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * @author TheLudlows
 */
public class TestStringToLong {
    @Test
    public void testReadString() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        byte[] bytes = "fosassssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssur".getBytes();
        buf.clear();
        buf.writeBytes(bytes);
        for (int i = 0; i < 64 - bytes.length; i++) {
            buf.writeByte(0);
        }
        byte[] bytes2 = new byte[64];
        buf.readBytes(bytes2);
        System.out.println(bytes.length);
        System.out.println(new String(bytes2));
        System.out.println(0x40 ^ 32);
    }
}
