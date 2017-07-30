package chp01;

import java.util.ArrayList;
import java.util.List;

public class VariableScope {
    public static void main(String[] args) {
        VariableScope variableScope = new VariableScope();
        variableScope.repeatMessage("Hello",10);
        //虽然不能给变量重新赋值，但是可以修改变量，这样做也是线程不安全的
        List<Integer> nums = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> nums.add(1)).start();
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
        int counter[]={0};
        Runnable runnable=() -> {
            for (int i = 0; i < count; i++) {
                System.out.println(msg);
                counter[0]++;
                Thread.yield();
            }
        };
        new Thread(runnable).start();
    }

}
