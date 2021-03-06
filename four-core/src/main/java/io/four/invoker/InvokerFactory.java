package io.four.invoker;

import io.four.log.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * save invokers in ConcurrentHashMap,key is service name,value is
 * the invokers of a interface,the index is stable
 */

public class InvokerFactory {

    private static ConcurrentHashMap<String, Invoker[]> invokers = new ConcurrentHashMap<>();

    /**
     * register a service and generate the invoker
     *
     * @param service
     */
    public static void generateInvoker(Class clazz, Object service) {
        if (invokers.get(clazz.getName()) != null) {
            Log.info("This service has Invoker already " + clazz.getName());
        }

        if (!Modifier.isPublic(clazz.getModifiers())) {
            throw new RuntimeException("the clazz must be public");
        }
        Method[] methods = clazz.getDeclaredMethods();
        List<Invoker> list = new ArrayList<>();
        Invoker[] invokerArr;
        for (Method method : methods) {
            if (method.isDefault()) continue;
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (!CompletableFuture.class.equals(method.getReturnType())) {
                System.out.println(method.getReturnType().getName());
                throw new RuntimeException("method return-type must be CompletableFuture");
            }
            list.add(new JavassistInvoker(service, method, clazz));
        }
        invokerArr = new Invoker[list.size()];
        for (int i = 0; i < list.size(); i++) {
            invokerArr[i] = list.get(i);
        }
        invokers.put(clazz.getName(), invokerArr);
    }

    public static Invoker getInvoker(String serviceName, int index) {
        Invoker[] invoker = invokers.get(serviceName);
        if (invoker == null) {
            throw new RuntimeException("no such impl" + serviceName);
        }
        return invoker[index];
    }
}
