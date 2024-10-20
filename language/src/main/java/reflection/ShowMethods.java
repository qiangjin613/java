package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 一个工具类，用于提取指定类的所有方法（含基类方法）
 */
public class ShowMethods {

    public static void main(String[] args) {
//        args = new String[] {"java.util.ArrayList"};
        try {
            Class<?> cls = Class.forName(args[0]);
            Method[] methods = cls.getMethods();
            Constructor<?>[] constructors = cls.getDeclaredConstructors();
            for (Method method : methods) {
                System.out.println(method.toString());
            }
            for (Constructor<?> constructor : constructors) {
                System.out.println(constructor.toString());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
