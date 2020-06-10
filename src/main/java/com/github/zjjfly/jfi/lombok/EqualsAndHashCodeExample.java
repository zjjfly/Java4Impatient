package com.github.zjjfly.jfi.lombok;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author zjjfly
 */
@EqualsAndHashCode
@Setter
public class EqualsAndHashCodeExample {
    private transient int transientVar = 10;
    private String name;
    private double score;
    @EqualsAndHashCode.Exclude
    private Shape shape = new Square(5, 10);
    private String[] tags;
    @EqualsAndHashCode.Exclude
    private int id;

    public String getName() {
        return this.name;
    }

    @EqualsAndHashCode(callSuper = true)
    public static class Square extends Shape {
        private final int width, height;

        public Square(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static void main(String[] args) {
        EqualsAndHashCodeExample example1 = new EqualsAndHashCodeExample();
        example1.setId(1);
        EqualsAndHashCodeExample example2 = new EqualsAndHashCodeExample();
        example2.setId(2);
        assertEquals(example1,example2);
        example1.setTransientVar(9);
        assertEquals(example1,example2);
        example1.setName("jjzi");
        example2.setName("zjj");
        assertNotEquals(example1,example2);

        //callSuper的作用
        Square square1 = new Square(1, 1);
        square1.name="jjzi";
        Square square2 = new Square(1, 1);
        square2.name="zjj";
        assertNotEquals(square1,square2);
    }
}
