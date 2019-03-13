package io.four.rpc;

import io.four.RPCServer;
import io.four.config.BaseConfig;
import io.four.invoke.UserService;
import io.four.invoke.UserServiceImpl;


public class FourServer {

    public static void main(String[] args) {
        UserService service = new UserServiceImpl();
        RPCServer server = new RPCServer("localhost:2181",7777);
        server.start();
        server.register(UserService.class, service, new BaseConfig().setAlias("alias"),5);
    }
}
