package io.four.proxy;

import io.four.config.BaseConfig;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TheLudlows
 */

public class DefaultProxyFactory {

    private static Map<Class, Object> proxyCache = new ConcurrentHashMap();

    @SuppressWarnings("unchecked")
    public static  <T> T getProxy(Class clazz, BaseConfig config) throws Exception {
        T t = (T) proxyCache.get(clazz);
        if (t == null) {
            t = newInstance(clazz,config);
            if(t != null) {
                proxyCache.put(clazz, t);
            }
        }
        return t;
    }

    private static <T> T newInstance(Class clazz, BaseConfig config) throws Exception {
        if (clazz == null) {
            throw new RuntimeException("clazz cannot be null");
        }
        if (!clazz.isInterface()) {
            throw new RuntimeException("the clazz must be interface");
        }
        if (!Modifier.isPublic(clazz.getModifiers())) {
            throw new RuntimeException("the clazz must be public");
        }
        return JavassistProxy.newProxy(clazz, new ProxyInvoke(clazz.getName(), config));
    }
}
