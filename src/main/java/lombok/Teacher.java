package lombok;

import lombok.experimental.Accessors;

/**
 * lombok实现链式调用风格和静态构造方法
 * @author zjjfly
 */
@Accessors(chain = true)
@Getter
@Setter
@RequiredArgsConstructor(staticName = "ofName")
public class Teacher {
    private int age;
    @NonNull
    private String name;

    public static void main(String[] args) {
        Teacher zjj = new Teacher("zjj").setAge(32).setName("jzz");
        Teacher zjjfly = Teacher.ofName("zjjfly");
    }
}
