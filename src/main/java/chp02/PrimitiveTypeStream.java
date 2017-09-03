package chp02;

import common.StreamFactory;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PrimitiveTypeStream {
    public static void main(String[] args) {
        //产生原生类型流的静态方法
        IntStream stream = IntStream.of(1, 2, 3, 5);
        int[] values = {1, 2, 3, 4};
        stream = Arrays.stream(values);
        //产生整数数列的方法
        IntStream zeroToNinetyNine = IntStream.range(1, 100);
        IntStream zeroToHundred = IntStream.rangeClosed(1, 100);
        //把字符串转换为unicode字符和utf-16字符单元的方法
        String sentence = "\uD835\uDD46 is the set of octonions";
        IntStream codes = sentence.codePoints();
        IntStream utf16Codes = sentence.chars();
        //对象流转换成原生类型流
        Stream<String> words = StreamFactory.stringStream();
        IntStream lengths = words.mapToInt(String::length);
        //原生类型流转换成对应的包装对象流
        Stream<Integer> integers = IntStream.range(0, 100).boxed();
        //原生类型流和对象流的区别：
        //1.原生类型流toArray返回的是类型数组，对象流的toArray方法返回的是对象数组
        int[] ints = IntStream.of(1, 2).toArray();
        //2.原生类型流返回的可选类型是Optional(Int|Double|Long),它们没有get方法，而是getAs(Int|Double|Long)方法
        int biggerThanOne = IntStream.of(1, 2).filter(i -> i > 1).findAny().getAsInt();
        //3.原生类型流有max，min，sum，average方法
        IntStream.of(1,2,3).sum();
        IntStream.of(1,2,3).average();
        IntStream.of(1,2,3).max();
        IntStream.of(1,2,3).min();
        //4.原生类型的summaryStatistics返回(Int|Double|Long)SummaryStatistics，可以获取流的和，平均值，最大值，最小值
        IntSummaryStatistics statistics = IntStream.of(1, 2, 3).summaryStatistics();
        statistics.getAverage();
        statistics.getSum();
        statistics.getCount();
        statistics.getMax();
        statistics.getMin();
        //生成随机数的原生类型流
        Random random = new Random();
        IntStream randomInts = random.ints();
        DoubleStream randomDoubles = random.doubles();
        LongStream randomLongs = random.longs();
    }
}
