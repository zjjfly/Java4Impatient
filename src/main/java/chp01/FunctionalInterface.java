package chp01;

import java.util.function.BiFunction;

/**
 * @author zjjfly
 */
public class FunctionalInterface {

    //对于函数式接口，最好都加上@FunctionalInterface注释。它会检查这个接口是否符合函数式接口的标准。
    @java.lang.FunctionalInterface

    interface MyFuncInterface {
        /**
         * 只有一个抽象方法的接口被称为函数式接口
         * 完成某种工作
         */
        void doWork();
    }

    public static void main(String[] args) {
        BiFunction<String, String, Integer> comp = (first, second) -> Integer.compare(first.length(), second.length());
//        下面的代码会报错，因为lambda表达式会抛出检查时异常，但Runnable.run方法是不能抛出异常的，所以这个赋值是不合法的
//        Runnable sleep=() -> {
//            System.out.println("Zzz");
//            Thread.sleep(1000);
//        };
    }
}
