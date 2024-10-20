package reflection.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DynamicProxyHandler implements InvocationHandler {

    private Object proxied;

    public DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("DynamicProxyHandler.invoke() 执行，proxy：" + proxy.getClass().getName()
                + ", method: " + method + ", args: " + Arrays.toString(args));
        return method.invoke(proxied, args);
    }
}
