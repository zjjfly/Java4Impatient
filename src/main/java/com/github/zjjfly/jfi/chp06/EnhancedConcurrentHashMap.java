package com.github.zjjfly.jfi.chp06;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.*;

/**
 * Java8对ConcurrentHashMap做了很多增强.比如,把底层的数据结构换成了红黑树
 *
 * @author zjjfly
 */
public class EnhancedConcurrentHashMap {
    private static final int MAX_THREAD_SIZE = 3000;
    private static final Integer THREAD_COUNT = 10000;
    ScheduledThreadPoolExecutor poolExecutor;

    @Before
    public void init() {
        poolExecutor = new ScheduledThreadPoolExecutor(
                MAX_THREAD_SIZE,
                new BasicThreadFactory.Builder()
                        .namingPattern(
                                "my-thread-%d")
                        .daemon(true)
                        .build());
    }

    /**
     * ConcurrentHashMap的原子性问题
     */
    @Test
    public void concurrentMapProblem() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(100);
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(() -> {
                //取出某个key对应的值,其他线程可能立即就修改了这个值,使用这个值再去计算需要放入的新值就会出错
                Integer oldValue = map.get("count");
                int newValue = oldValue + 1;
                map.put("count", newValue);
            });
        }
        Thread.sleep(1000L);
        assertNotEquals(THREAD_COUNT, map.get("count"));
        //如果包含的键值对太多,int可能会不足以表达,Java8加入了mappingCount用与此(size方法返回的是int),它返回的是long类型
        long mapSize = map.mappingCount();
        poolExecutor.shutdown();
    }

    /**
     * 解决上述问题的措施,类似Atomic的🌰
     *
     * @throws InterruptedException
     */
    @Test
    public void resolution1() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(100);
        map.putIfAbsent("count", 0);
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(() -> {
                Integer oldValue;
                Integer newValue;
                do {
                    oldValue = map.get("count");
                    newValue = oldValue + 1;
                } while (!map.replace("count", oldValue, newValue));
            });
        }
        Thread.sleep(1000L);
        assertEquals(THREAD_COUNT, map.get("count"));
        poolExecutor.shutdown();
    }

    /**
     * 还有一种解决方法
     *
     * @throws InterruptedException
     */
    @Test
    public void resolution2() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> map = new ConcurrentHashMap<>(100);
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(() -> map.putIfAbsent("count", new LongAdder()).increment());
        }
        Thread.sleep(2000L);
        assertEquals((int) THREAD_COUNT, map.get("count").intValue() + 1);
        poolExecutor.shutdown();
    }

    /**
     * 另一种解决方法
     *
     * @throws InterruptedException
     */
    @Test
    public void resolution3() throws InterruptedException {
        ConcurrentHashMap<String, LongAdder> map = new ConcurrentHashMap<>(100);
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(() -> map.computeIfAbsent("count", s -> new LongAdder()).increment());
        }
        Thread.sleep(1000L);
        assertEquals((int) THREAD_COUNT, map.get("count").sum());
        poolExecutor.shutdown();
    }

    /**
     * 更简单的解决方法,使用JDK8新加入的merge方法
     *
     * @throws InterruptedException
     */
    @Test
    public void simplerSolution1() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(100);
        for (int i = 0; i < THREAD_COUNT; i++) {
            poolExecutor.submit(() -> {
                map.merge("count", 1, (existVal, defaultVal) -> existVal + 1);
            });
        }
        Thread.sleep(1000L);
        assertEquals(THREAD_COUNT, map.get("count"));
        poolExecutor.shutdown();
    }

    /**
     * merge和compute传入的方法的返回值如果是null,则已有的entry会从map中删除
     * 这个方法不应该是耗时操作,也不应该修改map的其他entry
     */
    @Test
    public void attention1() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.putIfAbsent("count", 1);
        map.computeIfPresent("count", (s, integer) -> null);
        assertNull(map.get("count"));
    }

    /**
     * 批量操作,不会进行锁,所以它们的结果只能看做是一个近似结果
     */
    @Test
    public void batchOperation() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(3);
        map.put("java", 1);
        map.put("groovy", 2);
        map.put("scala", 3);
        //search方法返回的是查询函数返回非null的第一个值
        //第一个参数表示map元素超过多少个的时候把操作并行化
        assertEquals("java,1", map.search(1, (l, i) -> "java".equals(l) ? l + "," + i : null));
        assertTrue(map.searchKeys(1, k -> "groovy".equals(k) ? true : null));
        assertTrue(map.searchValues(1, v -> v == 2 ? true : null));
        assertTrue(map.searchEntries(1, entry -> entry.getKey().length() > entry.getValue() ? true : null));

        //reduce及其变体
        assertEquals(6, (int) map.reduce(1, (l, i) -> i, (i1, i2) -> i1 + i2));
        assertEquals(6, (int) map.reduceEntries(1, Map.Entry::getValue, (i1, i2) -> i1 + i2));
        assertEquals(6, (int) map.reduceValues(1, (i1, i2) -> i1 + i2));
        assertEquals("java,groovy,scala", map.reduceKeys(1, (l1, l2) -> l1 + "," + l2));
        //reduce可以传入一个转换器
        assertEquals(15, (int) map.reduceKeys(1, String::length, (integer, integer2) -> integer + integer2));
        //如果map是空的,则返回null
        assertNull(new ConcurrentHashMap<String, Integer>().reduceValues(1, (i1, i2) -> i1 + i2));
        //如果map只有一个entry,那么返回的只是经过转换器转换的值,不会应用累加器
        assertEquals(4, (int) new ConcurrentHashMap<String, Integer>(1) {{
            put("java", 2);
        }}.reduceValues(1, i -> i * i, (i1, i2) -> i1 + i2));
        //对于int,long,double类型的输出,有专门的方法,第三个函数是一个累加的初始值,在并行的时候每个线程都会使用这个初始值,所以需要慎重
        assertEquals(7, map.reduceToInt(4, (k, v) -> v, 1, (left, right) -> left + right));

        //forEach及其变体,都有两种形式:带转换器和不带转换器
        map.forEach(1, (k, v) -> System.out.println(k + "->" + v));
        map.forEach(1, (k, v) -> k + "->" + v, System.out::println);
        //转换器可以作为filter使用,当转换器返回null,这个值会被跳过
        map.forEach(1, (k, v) -> v > 1 ? k : null, System.out::println);
    }

    /**
     * set视图
     */
    @Test
    public void setView() {
        //set视图实际是一个ConcurrentHashMap的封装,所有映射的值是Boolean.TRUE
        Set<String> setView = ConcurrentHashMap.newKeySet();
        //之前map的keySet方法返回的set,如果你删除其中的元素,map中对应的值也会删除,但是如果添加元素,则无法添加对应的值
        //JDK8加入了一个新的keySet方法,传入一个默认的映射值
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        Set<String> keySet = map.keySet(1);
        keySet.add("java");
        assertEquals(1, (int) map.get("java"));
    }
}
