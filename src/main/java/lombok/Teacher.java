package lombok;

import lombok.experimental.Accessors;

/**
 * lombok实现链式调用风格和静态构造方法
 * @author zjjfly
 */
@Accessors(fluent = true)
@Getter
@Setter
@RequiredArgsConstructor(staticName = "ofName")
public class Teacher {
    private int age;
    @NonNull
    private String name;

    public static void main(String[] args) {
        Teacher zjj = new Teacher("zjj").age(32).name("jzz");
        Teacher zjjfly = Teacher.ofName("zjjfly");
    }
}
