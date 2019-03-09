package io.four.rpc;

import io.four.RPCClient;
import io.four.RPCServer;
import io.four.TimeUtil;
import io.four.config.BaseConfig;
import io.four.invoke.UserService;
import io.four.invoke.UserServiceImpl;
import org.junit.Test;

public class TestRPCServer {

    public static void main(String[] args) {
        UserService service = new UserServiceImpl();
        RPCServer.start();
        RPCServer.register(UserService.class, service, new BaseConfig().setAlias("alias"));
    }

    @Test
    public void client() throws Exception {
        RPCClient.init();
        UserService userService = RPCClient.getProxy(UserService.class,new BaseConfig().setAlias("alias"));
        System.out.println(userService.getName("FFFFF").get());
        long start = TimeUtil.currentTimeMillis();
        for(int i=0;i<10000;i++)
           userService.getAge().get();
        System.out.println(TimeUtil.currentTimeMillis() - start);
    }


}
