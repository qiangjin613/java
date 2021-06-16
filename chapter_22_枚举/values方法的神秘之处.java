/*
之前说到，编译器创建的 enum 类都继承自 Enum 类。
然而，Enum 类它并没有 values() 方法。
aHa? 可是，明明已经用过该方法了，难道存在某种“隐藏的”方法吗？
 */

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/*
使用反射机制一探究竟：
 */
enum Explore {
    HERE, THERE
}
class Reflection {

    /**
     * 返回该枚举类的所有方法
     */
    public static Set<String> analyze(Class<?> enumClass) {
        System.out.println("_____ Analyzing " + enumClass + " _____");

        /* 该枚举类的接口 */
        System.out.println("Interfaces:");
        for (Type t : enumClass.getGenericInterfaces()) {
            System.out.println(t);
        }

        /* 该枚举类的基类 */
        System.out.println("Base: " + enumClass.getSuperclass());

        /* 该枚举类的方法 */
        System.out.println("Methods: ");
        Set<String> methods = new TreeSet<>();
        for (Method m : enumClass.getMethods()) {
            methods.add(m.getName());
        }
        System.out.println(methods);

        return methods;
    }

    public static void main(String[] args) {
        Set<String> exploreMethods = analyze(Explore.class);
        Set<String> enumMethods = analyze(Enum.class);

        System.out.println("Explore.containsAll(Enum)? " +
                exploreMethods.containsAll(enumMethods));

        System.out.println("Explore.removeAll(Enum): ");
        exploreMethods.removeAll(enumMethods);
        System.out.println(exploreMethods);
    }
}
/*
可以看到，Enum 类确实没有 values() 方法。

---------
反编译枚举的代码:
Compiled from "Reflection.java"
final class Explore extends java.lang.Enum<Explore> {
  public static final Explore HERE;
  public static final Explore THERE;
  public static Explore[] values();
  public static Explore valueOf(java.lang.String);
  static {};
}
（观察 Explore.class 反编译后的文件，得知...）
事实上，values() 是由编译器添加的 static 方法。
在创建 Explore 的过程中，编译器还为其添加了 valueOf() 方法。

这可能有点令人迷惑，Enum 类不是已经有 valueOf() 方法了吗？

不过 Enum 中的 valueOf() 方法需要两个参数，而这个新增的方法只需一个参数。

从最后的输出中可以看到，编译器将 Explore 标记为 final 类，
所以无法继承自 enum，其中还有一个 static 的初始化子句。

由于泛型的擦除效应，反编译无法得到 Enum 的完整信息，
所以它展示的 Explore 的父类只是一个原始的 Enum，
而非事实上的 Enum<Explore>。

由于 values() 方法是由编译器插入到 enum 定义中的 static 方法，
所以，如果将 enum 实例向上转型为 Enum，那么 values() 方法就不可访问了。
不过，在 Class 中有一个 getEnumConstants() 方法，
所以即便 Enum 接口中没有 values() 方法，
仍然可以通过 Class 对象取得所有 enum 实例。
 */

/**
 * 通过 Class 对象获取所有 enum 实例：
 */
enum Search {
    HITHER, YON
}
class UpcastEnum {
    public static void main(String[] args) {
        Search[] values = Search.values();
        System.out.println(Arrays.toString(values));

        /* 向上转型 */
        Enum e = Search.HITHER;
        for (Enum en : e.getClass().getEnumConstants()) {
            System.out.println(en);
        }
    }
}

/*
因为 getEnumConstants() 是 Class 的方法，
所以可以对不是枚举的类调用此方法：
 */
class NonEnum {
    public static void main(String[] args) {
        Class<Integer> intClass = Integer.class;
        Integer[] enumConstants = intClass.getEnumConstants();
        /* 下面的语句抛出异常 */
        for (Integer en : enumConstants) {
            System.out.println(en);
        }
    }
}
/*
只不过，此时该方法返回 null，
所以当试图使用其返回的结果时会发生异常。
 */
