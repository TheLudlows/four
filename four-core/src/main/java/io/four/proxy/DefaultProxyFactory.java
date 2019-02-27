package io.four.proxy;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TheLudlows
 */

public class DefaultProxyFactory {
    private static Map<Class, Object> methodInvokeCache = new ConcurrentHashMap();



    @SuppressWarnings("unchecked")
    public static  <T> T getProxy(Class clazz) throws Exception {
        T t = (T) methodInvokeCache.get(clazz);
        if (t == null) {
            t = newInstance(clazz);
        }
        return t;
    }

    private static <T> T newInstance(Class clazz) throws Exception {
        if (clazz == null) {
            throw new InvokeException("clazz cannot be null");
        }
        if (!clazz.isInterface()) {
            throw new InvokeException("the clazz must be interface");
        }
        if (!Modifier.isPublic(clazz.getModifiers())) {
            throw new InvokeException("the clazz must be public");
        }
        return null;
    }
}
