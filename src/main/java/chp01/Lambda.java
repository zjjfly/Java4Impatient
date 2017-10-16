package chp01;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(
                1,
                new BasicThreadFactory.Builder()
                        .namingPattern(
                                "my-thread-%d")
                        .daemon(true)
                        .build());
        poolExecutor.execute(worker);
        //jdk8
        poolExecutor.execute(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        });
        poolExecutor.shutdown();
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
