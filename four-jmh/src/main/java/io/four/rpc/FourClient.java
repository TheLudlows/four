package io.four.rpc;

import io.four.RPCClient;
import io.four.config.BaseConfig;
import io.four.invoke.UserService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static io.four.RPCClient.RPCCLIENT;


@State(Scope.Benchmark)
public class FourClient {
    private static UserService consumer;
    private static RPCClient rpcClient;

    static {
        rpcClient = RPCCLIENT;
        rpcClient.start();
        try {
            consumer = RPCClient.getProxy(UserService.class, new BaseConfig().setAlias("alias"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(FourClient.class.getName())
                .warmupIterations(5)
                .measurementIterations(5)
                .threads(8)
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    @TearDown
    public void close() {
        rpcClient.close();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void four_getAge() throws Exception {
        consumer.getAge();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void four_getAgeGet() throws Exception {
        consumer.getAge().get();
    }
}
