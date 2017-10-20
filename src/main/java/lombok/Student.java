package lombok;

/**
 * 使用lombok快速的实现Builder模式
 * @author zjjfly
 */
@Builder
public class Student {
    private int id;
    private String name;

    public static void main(String[] args) {
        Student jjzi = Student.builder().id(1).name("jjzi").build();
        System.out.println(jjzi.id);
    }
}
