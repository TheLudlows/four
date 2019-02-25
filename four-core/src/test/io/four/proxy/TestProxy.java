package io.four.proxy;

import io.four.client.proxy.DefaultProxyFactory;
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
            hell.say();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public interface Hell{
        CompletableFuture say();
    }
}
