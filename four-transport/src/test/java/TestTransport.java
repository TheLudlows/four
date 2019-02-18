import io.four.remoting.netty.NettyClient;
import io.four.remoting.netty.NettyServer;
import org.junit.Test;

import java.io.IOException;

import static io.four.protocol.four.EntryBuilder.requestEntry;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class TestTransport {
    @Test
    public void client() throws InterruptedException, IOException {
        String address = "localhost:7777";
        NettyClient client = new NettyClient();
        client.start();
        client.connect(address);

        String [] str = new String[1];
        str[0] = "hhhhhh";
        client.send(requestEntry(1231232,str),address);
        System.in.read();
    }

    @Test
    public void server() {
        NettyServer server = new NettyServer();
        server.start();
    }

}
