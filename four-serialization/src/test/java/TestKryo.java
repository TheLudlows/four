import io.four.serialization.Serialize;
import io.four.serialization.SerializerHolder;
import io.four.serialization.fastjson.FastJSONSerialize;
import io.four.serialization.kryo.KryoSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestKryo implements Serializable {

    String name ;
    int age ;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestKryo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }


    @Test
    public void obj2ByteBuf() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        pool.submit(new fj2Runner());
        pool.submit(new fj2Runner());
        pool.submit(new fj2Runner());
        pool.submit(new fj2Runner());
        Thread.currentThread().join();
    }
    @Test
    public void getservice() {
        System.out.println(SerializerHolder.serialize());
    }


    //todo
    @Test
    public void testUnsafeBuf() {
        String str = "hhhhhh";
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        System.out.println(buf);
    }


}
class FjRunner implements Runnable {

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        Serialize serialize = new FastJSONSerialize();
        for(int i=0; i<1000000;i++) {
            TestKryo test = new TestKryo();
            test.setAge(i);
            test.setName(""+i);
            List<TestKryo> list = new ArrayList();
            list.add(test);
            byte[] bytes = serialize.objectToByte(list);
            serialize.byteToObject(bytes,ArrayList.class);
        }
        System.out.println("fjRunner_"+Thread.currentThread().getName()+":"+(System.currentTimeMillis()-start));
    }
}
class KryRunner implements Runnable {
    @Override
    public void run() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        long start = System.currentTimeMillis();
        Serialize serialize = new KryoSerialize();
        for(int i=0; i<1000000;i++) {
            TestKryo test = new TestKryo();
            test.setAge(i);
            test.setName(""+i);
            List<TestKryo> list = new ArrayList();
            list.add(test);
            serialize.objectToByteBuf(list,buf);
            serialize.byteBufToObject(buf,ArrayList.class);
            buf.clear();
        }
        System.out.println("KryRunner_"+Thread.currentThread().getName()+":"+(System.currentTimeMillis()-start));
    }

}

class fj2Runner implements Runnable {
    @Override
    public void run() {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        long start = System.currentTimeMillis();
        Serialize serialize = new FastJSONSerialize();
        for(int i=0; i<1000000;i++) {
            TestKryo test = new TestKryo();
            test.setAge(i);
            test.setName(""+i);
            List<TestKryo> list = new ArrayList();
            list.add(test);
            serialize.objectToByteBuf(list,buf);
            serialize.byteBufToObject(buf,ArrayList.class);
            buf.clear();
        }
        System.out.println("KryRunner_"+Thread.currentThread().getName()+":"+(System.currentTimeMillis()-start));
    }

}
