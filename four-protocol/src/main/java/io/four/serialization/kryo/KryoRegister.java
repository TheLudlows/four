package io.four.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *  @author: TheLudlows
 *  @since 0.1
 */

public class KryoRegister {

    private KryoRegister() {
    }

    protected static Kryo registerKryo() {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(CopyOnWriteArrayList.class);
        kryo.register(HashMap.class);
        kryo.register(ConcurrentHashMap.class);
        kryo.register(TreeMap.class);
        kryo.register(TreeSet.class);
        kryo.register(HashSet.class);
        kryo.register(java.util.Date.class);
        kryo.register(java.sql.Date.class);
        kryo.register(LocalDate.class);
        kryo.register(LocalDateTime.class);
        kryo.register(byte[].class);
        kryo.register(char[].class);
        kryo.register(short[].class);
        kryo.register(int[].class);
        kryo.register(long[].class);
        kryo.register(float[].class);
        kryo.register(double[].class);
        kryo.register(boolean[].class);
        kryo.register(String[].class);
        kryo.register(Object[].class);
        kryo.register(BigInteger.class);
        kryo.register(BigDecimal.class);
        kryo.register(Class.class);
        kryo.register(Enum.class);
        kryo.register(EnumSet.class);
        kryo.register(Currency.class);
        kryo.register(StringBuffer.class);
        kryo.register(StringBuilder.class);
        kryo.register(Collections.EMPTY_LIST.getClass());
        kryo.register(Collections.EMPTY_MAP.getClass());
        kryo.register(Collections.EMPTY_SET.getClass());
        kryo.register(Collections.singletonList(null).getClass());
        kryo.register(Collections.singletonMap(null, null).getClass());
        kryo.register(Collections.singleton(null).getClass());
        kryo.register(Collection.class);
        kryo.register(Map.class);
        kryo.register(TimeZone.class);
        kryo.register(Calendar.class);
        kryo.register(Locale.class);
        kryo.register(Charset.class);
        kryo.register(URL.class);
        return kryo;
    }

}
