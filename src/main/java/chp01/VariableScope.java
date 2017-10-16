package chp01;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zjjfly
 */
public class VariableScope {
    public static void main(String[] args) {
        VariableScope variableScope = new VariableScope();
        variableScope.repeatMessage("Hello",10);
        //虽然不能给变量重新赋值，但是可以修改变量，这样做也是线程不安全的
        List<Integer> nums = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-thread-%d").build();
            ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                                                                         new LinkedBlockingDeque<>(1024), threadFactory,
                                                                         new ThreadPoolExecutor.AbortPolicy());
            singleThreadPool.execute(() -> nums.add(1));
            singleThreadPool.shutdown();
        }
        //在lambda表达式中，不能声明和局部变量同名的参数或者局部变量
//        Path first = Paths.get("/Users/zjjfly/Desktop");
//        Comparator<String> comp=(first,second)->Integer.compare(first.length(),second.length());

    }

//    下面的代码不正确，lambda不能修改捕获的自由变量
//    private void repeatMsg(String msg, int count) {
//        Runnable r=() -> {
//            while (count>0){
//                count--;
//                System.out.println(msg);
//                Thread.yield();
//            }
//        };
//        new Thread(r).start();
//    }

    private void repeatMessage(String msg, int count){
        //实现一个局部计数器
        int[] counter={0};
        Runnable runnable=() -> {
            for (int i = 0; i < count; i++) {
                System.out.println(msg);
                counter[0]++;
                Thread.yield();
            }
        };
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-thread-%d").build();
                ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                                                                             new LinkedBlockingDeque<>(1024), threadFactory,
                                                                             new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(runnable);
        singleThreadPool.shutdown();
    }

}
