package com.github.zjjfly.jfi.chp02;

import java.util.stream.Stream;

import static com.github.zjjfly.jfi.chp02.TransformMethod.characterStream;

/**
 * @author zjjfly
 */
public class ExtractAndCombine {
    public static void main(String[] args) {
        Stream<Double> randoms = Stream.generate(Math::random).limit(100);
        Stream<Character> combined = Stream.concat(characterStream("Hello"), characterStream("World"));
        Stream<String> words = Stream.of("Hello World".split("[\\P{L}]+")).skip(1);
        Object[] powers = Stream.iterate(1, integer -> integer * 2).peek(e -> System.out.println("Fetching:" + e))
                                .limit(20).toArray();
    }
}
