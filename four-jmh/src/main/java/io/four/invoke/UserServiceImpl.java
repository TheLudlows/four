package io.four.invoke;

import java.util.concurrent.CompletableFuture;

public class UserServiceImpl implements UserService {
    @Override
    public CompletableFuture<String> getName(String hello) {
        CompletableFuture future = new CompletableFuture();
        future.complete("four:"+hello);
        return future;
    }

    @Override
    public CompletableFuture<Integer> getAge() {
        CompletableFuture future = new CompletableFuture();
        future.complete(1);
        return future;
    }
}
