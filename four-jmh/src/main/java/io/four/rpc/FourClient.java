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
            consumer = rpcClient.getProxy(UserService.class, new BaseConfig().setAlias("alias"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(FourClient.class.getName())
                .warmupIterations(5)
                .measurementIterations(20)
                .threads(16)
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    @TearDown
    public static void close() {
        rpcClient.close();
    }

   /* @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void four_getAge() throws Exception {
        consumer.getAge();
    }*/

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public int four_getAgeGet() throws Exception {
        return consumer.getAge().get();
    }
}
