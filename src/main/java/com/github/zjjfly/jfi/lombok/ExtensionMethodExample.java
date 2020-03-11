package com.github.zjjfly.jfi.lombok;

import com.github.zjjfly.jfi.lombok.experimental.ExtensionMethod;

import java.util.Arrays;

/**
 * @author zjjfly
 */
//@ExtensionMethod把它的参数指定的类中的所有public static的至少有一个参数且参数类型(可以是原始类型数组)不是原始类型的方法作为第一个参数的类型的扩展方法
@ExtensionMethod({Arrays.class,Extensions.class})
public class ExtensionMethodExample {
    public static void main(String[] args) {
        int [] numbers={1,4,2,3};
        //Arrays中的sort的第一个参数是数组类型,所以现在所有的数组都可以使用sort方法了
        System.out.println("first sort:");
        numbers.sort(1,3);
        numbers.stream().forEach(System.out::println);
        System.out.println("second sort:");
        numbers.sort();
        numbers.stream().forEach(System.out::println);
        numbers.extension();
    }
}


