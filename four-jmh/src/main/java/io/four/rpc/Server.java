package io.four.rpc;

import io.four.RPCServer;
import io.four.config.BaseConfig;
import io.four.invoke.UserService;
import io.four.invoke.UserServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException {
        UserService service = new UserServiceImpl();
        RPCServer.start();
        RPCServer.register(UserService.class, service, new BaseConfig().setAlias("alias"));

       /* ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
                "jsf-provider.xml");

        System.in.read();*/
    }
}
