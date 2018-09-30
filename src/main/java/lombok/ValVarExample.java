package lombok;

import lombok.experimental.var;

import java.util.ArrayList;

/**
 * @author zjjfly
 */
public class ValVarExample {
    public static void main(String[] args) {
        val example = new ArrayList<String>();
        example.add("Hello,World");
        for (val s : example) {
            System.out.println(s);
        }
        val foo = example.get(0);
        foo.toLowerCase();
        //var和val用法一样,但不会把变量标记为final
        var s="jjzi";
        s="zjj";
    }
}
