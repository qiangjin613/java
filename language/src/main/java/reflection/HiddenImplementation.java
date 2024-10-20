package reflection;

import reflection.xxx.A;
import reflection.xxx.HiddenC;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HiddenImplementation {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        A a = HiddenC.makeA();
        a.f();
        System.out.println(a.getClass().getName());

        // 'reflection.xxx.C' is not public in 'reflection.xxx'. Cannot be accessed from outside package
//        if (a instanceof reflection.xxx.C) {
//        }

        // 使用反射依旧可以使用不存在的 C 中的方法
        callHiddenMethod(a, "v");
        callHiddenMethod(a, "w");
        callHiddenMethod(a, "u");
    }

    static void callHiddenMethod(Object o, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> cls = o.getClass();
        Method method = cls.getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(o);
    }
}
