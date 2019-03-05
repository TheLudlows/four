import io.four.protocol.four.Request;
import io.four.registry.config.Host;
import io.four.remoting.netty.NettyClient;
import io.four.remoting.netty.NettyServer;
import org.junit.Test;

import java.io.IOException;



/**
 * @author TheLudlows
 * @since 0.1
 */
public class TestTransport {
    @Test
    public void client() throws InterruptedException, IOException {
        Host host = new Host("localhost:7777");
        NettyClient client = new NettyClient();
        client.start();
        client.connect(host);

        String[] str = new String[1];
        str[0] = "hhhhhh";
        client.send(new Request(1,"123", str), host);
        client.send(new Request(1,"124", str), host);
        client.send(new Request(1,"125", str), host);
        client.send(new Request(1,"126", str), host);
        client.send(new Request(1,"127", str), host);

        System.in.read();
    }

    @Test
    public void server() {
        NettyServer server = new NettyServer();
        server.start();
    }

}
