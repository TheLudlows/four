package io.four.rpc;

import io.four.RPCServer;
import io.four.config.BaseConfig;
import io.four.invoke.UserService;
import io.four.invoke.UserServiceImpl;

import java.io.IOException;

public class FourServer {

    public static void main(String[] args) throws IOException {
        UserService service = new UserServiceImpl();
        RPCServer server = new RPCServer("locahost:7777");
        server.start();
        server.register(UserService.class, service, new BaseConfig().setAlias("alias"),5);
    }
}
