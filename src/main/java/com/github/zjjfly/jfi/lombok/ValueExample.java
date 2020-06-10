package com.github.zjjfly.jfi.lombok;

import lombok.AccessLevel;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.experimental.Wither;

/**
 * @author zjjfly
 */
@Value
public class ValueExample {
    String name;
    //生成一个以with开头的clone方法
    @Wither(AccessLevel.PACKAGE)
    //NonFinal标注的字段可以修改
    @NonFinal
    int age;
    double score;
    protected String[] tags;

    @ToString(includeFieldNames = true)
    @Value(staticConstructor = "of")
    public static class Exercise<T> {
        String name;
        T value;
    }

    public static void main(String[] args) {
        ValueExample jjzi = new ValueExample("jjzi", 28, 99, args);
        ValueExample clone = jjzi.withAge(29);
        System.out.println(clone);
        //staticConstructor指定的工厂方法
        Exercise<Integer> exercise = Exercise.of("jjzi", 12);
    }
}
