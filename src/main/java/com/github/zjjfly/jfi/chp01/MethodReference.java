package com.github.zjjfly.jfi.chp01;

import java.util.Arrays;

/**
 * @author zjjfly
 */
public class MethodReference {
    public static void main(String[] args) {
        //不使用方法引用
        Arrays.stream(new int[]{1,2,3}).forEach(i-> System.out.println(i));
        //使用方法引用
        Arrays.stream(new int[]{1,2,3}).forEach(System.out::println);
        Arrays.sort(new String[]{"a","bcd","ef"},String::compareToIgnoreCase);
        //引用包裹类型的实例方法
        ConcurrentGreeter concurrentGreeter = new ConcurrentGreeter();
        ConcurrentGreeter.CcGreeter cGreceter = concurrentGreeter.new CcGreeter();
        cGreceter.greet();
    }

}
