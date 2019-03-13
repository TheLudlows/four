package io.four;

import io.netty.util.concurrent.FastThreadLocal;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * 基于ThreadLocal 获取唯一long Id，不保证递增，性能为AtomicLong的10倍
 */
public final class FastLong {
    private static final int BATCH_INCREMENT = 1000;

    private final AtomicLong rootCounter;
    private final Supplier<Cell> supplier;
    private FastThreadLocal<Cell> fastThreadLocal;

    public FastLong() {
        this(0);
    }

    public FastLong(int initialValue) {
        this.rootCounter = new AtomicLong(initialValue);
        this.supplier = () -> new Cell(rootCounter);
        fastThreadLocal = new FastThreadLocal() {
            @Override
            protected Object initialValue() {
                return supplier.get();
            }
        };
    }

    public long next() {
        return fastThreadLocal.get().next();
    }

    private static class Cell {
        private final AtomicLong root;
        private long base;
        private long counter;

        Cell(AtomicLong root) {
            this.root = root;
            this.base = root.getAndAdd(BATCH_INCREMENT);
        }

        long next() {
            long value = base + counter++;

            if (counter == BATCH_INCREMENT) {
                base = root.getAndAdd(BATCH_INCREMENT);
                counter = 0;
            }

            return value;
        }
    }
}
