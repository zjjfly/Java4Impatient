package chp02;

import common.City;
import common.StreamFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelStream {
    public static void main(String[] args) {
        //生成并行流的方法
        Stream<Integer> parallelIntegers = Arrays.asList(1, 2).parallelStream();
        Stream<String> parallelWords = Stream.of("1", "2", "3").parallel();
        //并行流的操作必须是无状态的，顺序无关的
        //下面是一个反例
        int[] shortWords = new int[12];
        try (Stream<String> words = Files.lines(Paths.get("gradlew"))
                                         .flatMap(s -> Arrays.stream(s.split("[\\P{L}]+")))) {
            words.parallel().forEach(s -> {
                if (s.length() < 12) {
                    shortWords[s.length()]++;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //利用unordered标记无序和并行流来加速limit
        Stream<Integer> stream = StreamFactory.intStream();
        Stream<Integer> sample = stream.parallel().unordered().limit(10);
        //groupingByConcurrent天生是无序的，所以不需要使用unordered标记
        Stream<City> cities = StreamFactory.cityStream();
        ConcurrentMap<String, List<City>> re = cities.parallel()
                                                     .collect(Collectors.groupingByConcurrent(City::getState));
        //流生成之后，改变原来的集合，流也会跟着改变
        List<String> wordList = new ArrayList() {{
            add("BEGIN");
            add("RUNNING");
        }};
        Stream<String> words = wordList.stream();
        wordList.add("END");
        assert 3 == words.distinct().count();

        //
        words = wordList.stream();
        words.forEach(s -> {
            if (s.length() < 7)
                wordList.remove(s);
        });
    }
}
