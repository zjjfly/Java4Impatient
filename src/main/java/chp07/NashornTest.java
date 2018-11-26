package chp07;

import common.Person;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * @author zjjfly
 */
public class NashornTest {
    @Test
    public void scriptEngine() throws ScriptException, IOException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine nashorn = manager.getEngineByName("nashorn");
        //直接执行脚本
        int result = (int) nashorn.eval("'Hello,World!'.length");
        assertEquals(12, result);

        //从文件中读取脚本
        assertEquals(12, nashorn.eval(Files.newBufferedReader(Paths.get("test.js"))));

        //可以传入对象到脚本的作用域
        Bindings bindings = nashorn.createBindings();
        bindings.put("person", new Person(1,"jjzi"));
        //脚本中可以使用js的语法访问对象的字段和修改字段
        assertEquals("hello,zjj", nashorn.eval("person['name']='zjj'\n'hello,'+person.name", bindings));
    }
}