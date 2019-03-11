package io.four;

import io.four.exception.ConnectionException;
import io.four.exception.InvokePoolFullException;
import io.four.log.Log;
import io.four.protocol.four.Request;
import io.four.protocol.four.Response;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

import static io.four.exception.ExceptionHolder.INVOKE_TIMEOUT;

public class InvokeFuturePool {

    private final static int MAX_POOL_SIZE = 5000;
    private final static int WAIT_MAX_TIME = 2000;
    private final static int FINISH_TIME = 1000;
    private LongObjectHashMap<InvokeFuture> waitPool = new LongObjectHashMap<>();

    public CompletableFuture add(Request request) {
        int size = waitPool.size();
        if (size > MAX_POOL_SIZE) {
            CompletableFuture future = request.getFuture();
            future.completeExceptionally(new InvokePoolFullException("invoke pool full:" + size));
            Log.info("Invoke pool size:" + size);
            return null;
        }
        long id = request.getRequestId();
        InvokeFuture future = (InvokeFuture) request.getFuture();
        future.setTime(request.getTimestamp())
                .setRequestId(id);
        waitPool.put(id, future);
        return future;
    }

    public void finish(Response response) {
        long id = response.getRequestId();
        InvokeFuture future = waitPool.get(id);
        if (future != null) {
            future.complete(response.getServiceResult());
            remove(id);
        }
    }

    private void remove(long id) {
        waitPool.remove(id);
    }

    public void cleanTimeout() {
        long finishTIme = TimeUtil.currentTimeMillis() + FINISH_TIME;
        Iterator<LongObjectMap.PrimitiveEntry<InvokeFuture>> iterator = waitPool.entries().iterator();
        while (iterator.hasNext()) {
            if (finishTIme > TimeUtil.currentTimeMillis()) {
                break;
            }
            InvokeFuture future = iterator.next().value();
            long expireTime = future.getTime() + WAIT_MAX_TIME;
            final long now = TimeUtil.currentTimeMillis();
            if (expireTime > now) {
                iterator.remove();
                future.completeExceptionally(INVOKE_TIMEOUT);
            }
        }
    }

    public void close() {
        if (waitPool.isEmpty()) return;
        cleanTimeout();
        if (waitPool.isEmpty()) return;

        Iterator<LongObjectMap.PrimitiveEntry<InvokeFuture>> iterator = waitPool.entries().iterator();
        while (iterator.hasNext()) {
            InvokeFuture future = iterator.next().value();
            iterator.remove();
            future.completeExceptionally(new ConnectionException("connection is closed"));
        }
    }
}
