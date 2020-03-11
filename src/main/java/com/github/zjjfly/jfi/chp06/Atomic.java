package com.github.zjjfly.jfi.chp06;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author zjjfly
 */
public class Atomic {
    private static final int MAX_THREAD_SIZE = 3000;
    private static final int THREAD_COUNT = 30000;
    /**
     * Java5开始引入的java.util.concurrent.atomic包
     */
    AtomicLong largest;

    ScheduledThreadPoolExecutor poolExecutor;


    @Before
    public void init() {
        largest = new AtomicLong();
        poolExecutor = new ScheduledThreadPoolExecutor(
                MAX_THREAD_SIZE,
                new BasicThreadFactory.Builder()
                        .namingPattern(
                                "my-thread-%d")
                        .daemon(true)
                        .build());
    }

    @Test
    public void raceCondition() throws InterruptedException {
        long[] observed = {0};
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(() -> {
                //下面的代码有问题,因为set和Math.max都不是原子的
                long l = largest.incrementAndGet();
                observed[0] = l;
                largest.set(Math.max(largest.get(), l));
            });
        }
        Thread.sleep(3000L);
        assertNotEquals(THREAD_COUNT, largest.get());
        largest.set(0);
        observed[0] = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(() -> {
                //纠正上面的问题
                //java8之前:
                //Long oldValue;
                //Long newValue;
                //do {
                //    oldValue=largest.get();
                //    newValue=Math.max(oldValue,observed);
                //}while (!largest.compareAndSet(newValue,oldValue));

                //Java8做法
                largest.updateAndGet(x -> Math.max(x, observed[0]));
//              largest.accumulateAndGet(observed[0], Math::max);
                //如果想要返回更新之前的值,使用getAndUpdate,getAndAccumulate
            });
        }
        assertNotEquals(THREAD_COUNT, largest.get());
        poolExecutor.shutdown();
    }

    @Test
    public void longAccumulator() throws InterruptedException {
        /**
         * 如果有大量线程会访问原子值,由于乐观锁需要太多次尝试,因此可以尝试使用悲观锁
         * 那么可以使用Java加入的LongAdder和LongAccumulator
         * LongAdder由多个变量组成,这些变量累加的值才是当前值,所以多个线程可以同时更新不同的被加数
         * LongAccumulator把这个思想带到了任意的累加操作,你需要在构造函数中提供操作类型和起始值
         */
        LongAdder adder = new LongAdder();
        /**
         * LongAccumulator把这个思想带到了任意的累加操作,你需要在构造函数中提供操作类型op和中立值a
         * 它内部包含a1,a2...an等多个变量,每个变量都被初始化为中立值
         * 当调用accumulate(v)累加值时,这些变量其中之一会变为ai=op(ai,v).
         * 当调用get()时,返回的结果是op(a1,op(a2,op(a3,....(an-2,op(an-1,an))))))
         * 所以,op操作必须是符合结合律和交换律的
         * Java8还提供了DoubleAdder和DoubleAccumulator
         */
        LongAccumulator addor = new LongAccumulator(Long::sum, 0L);
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(adder::increment);
        }
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(() -> {
                addor.accumulate(1);
            });
        }
        Thread.sleep(2000L);
        assertEquals(THREAD_COUNT, adder.sum());
        assertEquals(THREAD_COUNT, addor.get());
        poolExecutor.shutdown();
    }

}
