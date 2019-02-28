package io.four.proxy;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * @author TheLudlows
 */

public class TestProxy {
    @Test
    public void testProxy() {
        try {
            Hell hell = DefaultProxyFactory.getProxy(Hell.class);
            //hell.say();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface Hell {
        CompletableFuture say(int n, String a);

        CompletableFuture gogo();
    }

    @Test
    public void testJavassit() throws Exception {
        long start1 = System.currentTimeMillis();
        Hell hell1 = DefaultProxyFactory.getProxy(Hell.class);
        System.out.println("JavassistProxy new " + (System.currentTimeMillis() - start1));
        long start2 = System.currentTimeMillis();
        Hell hell2 = DefaultProxyFactory.getProxy(Hell.class);
        System.out.println("CglibProxy new " + (System.currentTimeMillis() - start2));
        long start3 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            hell1.gogo();
        }
        System.out.println("JavassistProxy exe " + (System.currentTimeMillis() - start3));

        long start4 = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            hell2.gogo();
        }
        System.out.println("cglib exe " + (System.currentTimeMillis() - start4));

    }

    public static void main(String[] args) {

        System.out.println("hello mac book !");
    }

}
