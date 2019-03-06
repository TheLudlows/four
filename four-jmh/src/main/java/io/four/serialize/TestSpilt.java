package io.four.serialize;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;


@State(Scope.Thread)
public class TestSpilt {
    static String s = "abc#123";

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void stringSpilt() throws Exception {
        s.split("#");

    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(TestSpilt.class.getName())
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(10)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
