package com.github.zjjfly.jfi.chp06;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 新加入的数组并行操作
 *
 * @author zjjfly
 */
public class ParallelArrayOperationTest {
    @Test
    public void sort() throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("build.gradle")), StandardCharsets.UTF_8);
        String[] words = contents.split("[\\P{L}]+");
        Arrays.parallelSort(words, Comparator.reverseOrder());
        //可以指定排序的元素范围
        Arrays.parallelSort(words, 0, 10);
    }

    @Test
    public void setAll() {
        int[] ints = new int[10];
        //第二个参数是参数是index的generator,
        Arrays.parallelSetAll(ints, i -> i % 5);
        Arrays.stream(ints).forEach(System.out::println);
    }

    @Test
    public void prefix() {
        int[] ints = {1,2,3,4,5,6,7,8,9};
        Arrays.parallelPrefix(ints,(left, right) -> left*right);
        Arrays.stream(ints).forEach(System.out::println);
    }
}
