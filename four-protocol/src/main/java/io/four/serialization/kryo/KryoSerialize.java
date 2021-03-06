package io.four.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.four.serialization.Serialize;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class KryoSerialize implements Serialize {
    private final static ThreadLocal<Kryo> HOLDER = ThreadLocal.withInitial(() -> new KryoRegister().registerKryo());
    private final static ThreadLocal<Output> OUTPUTHOLDER = ThreadLocal.withInitial(Output::new);

    @Override
    public void objectToByteBuf(Object obj, ByteBuf buf) {
        if (buf == null) {
            return;
        }
        Kryo kryo = HOLDER.get();
        Output output = OUTPUTHOLDER.get();
        output.setOutputStream(new ByteBufOutputStream(buf));
        kryo.writeObject(output, obj);
        output.flush();
        output.close();
    }


    @Override
    public Object byteBufToObject(ByteBuf buf, Class clazz) {
        if (buf == null) {
            return null;
        }
        Input input = new Input(new ByteBufInputStream(buf));
        Kryo kryo = HOLDER.get();
        return kryo.readObject(input, clazz);
    }

    @Override
    public Object byteBufToObjectSlow(ByteBuf buf, Class clazz) {
        return null;
    }

    @Override
    public void objectToByteBufSlow(Object obj, ByteBuf buf) {

    }

    @Override
    public byte[] objectToByte(Object obj) {
        return null;
    }

    @Override
    public Object byteToObject(byte[] bytes, Class clazz) {
        return null;
    }
}
