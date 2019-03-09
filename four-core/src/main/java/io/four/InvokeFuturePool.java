package io.four;

import io.four.protocol.four.Request;
import io.four.protocol.four.Response;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;

import java.util.Iterator;

import static io.four.exception.ExceptionHolder.INVOKE_TIMEOUT;

public class InvokeFuturePool {

    private LongObjectHashMap<InvokeFuture> waitPool = new LongObjectHashMap<>();

    public InvokeFuture add(Request request) {
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
        if (future != null && !future.isDone()) {
            future.complete(response.getServiceResult());
            remove((id));
        }

    }

    public void remove(long id) {
        waitPool.remove(id);
    }

    public void cleanTimeout(int ms) {

        Iterator<LongObjectMap.PrimitiveEntry<InvokeFuture>> iterator = waitPool.entries().iterator();
        while (iterator.hasNext()) {
            InvokeFuture future = iterator.next().value();
            long expireTime = future.getTime() + ms;
            final long now = TimeUtil.currentTimeMillis();
            if (expireTime < now) {
                continue;
            } else {
                iterator.remove();
                future.completeExceptionally(INVOKE_TIMEOUT);
            }
        }
    }

}
