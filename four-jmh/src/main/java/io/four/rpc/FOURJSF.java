package io.four.rpc;

import io.four.RPCClient;
import io.four.TimeUtil;
import io.four.config.BaseConfig;
import io.four.invoke.UserService;
import io.four.invoke.UserService1;
import io.four.recycle.TestRecycle;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
@State(Scope.Thread)

public class FOURJSF {
    static UserService consumer1;
    static UserService1 consumer2;

    static {
        RPCClient.init();
        try {
            consumer1 = RPCClient.getProxy(UserService.class, new BaseConfig().setAlias("alias"));
            System.out.println(consumer1.getName("FFFFF").get());
            System.out.println(consumer1.getAge().get());

          /*  ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
                    "/jsf-consumer.xml");
           consumer2 = (UserService1) appContext
                    .getBean("userService1");

            System.out.println(consumer2.getName("FFFFF"));
            System.out.println(consumer2.getAge());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(FOURJSF.class.getName())
                .warmupIterations(5)
                .measurementIterations(5)
                .threads(8)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
    @TearDown
    public void close() throws IOException {
        RPCClient.close();
    }
   /* @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void four_getAge() throws Exception {
        consumer1.getAge();
    }*/

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void four_getAgeGet() throws Exception {
        consumer1.getAge().get();
    }

   /* @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void four_getName() throws Exception {
        consumer1.getName("four-RPC");
    }*/
/*
    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void jsf_getAge() throws Exception {
        consumer2.getAge();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void jsf_getName() throws Exception {
        consumer2.getName("jsf");
    }*/

}
