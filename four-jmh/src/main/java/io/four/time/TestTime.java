package io.four.time;

import io.four.TimeUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)

public class TestTime {
    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void getNow() {
        System.currentTimeMillis();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void getNow1() {
        TimeUtil.currentTimeMillis();
    }

    public static void main(String[] args) throws Exception {

        Options opt = new OptionsBuilder()
                .include(TestTime.class.getName())
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(5)
                .forks(1)
                .build();

        new Runner(opt).run();

    }
}



