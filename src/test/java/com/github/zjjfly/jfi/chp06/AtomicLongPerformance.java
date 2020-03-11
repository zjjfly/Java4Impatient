package com.github.zjjfly.jfi.chp06;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@State(Scope.Group)
@BenchmarkMode({Mode.AverageTime})
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@Fork(10)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class AtomicLongPerformance {
    private AtomicLong counter;

    @Setup
    public void init() {
        counter = new AtomicLong();
    }

    @Benchmark
    @Group("g")
    @GroupThreads(1000)
    public void run() {
        for (int i = 0; i < 1000; i++) {
            counter.incrementAndGet();
        }
    }

    @Benchmark
    @Group("g")
    @GroupThreads()
    public long get() {
        return counter.get();
    }
}
