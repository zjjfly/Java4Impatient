package chp02;

import java.util.Optional;
import java.util.stream.Stream;

public class Reduction {
    public static void main(String[] args) {
        Stream<Integer> values = Stream.of(1, 2, 3, 4, 5);
        //Optional<Integer> sum = values.reduce((x, y) -> x + y);
        //另一种写法
//        Optional<Integer> sum=values.reduce(Integer::sum);
        Integer sum = values.reduce(0, Integer::sum);
        assert sum.equals(Optional.of(15));
        Stream<String> words = Stream.of("what", "which", "where");
        Integer result = words.reduce(0, (s, word) -> s + word.length(), (total, subtotal) -> total + subtotal);
        words = Stream.of("what", "which", "where");
        //实践中reduce用的不是恩多，更简单的方法是映射到数字流然后调用数字流的sum,max,min这些方法
        assert result==words.mapToInt(String::length).sum();
    }
}
