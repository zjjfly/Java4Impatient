package com.github.zjjfly.jfi.chp02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author zjjfly
 */
public class StatefulTransform {
    public static void main(String[] args) {
        Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently").distinct();
        try (Stream<String> lines = Files.lines(Paths.get("gradlew"))) {
            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                Stream<String> words = Stream.of(line.split("[\\P{L}]+"));
                Stream<String> sortedWords = words.sorted(Comparator.comparingInt(String::length).reversed());
                Optional<String> largest = sortedWords.max(String::compareToIgnoreCase);
                if (largest.isPresent()) {
                    System.out.println("largest:"+largest.get());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stream<String> words = Stream.of("add Queen To Castle".split(" "));
        //findFirst通常和filter搭配使用
        Optional<String> startWithQ1 = words.filter(s -> s.startsWith("Q")).findFirst();
        //findAny和并行流搭配使用效率高
        Optional<String> startWithQ2 = words.parallel().filter(s -> s.startsWith("Q")).findAny();
        boolean aWordStartsWithQ = words.parallel().anyMatch(s -> s.startsWith("Q"));
    }
}
