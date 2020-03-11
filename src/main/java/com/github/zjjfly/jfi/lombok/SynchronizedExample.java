package com.github.zjjfly.jfi.lombok;

/**
 * Synchronized和Java中的Synchronized关键字不同的是,它使用一个锁对象来加锁
 * @author zjjfly
 */
public class SynchronizedExample {
    @Synchronized
    public static void hello() {
        System.out.println("world");
    }

    @Synchronized
    public int answerToLife() {
        return 42;
    }

    @Synchronized
    public void foo() {
        System.out.println("bar");
    }
}
