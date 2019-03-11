package io.four.serialize;

import io.four.protocol.four.MessageUtil;
import io.four.protocol.four.Request;
import io.four.serialization.SerializerHolder;
import io.four.serialization.fastjson.FastJSONSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author TheLudlows
 */
@State(Scope.Thread)
public class SerializeJMH {
    public static final int CONCURRENCY = Runtime.getRuntime().availableProcessors();

    public static Request request = MessageUtil.getRequest().setRequestId(1)
            .setMethodIndex((byte)2).setArgs(new Object[]{"aaaaa"})
            .setServiceName("111");
    private ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer(1024 * 1024 * 8);

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(SerializeJMH.class.getName())
                .warmupIterations(5)
                .measurementIterations(10)
                .threads(CONCURRENCY)
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void allToeBuf() {
        buf.clear();
        SerializerHolder.getFastJson().objectToByteBuf(request,buf);
    }
    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void customerToBuf() {
        buf.clear();
        request.toByteBuf(buf);
    }

/*
    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serializeFastJsonSlow() throws Exception {
        buf.clear();
        SerializerHolder.getFastJson().objectToByteBufSlow(new User(1), buf);
        SerializerHolder.getFastJson().byteBufToObjectSlow(buf, User.class);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serializeFastJson() throws Exception {
        buf.clear();
        SerializerHolder.getFastJson().objectToByteBuf(new User(1), buf);
        SerializerHolder.getFastJson().byteBufToObject(buf, User.class);
    }*/

/*    @Benchmark
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void serializeKryo() throws Exception {
        buf.clear();
        SerializerHolder.getKryo().objectToByteBuf(new User(1), buf);
        SerializerHolder.getKryo().byteBufToObject(buf, User.class);
    }*/
}
