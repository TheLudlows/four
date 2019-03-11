package io.four.rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class JSFServer {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
                "jsf-provider.xml");
        System.in.read();
    }
}
