package io.four.invoke;

import io.four.invoker.JavassistInvoker;

import java.lang.reflect.Method;

public class TestJavaInvoker {

    static Method[] methods = UserService.class.getMethods();
    static UserService userService = new UserServiceImpl();
    static JavassistInvoker invoker = new JavassistInvoker(userService,methods[1],UserService.class);

    public static void main(String[] args) {
        System.out.println(methods[0].getParameterCount());
        System.out.println(invoker.invoke("hello"));
    }
}
