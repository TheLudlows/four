package io.four.string;

import io.four.UnsafeStringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class TestString {

    static String s = "    sdasdsndnslfmdnas,mm.a.d,ma.dma   ";

    static byte[] bytes = s.getBytes();

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void newString() {
        new String(bytes);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void unsafenew() {
        UnsafeStringUtils.toString(bytes);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void unsafetoByte() {
        UnsafeStringUtils.getLatin1Bytes(s);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void unsafetoByte2() {
        UnsafeStringUtils.getUTF8Bytes(s);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void getBytes() {
        s.getBytes();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void strTrim() {
        s.trim();
    }




    public static void main(String[] args) throws Exception {
        System.out.println(s.length() == s.getBytes().length);
        System.out.println(Byte.MAX_VALUE);
       /* Options opt = new OptionsBuilder()
                .include(TestString.class.getName())
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(8)
                .forks(1)
                .build();

        new Runner(opt).run();*/

    }
}
