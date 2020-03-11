package com.github.zjjfly.jfi.chp01;

import java.util.Comparator;

/**
 * @author zjjfly
 */
public class DefaultMethod {
    public static void main(String[] args) {
        //使用静态比较方法，这是一个高阶函数，返回的也是一个函数
        Comparator<String> comp= Comparator.comparingInt(String::length);

    }
    interface Person{
        /**
         * 获取id
         * @return id
         */
        long getId();

        /**
         * 默认方法,获取名字
         * @return 名字
         */
        default String getName(){return "JJZI";}
    }
    interface Named{
        /**
         * 获取名称
         * @return 名字
         */
        default String getName(){return getClass().getName()+"_"+hashCode();}
    }
    static class Student implements Person,Named{
        @Override
        public long getId() {
            return 0;
        }

        @Override
        public String getName() {
            return Person.super.getName();
        }
    }

}
