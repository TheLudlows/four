package io.four;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TestComplete {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture future = new CompletableFuture();
        //../////////////////////////..................,l........future.complete(null);
        //future.isDoe();
        future.whenComplete((r,t)-> System.out.println("complete"));
        System.out.println(future.isDone());
        System.out.println( future.get());
        System.out.println(future.get());
    }
}
