import io.four.serialization.kryo.KryoSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public void obj2ByteBuf() {
        String str = "hhhhhh";
        TestKryo test = new TestKryo();
        test.setAge(100);
        test.setName("BBBBBBBBBBBBBBBBB");
        List<TestKryo> list = new ArrayList();
        list.add(test);
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        new KryoSerialize().objectToByteBuf(list,buf);
        System.out.println(buf);
        ArrayList s = (ArrayList) new KryoSerialize().byteBufToObject(buf,ArrayList.class);
        System.out.println(s);
    }
    //todo
    @Test
    public void testUnsafeBuf() {
        String str = "hhhhhh";
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();
        System.out.println(buf);
    }


}
