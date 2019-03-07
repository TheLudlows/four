package io.four.invoker;

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
    public static void generateInvoker(Object service) {
        Class clazz = service.getClass();
        if (invokers.get(clazz.getName()) != null) {
            throw new RuntimeException("This service has Invoker already " + clazz.getName());
        }

        if (!Modifier.isPublic(clazz.getModifiers())) {
            throw new RuntimeException("the clazz must be public");
        }
        Method[] methods = clazz.getDeclaredMethods();
        List<Invoker> list = new ArrayList<>();
        Invoker[] invokerArr;
        for (Method method : methods) {
            if (method.getModifiers() != Modifier.PUBLIC) continue;
            if (!CompletableFuture.class.equals(method.getReturnType())) {
                System.out.println(method.getReturnType().getName());
                throw new RuntimeException("method return-type must be CompletableFuture");
            }
            list.add(new JavassistInvoker(service, method, clazz));
        }
        invokerArr = new Invoker[list.size()];
        for(int i=0; i<list.size();i++) {
            invokerArr[i] = list.get(i);
        }
        invokers.put(clazz.getName(), invokerArr);
    }

    public static Invoker getInvoker(String serviceName, int index) {
        return invokers.get(serviceName)[index];
    }
}
