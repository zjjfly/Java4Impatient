package chp03;

import javafx.scene.paint.Color;

import java.util.function.IntConsumer;

public class LambdaArgument {
    public static void main(String[] args) {
        repeat(10, i -> System.out.println("Countdown: " + (9 - i)));
        //lambda的参数最好可以包含它可能需要的信息
//        Button button = new Button("a");
//        button.setOnAction(event -> System.out.println(event.getEventType()));
        repeat(10, () -> System.out.println("Hello World!"));
    }

    /**
     * @param n      迭代次数
     * @param action 使用IntConsumer作为参数，这样可以知道是在哪一次迭代
     */
    private static void repeat(int n, IntConsumer action) {
        for (int i = 0; i < n; i++) {
            action.accept(i);
        }
    }

    //如果某些信息很少用到，也可以提供一个重载方法，不强制用户传递这些信息
    private static void repeat(int n, Runnable action) {
        for (int i = 0; i < n; i++) {
            action.run();
        }
    }

    //自定义函数式接口
    @FunctionalInterface
    public interface ColorTransformer {
        Color apply(int x, int y, Color colorAtXY);
    }
}
