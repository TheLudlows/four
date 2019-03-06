package io.four;

import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.LongObjectHashMap;

public class InvokeFuturePool {

    private LongObjectHashMap<InvokeFuture> waitPool = new LongObjectHashMap<>();
    IntObjectHashMap
}
