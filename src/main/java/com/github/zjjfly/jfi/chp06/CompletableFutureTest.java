package com.github.zjjfly.jfi.chp06;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * JDK8之前的Future API缺少方便的告诉future当其结果可用时进行的后续操作.8引入了CompletableFuture解决了这个问题
 *
 * @author zjjfly
 */
public class CompletableFutureTest {
    /**
     * 处理单个CompletableFuture的方法
     */
    @Test
    public void singleCompletableFutureOperation() {
        CompletableFuture<String> future = readPage("www.baidu.com");
        CompletableFuture<List<String>> links = future.thenApply(CompletableFutureTest::getLinks);
        //以Async结尾的是在另一个线程执行操作
        CompletableFuture<List<String>> links2 = readPage("www.sina.com")
                .thenApplyAsync(CompletableFutureTest::getLinks);
        //thenAccept同thenApply类似,但传入的函数的返回类型是Void
        links.thenAccept(list -> list.forEach(System.out::println));
        //thenCompose用于处理结果并返回另一个CompletableFuture,用于把多个返回CompletableFuture的方法组合
        links.thenCompose(CompletableFutureTest::log);
        //handle用于处理异常或结果
        links2.handle((list, throwable) -> {
            if (null != throwable) {
                throwable.printStackTrace();
                return false;
            }
            return true;
        });
        //thenRun执行一个返回类型是Void的无参函数
        links2.thenRun(() -> System.out.println("hello"));
    }

    @Test
    public void multiplyCompletableFutureOpertaion() {
        //thenCombine用于执行两个future,并把他们的结果按照指定函数组合起来.返回的是一个新的future
        CompletableFuture.supplyAsync(() -> "1")
                         .thenCombine(CompletableFuture.supplyAsync(() -> "2"), (s, s2) -> s + "," + s2);
        //thenAcceptBoth,类似thenCombine,但传入的函数的返回值类型是Void
        CompletableFuture.supplyAsync(() -> "1")
                         .thenAcceptBoth(
                                 CompletableFuture.supplyAsync(() -> "2"),
                                 (s, s2) -> System.out.println(s + "," + s2));
        //runAfterBoth,等两个future执行完成之后,执行一个Runnable
        CompletableFuture.supplyAsync(() -> "1")
                         .runAfterBoth(CompletableFuture.supplyAsync(() -> "2"), () -> System.out.println("complete!"));
        //applyToEither,用于两个future任意一个执行完成之后,对其结果进行操作
        CompletableFuture.supplyAsync(() -> "1")
                         .applyToEither(CompletableFuture.supplyAsync(() -> "1"), Function.identity());
        //acceptEither,类似acceptEither,但传入的函数的返回值类型是Void
        CompletableFuture.supplyAsync(() -> "1")
                         .acceptEither(CompletableFuture.supplyAsync(() -> "1"), System.out::println);
        //runAfterEither,类似applyToEither,但传入的函数是无参的
        CompletableFuture.supplyAsync(() -> "1")
                         .runAfterEither(CompletableFuture.supplyAsync(() -> "1"), System.out::println);
        //返回一个future,当所有传入的future完成之后这个future才算完成
        CompletableFuture.allOf(CompletableFuture.supplyAsync(() -> "1"),CompletableFuture.supplyAsync(() -> "2"));
        //返回一个future,当任意一个传入的future完成之后这个future就算完成
        CompletableFuture.anyOf(CompletableFuture.supplyAsync(() -> "1"),CompletableFuture.supplyAsync(() -> "2"));
    }

    private static CompletableFuture<String> readPage(String url) {
        return CompletableFuture.supplyAsync(() -> "hello");
    }

    private static List<String> getLinks(String content) {
        //需要有返回数据的时候,使用supplyAsync
        if (null == content || content.isEmpty()) {
            throw new IllegalArgumentException("content cannot be empty");
        }
        return new ArrayList<String>() {{
            add("1");
            add("2");
        }};
    }

    private static CompletableFuture<Void> log(List<String> links) {
        //不需要有返回数据,只需要按一个接一个执行,使用runAsync
        return CompletableFuture.runAsync(() -> System.out.println(links));
    }

}
