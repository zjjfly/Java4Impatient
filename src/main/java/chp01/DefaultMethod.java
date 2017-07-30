package chp01;

import java.util.Comparator;

public class DefaultMethod {
    public static void main(String[] args) {
        //使用静态比较方法，这是一个高阶函数，返回的也是一个函数
        Comparator<String> comp= Comparator.comparingInt(String::length);

    }
    interface Person{
        long getId();
        //默认方法
        default String getName(){return "JJZI";}
    }
    interface Named{
        default String getName(){return getClass().getName()+"_"+hashCode();}
    }
    class Student implements Person,Named{
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
