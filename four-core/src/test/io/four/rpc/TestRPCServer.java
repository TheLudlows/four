package io.four.rpc;

import io.four.RPCClient;
import io.four.RPCServer;
import io.four.TimeUtil;
import io.four.config.BaseConfig;
import io.four.invoke.UserService;
import io.four.invoke.UserServiceImpl;
import org.junit.Test;

import static io.four.RPCClient.RPCCLIENT;

public class TestRPCServer {

    public static void main(String[] args) {
        UserService service = new UserServiceImpl();
        RPCServer server = new RPCServer("localhost:2181",7777);
        server.start();
        server.register(UserService.class, service, new BaseConfig().setAlias("alias"),5);
    }

    @Test
    public void client() throws Exception {
        RPCClient rpcClient =  RPCCLIENT;
        rpcClient.start();
        UserService userService = rpcClient.getProxy(UserService.class,new BaseConfig().setAlias("alias"));
        System.out.println(userService.getName("FFFFF").get());
        long start = TimeUtil.currentTimeMillis();
        for(int i=0;i<100000;i++)
           userService.getAge();
        System.out.println(TimeUtil.currentTimeMillis() - start);
    }
}
