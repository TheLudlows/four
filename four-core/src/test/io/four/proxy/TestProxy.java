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

    public interface Hell{
        CompletableFuture say(int n,String a);
        CompletableFuture gogo(Hell hell);
    }
    @Test
    public void testJavassit() throws Exception {
        JavassistInvoke.newFailFastProxy(Hell.class);
    }
}
