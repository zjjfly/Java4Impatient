package chp03;

import common.Person;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static chp03.LambdaArgument.ColorTransformer;
public class Exercise {
    private Image image;
    @Before
    public void init(){
        JFXPanel jfxPanel = new JFXPanel();
        this.image= new Image("https://img3.doubanio.com/dae/niffler/niffler/images/c4972ec0-e3bf-11e7-9d88-0242ac110021.jpg");
    }

    @Test
    public void exercise1() throws Exception {
        int i = 1;
        logif(Logger.getLogger(""), Level.INFO, () -> i == 10, () -> "hello");
    }

    private void logif(Logger logger, Level level, Supplier<Boolean> condition, Supplier<String> msg) {
        if (logger.isLoggable(level)) {
            if (condition.get()) {
                logger.log(level, msg);
            }
        }
    }

    @Test
    public void exercise2() throws Exception {
        ReentrantLock reentrantLock = new ReentrantLock();
        withLock(reentrantLock, () -> System.out.println("hello"));
    }

    private void withLock(ReentrantLock lock, Runnable runnable) {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }

    @Test
    public void exercise3() throws Exception {
        myassert(true);
        myassert(() -> Calendar.getInstance().get(Calendar.YEAR) == 2017);
    }

    private void myassert(Supplier<Boolean> supplier) {
        if (!supplier.get()) {
            throw new AssertionError();
        }
    }

    private void myassert(boolean flag) {
        if (!flag) {
            throw new AssertionError();
        }
    }

    @Test
    public void exercise5() throws Exception {
        Image out = transform(image, (x, y, c) -> {
            double width = image.getWidth();
            double height = image.getHeight();
            if (x > 9 && y > 9 && x <= width - 10 && y <= height - 10) {
                return c;
            } else {
                return Color.GRAY;
            }
        });
        HBox hBox = new HBox();
    }

    private static Image transform(Image in, ColorTransformer transformer) {
        int width = (int) in.getWidth();
        int height = (int) in.getHeight();
        WritableImage out = new WritableImage(width, height);
        PixelWriter pixelWriter = out.getPixelWriter();
        PixelReader pixelReader = in.getPixelReader();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixelWriter.setColor(x, y, transformer.apply(x, y, pixelReader.getColor(x, y)));
            }
        }
        return out;
    }

    @Test
    public void exercise7() throws Exception {
        Comparator<String> stringComparator = stringComparator(false, true, true);
        ArrayList<String> strings = new ArrayList<String>() {{
            add("sada");
            add(" Wwdwd");
            add("dwcw");
        }};
        strings.sort(stringComparator);
        System.out.println(strings);
    }

    private static Comparator<String> stringComparator(boolean reverse, boolean caseSensitive, boolean spaceSensitive) {
        return (s1, s2) -> {
            if (caseSensitive) {
                s1 = s1.toLowerCase();
                s2 = s2.toLowerCase();
            }
            if (spaceSensitive) {
                s1 = s1.replaceAll("\\s+", "");
                s2 = s2.replaceAll("\\s+", "");
            }
            if (reverse) {
                return -1 * s1.compareTo(s2);
            } else {
                return s1.compareTo(s2);
            }
        };
    }

    @Test
    public void exercise8() throws Exception {
        transform(image, colorTransformer(image.getWidth(), image.getHeight(), 10, Color.RED));
    }

    private static ColorTransformer colorTransformer(double width, double height, double thickness, Color color) {
        return (x, y, c) -> {
            if (x >= thickness && y >= thickness && x <= width - thickness && y <= height - thickness) {
                return c;
            } else {
                return color;
            }
        };
    }

    @Test
    public void exercise9() throws Exception {
        Person jjzi = new Person(1, "jjzi");
        Person jj = new Person(2, "jj");
        Comparator<Person> objectComparator = lexicographicComparator("id", "name");
        assert objectComparator.compare(jjzi, jj)<0;
    }

    private <T> Comparator<T> lexicographicComparator(String... fields) {
        return (t1, t2) -> {
            Class<?> clazz = t1.getClass();
            int re = 0;
            for (String field : fields) {
                try {
                    Field declaredField = clazz.getDeclaredField(field);
                    declaredField.setAccessible(true);
                    if (declaredField.getType().isPrimitive() || declaredField.getClass()
                                                                              .isAssignableFrom(Comparable.class)) {
                        Comparable o1 = (Comparable) declaredField.get(t1);
                        Comparable o2 = (Comparable) declaredField.get(t2);
                        re = o1.compareTo(o2);
                        if (re != 0) {
                            break;
                        }
                    } else {
                        System.out.println("the field \"" + field + "\" is not comparable");
                    }
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            return re;
        };
    }

    static Image transform(Image in, Function<Color, Color> f) {
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

    @Test
    public void exercise10() throws Exception {
        UnaryOperator<Color> op = Color::brighter;
        transform(image, op.compose(Color::grayscale));
    }

    private static ColorTransformer compose(ColorTransformer ctf1, ColorTransformer ctf2) {
        return (x, y, c) -> ctf2.apply(x, y, ctf1.apply(x, y, c));
    }

    private static ColorTransformer convert(UnaryOperator<Color> operator){
        return (int x, int y, Color c) -> operator.apply(c);
    }

    @Test
    public void exercise11() throws Exception {
        ColorTransformer grapFrame = colorTransformer(image.getWidth(), image.getHeight(), 10, Color.GRAY);
        ColorTransformer compose = compose(grapFrame, convert(Color::brighter));
        transform(image, compose);
    }
}
