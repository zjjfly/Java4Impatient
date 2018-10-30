package chp06;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.AverageTime})
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@Fork(5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ParallelSortPerformance {
    private static int[] origin = new int[500000];
    private static final Random random = new Random();
    private int[] ints;

    static {
        for (int i = 0; i < origin.length; i++) {
            origin[i] = random.nextInt();
        }
    }

    @Setup(Level.Invocation)
    public void init() {
        ints = Arrays.copyOf(origin, 4000000);
    }

    @Benchmark
    public void sort() {
        Arrays.sort(ints);
    }

    @Benchmark
    public void parallelSort() {
        Arrays.parallelSort(ints);
    }
}
//结论:不是很好测试,结果取决于待排序的数组的情况