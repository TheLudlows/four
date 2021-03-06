package io.four.rpc;

import io.four.invoke.UserService1;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class JSFClient {
    private  ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            "/jsf-consumer.xml");
    private  UserService1 consumer = (UserService1) context.getBean("userService1");
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JSFClient.class.getName())
                .warmupIterations(5)
                .measurementIterations(5)
                .threads(100)
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void jsf_getAge() throws Exception {
        consumer.getAge();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void jsf_getName() throws Exception {
        consumer.getName("jsf");
    }

    @TearDown
    public void close() {
        context.close();
    }
}
