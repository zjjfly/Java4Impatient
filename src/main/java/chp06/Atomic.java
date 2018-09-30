package chp06;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author zjjfly
 */
public class Atomic {
    public static final int MAX_THREAD_SIZE = 3000;
    /**
     * Java5开始引入的java.util.concurrent.atomic包
     */
    private static AtomicLong largest = new AtomicLong();
    /**
     * 如果有大量线程会访问原子值,由于乐观锁需要太多次尝试,因此可以尝试使用悲观锁
     * 那么可以使用Java加入的LongAdder和LongAccumulator
     * LongAdder由多个变量组成,这些变量累加的值才是当前值,所以多个线程可以同时更新不同的被加数
     * LongAccumulator把这个思想带到了任意的累加操作,你需要在构造函数中提供操作类型和起始值
     */
    static LongAdder adder = new LongAdder();
    /**
     * LongAccumulator把这个思想带到了任意的累加操作,你需要在构造函数中提供操作类型op和中立值a
     * 它内部包含a1,a2...an等多个变量,每个变量都被初始化为中立值
     * 当调用accumulate(v)累加值时,这些变量其中之一会变为ai=op(ai,v).
     * 当调用get()时,返回的结果是op(a1,op(a2,op(a3,....(an-2,op(an-1,an))))))
     * 所以,op操作必须是符合结合律和交换律的
     * Java8还提供了DoubleAdder和DoubleAccumulator
     */
    static LongAccumulator addor = new LongAccumulator(Long::sum, 0L);
    static long observed = 0L;

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(
                MAX_THREAD_SIZE,
                new BasicThreadFactory.Builder()
                        .namingPattern(
                                "my-thread-%d")
                        .daemon(true)
                        .build());
        for (int i = 0; i < 10; i++) {
            poolExecutor.submit(() -> {
                //下面的代码有问题,因为set和Math.max都不是原子的
                long l = largest.incrementAndGet();
                observed = l;
                largest.set(Math.max(largest.get(), l));
            });
        }
        for (int i = 0; i < 10; i++) {
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
                //largest.updateAndGet(x -> Math.max(x,observed));
                //largest.accumulateAndGet(observed, Math::max);
                //如果想要返回更新之前的值,使用getAndUpdate,getAndAccumulate
            });
        }
        for (int i = 0; i < MAX_THREAD_SIZE; i++) {
            poolExecutor.submit(adder::increment);
        }
        for (int i = 0; i < MAX_THREAD_SIZE; i++) {
            poolExecutor.submit(() -> {
                addor.accumulate(1);
            });
        }
        poolExecutor.shutdown();
        System.out.println(adder.sum());
        System.out.println(addor.get());
    }
}
