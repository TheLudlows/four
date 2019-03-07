package io.four.providerSave;

import com.esotericsoftware.kryo.util.ObjectMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class TestProviderSave {

    private static ConcurrentHashMap<String, String[]> providers1 = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, String> providers2 = new ConcurrentHashMap<>();

    private static ObjectMap<String, String[]> providers3 = new ObjectMap<>();

    private static HashMap<String, String[]> providers4 = new HashMap<>();


    static String key = 10 + "";
    static int index = 3;

    static {
        for (int i = 0; i < 100; i++) {
            providers1.put("" + i, new String[]{"" + 1, "" + 2, "" + 3, "" + 4, "" + 5});
            providers3.put("" + i, new String[]{"" + 1, "" + 2, "" + 3, "" + 4, "" + 5});
            providers4.put("" + i, new String[]{"" + 1, "" + 2, "" + 3, "" + 4, "" + 5});

            for (int j = 0; j < 5; j++) {
                providers2.put(i + "" + j, j + "");
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TestProviderSave.class.getName())
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(8)
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void get1() {
        String value = providers1.get(key)
                [index];
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void get3() {
        String value = providers3.get(key)
                [index];
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void get2() {
        String value = providers2.get(key + index);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void get4() {
        String value = providers4.get(key)[index];
    }

}
