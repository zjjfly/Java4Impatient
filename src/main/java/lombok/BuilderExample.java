package lombok;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author zjjfly
 */
@ToString
@Builder
public class BuilderExample {
    @Builder.Default
    private long created = System.currentTimeMillis();
    private String name;
    private int age;
    @Singular
    private Set<String> occupations;

    public static void main(String[] args) {
        BuilderExampleBuilder builder = BuilderExample.builder();
        BuilderExample build = builder.age(11).name("jjzi").occupations(new ArrayList<String>() {{
            add("jjzi");
        }}).build();
        System.out.println(build);
    }
}