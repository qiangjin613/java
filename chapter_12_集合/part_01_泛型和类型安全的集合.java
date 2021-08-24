/*
如果一个程序只包含固定数量的对象且对象的生命周期都是已知的，（ -> 数组）
那么这是一个非常简单的程序。（通常，程序不是这么简单的...）

通常，程序总是根据运行时才知道的某些条件去创建新的对象。
在此之前，无法知道所需对象的数量甚至确切类型。

为了解决这个普遍的编程问题，
需要在任意时刻和任意位置创建任意数量的对象。

因此，不能依靠创建命名的引用来持有每一个对象，
因为从来不会知道实际需要多少个这样的引用。

使用固定长度、确定类型的数组已经无法满足这个需求了，
java.util 库提供了一套相当完整的集合类（collection classes）来解决这个问题，
这些类型也被称作容器类（container classes），
集合提供了完善的方法来保存对象，可以使用这些工具来解决大量的问题。

集合还有一些其它特性，比如：
    Set 对于每个值都只保存一个对象， Map 是一个关联数组，
    Java集合类都可以自动地调整自己的大小，
    ...
 */

import java.util.ArrayList;

/**
 * 使用 Java 5 之前的集合的一个主要问题是编译器允许你向集合中插入不正确的类型。
 * 举一个例子：
 */
class Apple {
    private static long counter;
    private final long id = counter++;
    public long id() {
        return id;
    }
}
class Orange {}
class ApplesAndOrangesWithoutGenerics {
    public static void main(String[] args) {
        ArrayList apples = new ArrayList();
        for (int i = 0; i < 3; i++) {
            apples.add(new Apple());
        }
        // 这里把不相干的 orange 加入到 apple 集合中是没有问题的：
        apples.add(new Orange());
        for (Object apple : apples) {
            /* 在使用存放在 apple 集合里的 orange 时就会引发异常 */
            System.out.println(((Apple) apple).id());
        }
    }
}
/*
Q：为什么可以保存不相干的类？
A：因为 ArrayList 中保存的是 Object[]，所以在向上转型的帮助下，能存放“不相干”的类。
 */


/*
通过使用泛型（since 1.5），就可以在编译期防止将错误类型的对象放置到集合中。
（成为一个编译期错误而不是运行时错误）
如下：
 */
class ApplesAndOrangesWithGenerics {
    public static void main(String[] args) {
        ArrayList<Apple> apples = new ArrayList<Apple>();
        for (int i = 0; i < 3; i++) {
            apples.add(new Apple());
        }
        /*
        在放入“不相干”的类的时候，在编译期就阻止将错误类型的对象放置到集合中，
        在使用时，也就不会出现上述异常（ClassCastException）
         */
        //apples.add(new Orange());
        for (Object apple : apples) {
            /* 在使用存放在 apple 集合里的 orange 时就会引发异常 */
            System.out.println(((Apple) apple).id());
        }
    }
}


/*
在 Java 7 之前，使用泛型集合时必须要在两端都进行类型声明（如上），

随着类型变得越来越复杂，这种重复产生的代码非常混乱且难以阅读。
程序员发现所有类型信息都可以从左侧获得，
因此，编译器没有理由强迫右侧再重复这些。

虽然类型推断（type inference）只是个很小的请求，
Java 语言团队仍然欣然接受并进行了改进。
 */


/*
如果想要将 Apple 和 Orange 放到一起，向上转型也使用于泛型：
 */
class Fruit2 {
    private static long counter;
    private final long id = counter++;
    public long id() {
        return id;
    }
}
class Apple2 extends Fruit2 {}
class Orange2 extends Fruit2 {}

class GenericsAndUpcasting {
    public static void main(String[] args) {
        ArrayList<Fruit2> fruits = new ArrayList<>();
        fruits.add(new Apple2());
        fruits.add(new Orange2());
        for (Fruit2 fruit : fruits) {
            /* 这并不会有什么问题 */
            System.out.println(fruit.id());
        }
    }
}
