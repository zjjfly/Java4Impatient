package com.github.zjjfly.jfi.chp06;

import org.junit.Test;

import java.net.PasswordAuthentication;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Exercise {
    static Random random = new Random();

    @Test
    public void ex11() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println(repeat(this::getPassword, this::validate).get(60, TimeUnit.SECONDS));
    }

    private PasswordAuthentication getPassword() {
        return new PasswordAuthentication("jjzi", String.valueOf(random.nextInt(1000)).toCharArray());
    }

    private boolean validate(PasswordAuthentication authentication) {
        System.out.println("begin to validate authentication...");
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String passwd = new String(authentication.getPassword());
        return Integer.parseInt(passwd) > 900 && authentication.getUserName().equals("jjzi");
    }

    private static <T> CompletableFuture<T> repeat(Supplier<T> action, Predicate<T> util) {
        return CompletableFuture.supplyAsync(action).thenComposeAsync(t -> {
            if (!util.test(t)) {
                return repeat(action, util);
            } else {
                return CompletableFuture.completedFuture(t);
            }
        });
    }
}
