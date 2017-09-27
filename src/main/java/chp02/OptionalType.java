package chp02;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalType {
    public static void main(String[] args) {
        //生成Optional
        Optional<String> optional = Optional.of("s");
        assert optional.get().equals("s");
        //生成空的Optional
        assert !Optional.empty().isPresent();
        Stream<String> words = Stream.of("add Queen To Castle".split(" "));
        Optional<String> optionalValue = words.filter(s -> s.startsWith("Q")).findFirst();
        //错误的使用Optional的例子
        //optionalValue.get().toLowerCase();//如果Optional没有值，还是会抛出异常
        //下面的代码并不比以前的先判断对象是否是null然后进行处理这样的代码更简单
        if (optionalValue.isPresent()) {
            System.out.println(optionalValue.get());
        }
        //正确的使用Optional的两种方式
        //第一种使用只在Optional有值时才执行的方法
        optionalValue.ifPresent(System.out::println);
        List<String> results = new ArrayList<>();
        //如果需要处理结果，使用map
        Optional<Boolean> added = optionalValue.map(results::add);
        //第二种是使用在Optional没有值的时候会产生替代值的方法
        Optional<String> optionalString = Stream.of("Aboout", "Brave", "Cute").parallel().filter(s -> s.startsWith("Q"))
                                                .findAny();
        //orElse方法设定默认值
        String result = optionalString.orElse("");
        assert result.equals("");
        //orElseGet方法调用代码块产生默认值
        result = optionalString.orElseGet(() -> System.getProperty("user.dir"));
        assert result.equals(System.getProperty("user.dir"));
        //orElseThrow设定没有值的时候抛出的异常
        try {
            optionalString.orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
        }
        //使用flatMap组合可选值的方法
        Optional<Double> x = inverse(4.0).flatMap(OptionalType::squre);
        assert x.equals(Optional.of(4.0).flatMap(OptionalType::inverse).flatMap(OptionalType::inverse));
    }

    private static Optional<Double> inverse(Double x) {
        return x == 0 ? Optional.empty() : Optional.of(1 / x);
    }

    private static Optional<Double> squre(Double x) {
        return x == 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }
}
