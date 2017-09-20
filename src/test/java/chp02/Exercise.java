package chp02;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

public class Exercise {
    @Test
    public void exer1() throws IOException, InterruptedException, ExecutionException {
        int coreNums = Runtime.getRuntime().availableProcessors();
        List<String> wordLines = Files.readAllLines(Paths.get("gradlew"));
        HashMap<Integer, List<String>> core2Words = new HashMap<>();
        for (int i = 0; i < wordLines.size(); i++) {
            List<String> words = core2Words.get(i % coreNums);
            if (null == words) {
                words = new ArrayList<>();
                core2Words.put(i % 8, words);
            }
            words.addAll(Arrays.asList(wordLines.get(i).split("[\\P{L}]+")));
        }
        ExecutorService executorService = Executors.newFixedThreadPool(coreNums);
        List<Callable<Integer>> callables = new ArrayList<>();
        for (List<String> words : core2Words.values()) {
            Callable<Integer> callable = () -> {
                int count = 0;
                for (String word : words) {
                    if (word.length() > 12) count++;
                }
                return count;
            };
            callables.add(callable);
        }
        List<Future<Integer>> futures = executorService.invokeAll(callables);
        int count = 0;
        for (Future<Integer> f : futures) {
            int n = f.get();
            count += n;
        }
        assertEquals(count, 5);
    }

    @Test
    public void exer2() throws Exception {
        Files.lines(Paths.get("gradlew")).flatMap(Pattern.compile("[\\P{L}]+")::splitAsStream)
             .peek(s -> System.out.println("call filter on " + s))
             .filter(s -> s.length() >= 12).limit(5).collect(joining(",", "(", ")"));
    }

    @Test
    public void exer4() throws Exception {
        int[] values = {1, 4, 9, 16};
        Stream<int[]> stream = Stream.of(values);
        IntStream intStream = Arrays.stream(values);
    }

    @Test
    public void exer5() throws Exception {
        Stream<Long> longStream = randomStream(101L);
        longStream.limit(10).forEach(System.out::println);
    }

    private static Stream<Long> randomStream(long seed) {
        long a = 25214903917L;
        long c = 11L;
        long m = (long) Math.pow(2, 48);
        return Stream.iterate(seed, aLong -> (a * aLong + c) % m);
    }

    @Test
    public void exer6() throws Exception {
        Stream<Character> stream = characterStream("What");
        stream.forEach(System.out::println);
    }

    private static Stream<Character> characterStream(String s) {
        Stream<Integer> stream = Stream.iterate(0, i -> i + 1).limit(s.length());
        return stream.map(s::charAt);
    }

    @Test
    public void exer8() throws Exception {
        Stream<Integer> first = Stream.of(1, 3, 5, 7);
        Stream<Integer> second = Stream.of(2, 4, 6, 8);
        Stream<Integer> zip = zip(first, second);
        zip.forEach(System.out::println);
    }

    private static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Iterator<T> secondIterator = second.iterator();
        Stream.Builder<T> builder = Stream.builder();
        first.forEach(t -> {
            if (secondIterator.hasNext()) {
                builder.accept(t);
                builder.accept(secondIterator.next());
            } else {
                first.close();
            }
        });
        return builder.build();
    }

    @Test
    public void exer9() throws Exception {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        List<Integer> list3 = new ArrayList<>();
        list3.add(3);
        Stream<List<Integer>> listStream = Stream.of(list1, list2, list3);
        //solution1
        listStream.reduce((l1, l2) -> {
            ArrayList<Integer> re = new ArrayList<>(l1);
            re.addAll(l2);
            return re;
        }).orElse(new ArrayList<>());
        //solution2
        listStream = Stream.of(list1, list2, list3);
        listStream.reduce(new ArrayList<>(), (re, l) -> {
            re.addAll(l);
            return re;
        });
        //solution3
        listStream = Stream.of(list1, list2, list3);
        List<Integer> reduce = listStream.reduce(new ArrayList<>(), (re, l) -> {
            ArrayList<Integer> list = new ArrayList<>(l);
            list.addAll(re);
            return list;
        }, (re, l) -> {
            ArrayList<Integer> list = new ArrayList<>(l);
            list.addAll(re);
            return list;
        });
    }

    @Test
    public void exer10() throws Exception {
        double[] sumCount = {0, 0};
        Stream<Double> doubleStream = Stream.of(1.0, 2.3, 3.5, 1.2);
        doubleStream.reduce(sumCount, (doubles, aDouble) -> {
            doubles[0] += aDouble;
            doubles[1]++;
            return doubles;
        }, (doubles, doubles2) -> {
            doubles[0] += doubles2[0];
            doubles[1] += doubles2[1];
            return doubles;
        });
        assertEquals(new BigDecimal(sumCount[0]).divide(new BigDecimal(sumCount[1])), new BigDecimal(2.0));
    }

    @Test
    public void exer11() throws Exception {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        List<Integer> list3 = new ArrayList<>();
        list3.add(3);
        Stream<List<Integer>> listStream = Stream.of(list1, list2, list3);
        Integer[] ints = listStream.flatMap(Collection::stream).toArray(Integer[]::new);
        System.out.println(Arrays.toString(ints));
        IntStream range = IntStream.range(0, ints.length);
        //需要为list的每个元素进行初始化，否则下面的set方法会报错
        List<Integer> result = new ArrayList(Arrays.asList(new String[ints.length]));
        range.parallel().forEach(i -> result.set(i, ints[i]));
        System.out.println(result);
    }

    @Test
    public void exer12() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("War and Peace.txt");
        assert resource != null;
        URI uri = resource.toURI();
        List<String> words = Files.lines(Paths.get(uri)).flatMap(Pattern.compile("[\\P{L}]+")::splitAsStream).collect(
                toList());
        AtomicInteger[] shortWords = new AtomicInteger[12];
        for (int i = 0; i < shortWords.length; i++) {
            shortWords[i]=new AtomicInteger(0);
        }
        words.parallelStream().forEach(
                s -> {
                    if (s.length() < 12) shortWords[s.length()].getAndIncrement();
                }); // Error—race condition!
        System.out.println(Arrays.toString(shortWords));
    }

    @Test
    public void exer3() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("War and Peace.txt");
        assert resource != null;
        URI uri = resource.toURI();
        List<String> words = Files.lines(Paths.get(uri)).flatMap(Pattern.compile("[\\P{L}]+")::splitAsStream).collect(
                toList());
        ConcurrentMap<Integer, Long> result = words.parallelStream().filter(s -> s.length() < 12).collect(
                groupingByConcurrent(String::length, counting()));
        System.out.println(result);
    }
}
