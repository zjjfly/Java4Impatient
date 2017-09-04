package chp02;

import common.Person;
import common.StreamFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectResult {
    public static void main(String[] args) {
        Stream<String> words = StreamFactory.stringStream();
        String[] result = words.toArray(String[]::new);
        Stream<Integer> nums = StreamFactory.intStream();
        HashSet<Integer> integers = nums.collect(HashSet::new, HashSet<Integer>::add, HashSet::addAll);
        //更简单的写法，利用Collectors中已经实现好的收集器
        List<Integer> list = StreamFactory.intStream().collect(Collectors.toList());
        Set<Integer> set = StreamFactory.intStream().collect(Collectors.toSet());
        //如果想要控制返回的set的类型，使用Collectors.toCollection
        TreeSet<Integer> treeSet = StreamFactory.intStream().collect(Collectors.toCollection(TreeSet::new));
        //字符串连接用Collectors.joining，可以传入一个分隔符
        String join = StreamFactory.stringStream().collect(Collectors.joining());
        String delimiter = StreamFactory.stringStream().collect(Collectors.joining("，"));
        String persons = StreamFactory.objectStream().map(Person::toString)
                                      .collect(Collectors.joining(","));
        //Collectors.summarizingInt产生一个统计对象，可以获取和，平均值，最大最小值
        IntSummaryStatistics statistics = StreamFactory.stringStream().collect(Collectors.summarizingInt(String::length));
        double averageLength = statistics.getAverage();
        int maxLength = statistics.getMax();
    }
}
