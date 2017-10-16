package chp03;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.function.UnaryOperator;

/**
 * @author zjjfly
 */
public class Function {
    /**
     * @param in 原图片
     * @param f  使颜色变亮的函数
     * @return 新的图片
     */
    static Image transform(Image in, UnaryOperator<Color> f) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.getPixelWriter().setColor(x, y, f.apply(in.getPixelReader().getColor(x, y)));
            }
        }
        return out;
    }

    /**
     * 返回函数的方法
     * @param factor 变亮的因子
     * @return 转换Color的函数
     */
    static UnaryOperator<Color> brighten(double factor) {
        return c -> c.deriveColor(0, 1, factor, 1);
    }

    /**
     * 组合函数
     * @param op1 函数1
     * @param op2 函数2
     * @param <T> 函数的参数和返回值类型
     * @return 组合之后的函数
     */
    static <T> UnaryOperator<T> compose(UnaryOperator<T> op1,UnaryOperator<T> op2){
        return t->op2.apply(op1.apply(t));
    }
}
