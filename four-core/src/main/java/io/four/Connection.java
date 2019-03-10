package io.four;

import io.four.log.Log;
import io.four.protocol.four.Request;
import io.netty.channel.Channel;

public class Connection {

    private Channel channel;

    public Connection(Channel channel) {
        this.channel = channel;
    }

    public void close() {
        try {
            channel.close();
        }catch (Exception e) {
            Log.warn("close channel failed ",e);
        }
    }

    public void send(Request request) {
        channel.writeAndFlush(request);
    }
}
