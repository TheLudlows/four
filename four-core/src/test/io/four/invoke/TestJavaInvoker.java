package io.four.invoke;

import io.four.invoker.Invoker;
import io.four.invoker.InvokerFactory;
import io.four.invoker.JavassistInvoker;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestJavaInvoker {

    static Method[] methods = UserService.class.getMethods();
    static UserService userService = new UserServiceImpl();
    static JavassistInvoker invoker = new JavassistInvoker(userService,methods[1],UserService.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //System.out.println(methods[0].getParameterCount());
        //System.out.println(invoker.invoke("hello"));

        InvokerFactory.generateInvoker(UserService.class,userService);
        Invoker<CompletableFuture> invoker = InvokerFactory.getInvoker(userService.getClass().getName(),1);
        System.out.println(invoker.invoke().get());
    }
}
