package com.github.zjjfly.jfi.chp02;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author zjjfly
 */
public class StreamCreate {
    static Pattern compile = Pattern.compile("[\\P{L}]+");

    public static void main(String[] args) {
        //把数组准成Stream
        String content="i am jjzi";
        Stream<String> words = Stream.of(content.split("[\\P{L}]+"));
        Stream<String> songs = Stream.of("gently", "down", "the", "stream");
        Arrays.stream(content.split("[\\P{L}]+"));
        //把数组的一部分变成Stream
        Stream<String> partWords = Arrays.stream(content.split("[\\P{L}]+"), 0, 1);
        //创建空的Stream
        Stream<String> silence = Stream.empty();
        //创建一个无限的Stream
        Stream<String> echos = Stream.generate(() -> "Echo");
        Stream<Double> randoms = Stream.generate(Math::random);
        Stream<BigInteger> integers = Stream.iterate(BigInteger.ZERO, bigInteger -> bigInteger.add(BigInteger.ONE));
        //其他生成Stream的方法
        Stream<String> splitStream = compile.splitAsStream(content);
        try(Stream<String> lines=Files.lines(Paths.get("gradlew"))) {
            System.out.println(lines.count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
