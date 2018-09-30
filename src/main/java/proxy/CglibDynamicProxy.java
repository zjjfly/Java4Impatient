package proxy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib实现的动态代理
 * @author zjjfly
 */
public class CglibDynamicProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("before:"+method.getName());
        Object re = proxy.invokeSuper(obj, args);
        System.out.println("after:"+method.getName());
        return re;
    }

    public static void main(String[] args) {
        //设置编译出的class文件的存放路径
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/zjjfly/Desktop");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Hello.class);
        enhancer.setCallback(new CglibDynamicProxy());
        Hello hello = (Hello) enhancer.create();
        hello.hello();
    }
}
