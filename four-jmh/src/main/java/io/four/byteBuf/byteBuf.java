package io.four.byteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

public class byteBuf {

    static ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void writeAndRelease() {
        buf.writeByte(1);
        buf.writeByte(1);

        buf.clear();
    }


    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(byteBuf.class.getName())
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(8)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
