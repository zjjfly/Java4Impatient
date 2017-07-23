package chp01;

import java.util.Arrays;
import java.util.Comparator;

public class Lambda {
    static class Worker implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        }
    }

    static class LengthComparator implements Comparator<String>{

        @Override
        public int compare(String o1, String o2) {
            return Integer.compare(o1.length(),o2.length());
        }
    }

    public static void main(String[] args) {
        Worker worker = new Worker();
        new Thread(worker).start();
        String[] strings = {"a", "bcd", "ef"};
        Arrays.sort(strings, new LengthComparator());
        Arrays.sort(strings, Comparator.comparingInt(String::length));
        System.out.println(Arrays.toString(strings));
    }
}
