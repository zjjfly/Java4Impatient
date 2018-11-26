package lombok;


/**
 * @author zjjfly
 */
@ToString
public class ToStringExample {
    private static final int STATIC_VAR = 10;
    private String name;
    private Shape shape = new Square(5, 10);
    @ToString.Exclude
    private int id;

    public String getName() {
        return this.name;
    }

    @ToString(callSuper = true, includeFieldNames = true)
    public static class Square extends Shape {
        private final int width, height;

        public Square(int width, int height) {
            this.name="square";
            this.width = width;
            this.height = height;
        }
    }

    public static void main(String[] args) {
        System.out.println(new ToStringExample());
    }
}
