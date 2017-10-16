package chp01;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.*;

/**
 * @author zjjfly
 */
public class Lambda {
    static class Worker implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName()+":"+i);
            }
        }
    }

    static class LengthComparator implements Comparator<String>, Serializable {
        @Override
        public int compare(String o1, String o2) {
            return Integer.compare(o1.length(), o2.length());
        }
    }

    public static void main(String[] args) {
        //jdk8之前
        Worker worker = new Worker();
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("my-thread-%d").build();
        ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                                                                               new LinkedBlockingDeque<>(1024), threadFactory,
                                                                                 new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(worker);
        //jdk8
        singleThreadPool.execute(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        });
        singleThreadPool.shutdown();
        String[] strings = {"a", "bcd", "ef"};
        //jdk8之前
        Arrays.sort(strings, new LengthComparator());
        //jdk8
        Arrays.sort(strings, (String o1, String o2) -> Integer.compare(o1.length(), o2.length()));
        //lambda有多行的话可以使用花括号
        Arrays.sort(strings, (String o1, String o2) -> {
            if (o1.length() > o2.length()) {
                return 1;
            } else if (o1.length() < o2.length()) {
                return -1;
            } else {
                return 0;
            }
        });
        //如果可以推导出参数的类型，不需要显示的声明类型
        Comparator<String> comp = (first, second) -> Integer.compare(first.length(), second.length());
        //如果只有一个参数，并且类型是可以被推导的，那么可以不加括号
        EventHandler<ActionEvent> listener = event -> System.out.println("Click");
    }
}
