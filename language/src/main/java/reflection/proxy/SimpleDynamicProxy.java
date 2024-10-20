package reflection.proxy;

import java.lang.reflect.Proxy;

/**
 * Java 动态代理 Demo
 */
public class SimpleDynamicProxy {

    private static void consumer(Interface iface) {
        iface.doSomething();
    }

    public static void main(String[] args) {
        RealObject real = new RealObject();
        consumer(real);

        Interface o = (Interface) Proxy.newProxyInstance(Interface.class.getClassLoader(),
                new Class[]{Interface.class},
                new DynamicProxyHandler(real));
        System.out.println(o.getClass().getName());
        consumer(o);
    }
}


