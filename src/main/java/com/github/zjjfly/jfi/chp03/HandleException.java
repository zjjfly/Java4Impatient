package com.github.zjjfly.jfi.chp03;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author zjjfly
 */
public class HandleException {

    //函数式接口通常不允许检查期异常,很不方便.
    // 所以你的方法可以选择那些允许检查期异常的函数式接口.
    //也可以用下面的方法把一个可能抛出检查型异常的函数变为一个抛出非检查型异常的函数

    static <T> Supplier<T> unchecked(Callable<T> f) {
        return () -> {
            try {
                return f.call();
            } catch (Exception e) {
                //把所有的Exception变为运行时异常抛出
                throw new RuntimeException(e);
            } catch (Throwable t) {
                //所有的Error直接抛出
                throw t;
            }
        };
    }
}
