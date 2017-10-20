package lombok;

/**
 * lombok自动添加getter,setter和空值检查
 *
 * @author zjjfly
 */
@Setter
@Getter
@AllArgsConstructor
public class Cat {
    //任何被@NonNull修饰的成员,凡是lombok自动生成对它赋值的方法都会进行空值检查
    @NonNull
    private String name;
    @NonNull
    private Integer age;

    public static void main(String[] args) {
        Cat kitty = new Cat("Kitty", 2);
    }
}
