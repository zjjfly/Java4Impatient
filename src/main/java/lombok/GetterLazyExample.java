package lombok;


/**
 * @Getter(lazy=true)类似Scala的lazy关键字
 * @author zjjfly
 */
public class GetterLazyExample {
    @Getter(lazy=true) private final double[] cached = expensive();

    private double[] expensive() {
        System.out.println("init cache");
        double[] result = new double[1000000];
        for (int i = 0; i < result.length; i++) {
            result[i] = Math.asin(i);
        }
        return result;
    }

    public static void main(String[] args) {
        GetterLazyExample getterLazyExample = new GetterLazyExample();
        System.out.println("to get cached");
        getterLazyExample.getCached();
    }
}