package io.four;

import io.four.exception.InvokePoolFullException;
import io.four.log.Log;
import io.four.protocol.four.Request;
import io.netty.channel.Channel;

import java.util.concurrent.CompletableFuture;

public class Connection {
    private final static int MAX_POOL_SIZE = 5000;


    private Channel channel;

    private InvokeFuturePool pool;

    public Connection(Channel channel, InvokeFuturePool pool) {
        this.channel = channel;
        this.pool = pool;
    }

    public void close() {
        try {
            channel.close();
            pool.close();
        }catch (Exception e) {
            Log.warn("close channel failed ",e);
        }
    }

    public void send(Request request) {
        int size = pool.size();
        if (size > MAX_POOL_SIZE) {
            CompletableFuture future = request.getFuture();
            future.completeExceptionally(new InvokePoolFullException("invoke pool full:" + size));
            return;
        }
        channel.writeAndFlush(request);
    }
}
