package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk动态代理,局限是被代理的类必须实现某个接口
 * @author zjjfly
 */
public class JdkDynamicProxy implements InvocationHandler {
    private Object subject;

    public JdkDynamicProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before invoke...");
        method.invoke(subject,args);
        System.out.println("After invoke...");
        return null;
    }

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Hello hello = new Hello();
        InvocationHandler handler = new JdkDynamicProxy(hello);
        HelloInterface instance = (HelloInterface) Proxy
                .newProxyInstance(handler.getClass().getClassLoader(), hello.getClass().getInterfaces(), handler);
        instance.hello();
    }
}
