package io.four;

import io.netty.util.collection.LongObjectHashMap;

public class InvokeFuturePool {

    private static LongObjectHashMap<InvokeFuture> waitPool = new LongObjectHashMap<>();

    public static void add(InvokeFuture future) {
        long id = future.getRequestId();
        if (waitPool.get(id).isDone()) {
            waitPool.remove(id);
        } else {
            waitPool.put(id, future);
        }
    }
}
