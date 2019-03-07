package io.four;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestComplete {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture future = new CompletableFuture();
        future.complete(null);
        //future.isDoe();
        System.out.println(future.isDone());
        System.out.println( future.get());
        System.out.println(future.get());
    }
}
