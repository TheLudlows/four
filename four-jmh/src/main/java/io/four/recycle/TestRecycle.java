package io.four.recycle;

import io.four.protocol.four.MessageUtil;
import io.four.protocol.four.Response;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)

public class TestRecycle {
    //static  Response response;
    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void getRequest() {
        MessageUtil.getRequest();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void getResponse() {
        MessageUtil.getResponse();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public Response newResponse() {
        Response response = new Response();
        return response;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TestRecycle.class.getName())
                .warmupIterations(5)
                .measurementIterations(20)
                .threads(8)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
