package io.four.serialize;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author TheLudlows
 */

public class TestLongAdder {

    private static LongAdder longAdder = new LongAdder();

    private static AtomicLong atomicLong = new AtomicLong();

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void longAdder() {

    }

    public static void main(String[] args) throws InterruptedException {

        Thread[] threads1 = new Thread[100];
        Thread[] threads2 = new Thread[100];
        for (int i = 0; i < 100; i++) {

            threads1[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++)
                    longAdder.increment();
            });

            threads2[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++)
                    atomicLong.getAndIncrement();
            });
        }

        for (int i = 0; i < 100; i++) {
            threads1[i].start();
            threads2[i].start();
        }

        for (int i = 0; i < 100; i++) {
            threads1[i].join();
            threads2[i].join();
        }

        System.out.println(longAdder.sum());
        System.out.println(atomicLong.get());

    }

}
