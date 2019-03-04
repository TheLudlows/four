package io.four.remoting;

import io.four.exception.RequestTimeoutException;
import io.four.protocol.four.Response;
import io.netty.channel.Channel;
import sun.misc.RequestProcessor;

public abstract class BaseRpcServer {

    public void registerProecessor(final byte requestCode, final RequestProcessor processor) {

    }


    public Response invokeSync(final Channel channel, final Response request, final long timeoutMillis) throws InterruptedException,
            RequestTimeoutException {
        return null;
    }
}
