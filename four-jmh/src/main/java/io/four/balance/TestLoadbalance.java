package io.four.balance;

import io.four.proxy.DefaultLoadBalance;
import io.four.proxy.LoadBalance;
import io.four.registry.config.Host;
import io.four.serialization.SerializerHolder;
import io.four.serialize.SerializeJMH;
import io.four.serialize.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestLoadbalance {
    public static final int CONCURRENCY = Runtime.getRuntime().availableProcessors();

    static List<Host> list = new ArrayList();
    static LoadBalance balance = new DefaultLoadBalance(list);
    static {
        list.add(new Host("loadhost:8080"));
        list.add(new Host("loadhost:8080"));
        list.add(new Host("loadhost:8080"));
        list.add(new Host("loadhost:8080"));
        list.add(new Host("loadhost:8080"));
        list.add(new Host("loadhost:8080"));
        list.add(new Host("loadhost:8080"));
    }
    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(TestLoadbalance.class.getName())
                .warmupIterations(5)
                .measurementIterations(5)
                .threads(CONCURRENCY)
                .forks(1)
                .build();
        System.out.println(CONCURRENCY);
        new Runner(opt).run();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void next() throws Exception {
       balance.next();
    }

}
