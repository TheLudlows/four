package io.four;

import io.four.log.Log;
import sun.misc.Unsafe;

/**
 * @author TheLudlows
 */
public class UnsafeUtils {
    final static private Unsafe UNSAFE;

    static {
        try {
            java.lang.reflect.Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) field.get(null);
        } catch (Exception e) {
            Log.warn("can't create unsafe utils", e);
            throw new Error(e);
        }
    }

    public static final Unsafe unsafe() {
        return UNSAFE;
    }
}
