import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
【代理】
代理是基本的设计模式之一。
一个对象封装真实对象，
代替其提供其他或不同的操作---这些操作通常涉及到与“真实”对象的通信，因此代理通常充当中间对象。
 */

/**
 * 这是一个简单的示例，显示代理的结构：
 */
interface Interface {
    void doSomething();
    void somethingElse(String arg);
}
class RealObject implements Interface {

    @Override
    public void doSomething() {
        System.out.println("RealObject doSomething");
    }

    @Override
    public void somethingElse(String arg) {
        System.out.println("RealObject somethingElse " + arg);
    }
}
class SimpleProxy implements Interface {
    private Interface proxied;

    SimpleProxy(Interface proxied) {
        this.proxied = proxied;
    }

    @Override
    public void doSomething() {
        System.out.println("SimpleProxy doSomething");
        proxied.doSomething();
    }

    @Override
    public void somethingElse(String arg) {
        System.out.println("SimpleProxy somethingElse " + arg);
        proxied.somethingElse(arg);
    }
}
class SimpleProxyDemo {
    public static void consumer(Interface iface) {
        iface.doSomething();
        iface.somethingElse("bonobo");
    }

    public static void main(String[] args) {
        // 没有使用代理
        consumer(new RealObject());
        // 使用 SimpleProxy 代理
        consumer(new SimpleProxy(new RealObject()));
    }
}




/*
Java 的动态代理更进一步，
不仅动态创建代理对象而且动态处理对代理方法的调用。

在动态代理上进行的所有调用都被重定向到单个调用处理程序，
该处理程序负责发现调用的内容并决定如何处理。
 */

/**
 * SimpleProxy 使用动态代理重写的例子：
 */
class DynamicProxyHandler implements InvocationHandler {
    private Object proxied;

    DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(
                "**** proxy: " + proxy.getClass() +
                        ", method: " + method + ", args: " + args);
        if (args != null) {
            for (Object arg : args) {
                System.out.println(" " + arg);
            }
        }
        return method.invoke(proxied, args);
    }
}

class SimpleDynamicProxy {
    public static void consumer(Interface iface) {
        iface.doSomething();
        iface.somethingElse("bonobo");
    }

    public static void main(String[] args) {
        RealObject real = new RealObject();

        DynamicProxyHandler handler = new DynamicProxyHandler(real);
        try {
            handler.invoke(real, Interface.class.getMethods()[0], new Object[]{""});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 插入代理并再次调用:
        Interface proxy = (Interface) Proxy.newProxyInstance(
                Interface.class.getClassLoader(),
                new Class[]{Interface.class},
                new DynamicProxyHandler(real));
        consumer(proxy);
    }
}


/**
 * 在 DynamicProxyHandler 中，可过选择性地过滤掉一些方法的调用。
 * 改造如下：
 */
class MethodSelector implements InvocationHandler {
    private Object proxied;

    MethodSelector(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 通过方法名称，过滤掉 interesting 方法
        if (method.getName().equals("interesting")) {
            System.out.println("Proxy detected the interesting method");
        }
        return method.invoke(proxied, args);
    }
}

interface SomeMethods {
    void boring1();
    void boring2();
    void interesting(String arg);
    void boring3();
}
class Implementation implements SomeMethods {

    @Override
    public void boring1() {
        System.out.println("boring1");
    }

    @Override
    public void boring2() {
        System.out.println("boring2");
    }

    @Override
    public void interesting(String arg) {
        System.out.println("interesting " + arg);
    }

    @Override
    public void boring3() {
        System.out.println("boring3");
    }
}
class SelectingMethods {
    public static void main(String[] args) {
        // 使用代理创建对象并通过代理调用方法
        SomeMethods proxy = (SomeMethods) Proxy.newProxyInstance(
                SomeMethods.class.getClassLoader(),
                new Class[] { SomeMethods.class},
                new MethodSelector(new Implementation()));
        proxy.boring1();
        proxy.boring2();
        proxy.interesting("bonobo");
        proxy.boring3();
    }
}
