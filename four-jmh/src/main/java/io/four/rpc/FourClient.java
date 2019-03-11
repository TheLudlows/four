package io.four.rpc;

import io.four.RPCClient;
import io.four.config.BaseConfig;
import io.four.invoke.UserService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.util.concurrent.TimeUnit;


@State(Scope.Thread)
public class FourClient {
    private static UserService consumer;

   static  {
        RPCClient.start();
        try {
            consumer = RPCClient.getProxy(UserService.class, new BaseConfig().setAlias("alias"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        //FourClient client = new FourClient();
        //client.close();
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
        RPCClient.close();
    }
    /*@Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void four_getAge() throws Exception {
        consumer.getAge();
    }*/

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void four_getAgeGet() throws Exception {
        consumer.getAge().get();
    }

   /* @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void four_getName() throws Exception {
        consumer1.getName("four-RPC");
    }*/
}
