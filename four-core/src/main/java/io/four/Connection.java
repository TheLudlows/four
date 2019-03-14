package io.four;

import io.four.log.Log;
import io.four.protocol.four.Request;
import io.netty.channel.Channel;

public class Connection {

    private Channel channels[];
    private int n;

    public Connection(Channel[] channels) {
        this.channels = channels;
        n = channels.length;
    }

    public void close() {
        try {
            for (int i = 0; i < channels.length; i++) {
                channels[i].close();
            }
        } catch (Exception e) {
            Log.warn("close channel failed ", e);
        }
    }

    public void send(Request request) {
        int index = (int) request.getRequestId() % n;
        channels[index].writeAndFlush(request);
    }
}
