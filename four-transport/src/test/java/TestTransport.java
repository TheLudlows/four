import io.four.protocol.body.RequestBody;
import io.four.protocol.four.TransportEntry;
import io.four.remoting.netty.NettyClient;
import io.four.remoting.netty.NettyServer;
import org.junit.Test;

import static io.four.protocol.four.EntryBuilder.requestEntry;
import static io.four.protocol.four.ProtocolConstant.FASTJSON;


/**
 * @author TheLudlows
 * @since 0.1
 */
public class TestTransport {

    public static void main(String[] args) throws InterruptedException {
        String address = "localhost:7777";
        NettyClient client = new NettyClient();
        client.start();
        client.connect(address);

        String [] str = new String[1];
        str[0] = "hhhhhh";
        client.send(requestEntry(1231232,str),address);
    }

    @Test
    public void server() {
        NettyServer server = new NettyServer();
        server.start();
    }

}
