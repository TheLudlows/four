package io.four.invoke;

import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<String>  getName(String hello);

    CompletableFuture<Integer> getAge();
}
