package io.four.invoke;

import io.four.invoker.JavassistInvoker;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class InvokeTest {
    static Method[] methods = UserService.class.getMethods();
    static UserService userService = new UserServiceImpl();
    static JavassistInvoker invoker = new JavassistInvoker(userService, methods[0], UserService.class);

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void refInvoke() throws InvocationTargetException, IllegalAccessException {
        methods[0].invoke(userService, "hello");
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void javassistInvoke() {
        invoker.invoke("hello");
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void dirInvoke() {
        userService.getName("hello");
    }

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(InvokeTest.class.getName())
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(10)
                .forks(1)
                .build();

        new Runner(opt).run();

    }
}

