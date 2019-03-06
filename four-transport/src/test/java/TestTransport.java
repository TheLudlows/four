import io.four.protocol.four.Request;
import io.four.registry.config.Host;
import io.four.remoting.netty.NettyClient;
import io.four.remoting.netty.NettyServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class TestTransport {
    @Test
    public void client() throws InterruptedException, IOException {
        Host host = new Host("127.0.0.1:7777");
        NettyClient client = new NettyClient();
        client.start();
        client.connect(host);

        String[] str = new String[1];
        str[0] = "hhhhhh";
        client.send(new Request(1,"123", str), host);
        client.send(new Request(1,"124", str), host);

        System.in.read();
    }

    @Test
    public void server() {
        NettyServer server = new NettyServer();
        server.start();
    }

    @Test

    public void testLength() {
        byte[] bytes = new byte[64];
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();

        String test = "abc";
        buf.writeBytes(test.getBytes());
        buf.writeBytes(bytes,0,64-test.length());
        System.out.println(buf);
    }

    @Test
    public void getMethodName() {
        Class clazz = TestTransport.class;
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method:methods) {
            System.out.println(method.getName());
        }
    }

}
