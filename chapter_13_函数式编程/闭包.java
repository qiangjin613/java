/*
Java 8 提供了有限但合理的闭包支持。

之前，从方法中返回 Lambda 函数。
Q：考虑一个更复杂的 Lambda，它使用函数作用域之外的变量。 返回该函数会发生什么？
    也就是说，当你调用函数时，它对那些 “外部 ”变量引用了什么?
    如果语言不能自动解决，那问题将变得非常棘手。能够解决这个问题的语言被称作支持闭包
    （或词法定界，或变量捕获）。
 */

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * 简单的例子：
 */
class Closure1 {
    int i;
    IntSupplier makeFun(int x) {
        return () -> x + i++;
    }
}

/*
对于上述的例子，i 的这种用法并非是个大难题，
因为对象很可能在你调用 makeFun() 之后就存在了。

当然，如果你对同一个对象多次调用 makeFun()，
最终会得到多个函数，它们共享 i 的存储空间。

看下面的例子：
 */
class SharedStorage {
    public static void main(String[] args) {
        Closure1 c1 = new Closure1();
        /* 多次调用 makeFun() 得到多个函数*/
        IntSupplier f1 = c1.makeFun(0);
        IntSupplier f2 = c1.makeFun(0);
        IntSupplier f3 = c1.makeFun(0);
        IntSupplier f4 = c1.makeFun(0);

        System.out.println("调用 getAsInt() 之前的 i = " + c1.i);

        /* 每次调用 getAsInt() 都会使 i 的值 +1 */
        System.out.println(f1.getAsInt());
        System.out.println(f2.getAsInt());
        System.out.println(f3.getAsInt());
        System.out.println(f4.getAsInt());
    }
}

/*
如果 i 是 makeFun() 的局部变量时，怎么办？
 */
class Closure2 {
    IntSupplier makeFun(int x) {
        int i = 0;
        return () -> x + i;
    }
}
class SharedStorage2 {
    public static void main(String[] args) {
        Closure2 c2 = new Closure2();
        /* 多次调用 makeFun() 得到多个函数*/
        IntSupplier f1 = c2.makeFun(0);
        IntSupplier f2 = c2.makeFun(0);
        IntSupplier f3 = c2.makeFun(0);
        IntSupplier f4 = c2.makeFun(0);

        /* 每次调用 getAsInt() 其值都不变 */
        System.out.println(f1.getAsInt());
        System.out.println(f2.getAsInt());
        System.out.println(f3.getAsInt());
        System.out.println(f4.getAsInt());
    }
}
/*
在正常情况下，当 makeFun() 完成时 i 就消失。

由于 makeFun() 返回的 IntSupplier “关住了” i 和 x，
因此即使makeFun()已执行完毕，当你调用返回的函数时i 和 x仍然有效，
而不是像正常情况下那样在 makeFun() 执行后 i 和x就消失了。

注意一点：
被 Lambda 表达式引用的局部变量必须是 final 或者是等同 final 效果的。
（上述例子中，x++ 或者 i++ 或者在返回的函数作用域之外改变 x 和 i 都会报错，最好使用 final 进行修饰）

IntSupplier makeFun(int x) {
    int i = 0;
    // i、x 都会报错
    i++;
    x++;
    return () -> x + i;
}
 */

/*
Q：如果非要对 i、x 进行修改，改怎么处理呢？
A：提供一种办法；
 */
class Closure6 {
    IntSupplier makeFun(int x) {
        int i = 0;
        // i、x 都会报错
        i++;
        x++;
        final int iFinal = i;
        final int xFinal = x;
        return () -> xFinal + iFinal;
    }
}

/*
在上述例子中，iFinal、xFinal 并没有被改变过（等效final），
所以说，final 是多余的
 */

/*
Q：如果改用包装类型会是什么情况呢？

class Closure7 {
    IntSupplier makeFun(int x) {
        Integer i = 0;
        i = i + 1;
        return () -> x + i;
    }
}

编译器非常聪明地识别到变量 i 的值被更改过。
包装类型可能是被特殊处理了，再尝试下 List：
 */
class Closure8 {
    Supplier<List<Integer>> makeFun() {
        final List<Integer> ai = new ArrayList<>();
        ai.add(1);
        ai.add(2);
        return () -> ai;
    }

    public static void main(String[] args) {
        Closure8 c7 = new Closure8();
        List<Integer>
                l1 = c7.makeFun().get(),
                l2 = c7.makeFun().get();
        System.out.println(l1);
        System.out.println(l2);
        l1.add(42);
        l2.add(9);
        System.out.println(l1);
        System.out.println(l2);
    }
}
/*
可以看到，改变了 List 的内容却没产生编译时错误。

通过观察本例的输出结果，我们发现这看起来非常安全。
这是因为每次调用 makeFun() 时，
其实都会创建并返回一个全新而非共享的 ArrayList。
也就是说，每个闭包都有自己独立的 ArrayList，它们之间互不干扰。

注意：已经声明 ai 是 final 的了。
应用于对象引用的 final 关键字仅表示不会重新赋值引用。
它并不代表你不能修改对象本身。

这时，新的问题又来了：
只要没有其他人获得对该对象的引用，基本上就是安全的。
但是，这也意味着有多个实体可以修改对象，此时事情会变得非常混乱。
（当理解了并发编程章节的内容，就能明白为什么更改共享变量 “不是线程安全的” 的了。）
 */

/*
【小结】
Q：回顾一下 Closure1，为什么变量 i 被修改编译器却没有报错呢。
    它既不是 final 的，也不是等同 final 效果的。
A：因为 i 是外部类的成员，所以这样做肯定是安全的。
    （除非正在创建共享可变内存的多个函数）

必须考虑捕获的变量是否是等同 final 效果的。
如果它是对象中的字段（实例变量），那么它有独立的生命周期，
不需要任何特殊的捕获以便稍后在调用 Lambda 时存在。

注：结论是，Lambda 可以没有限制地引用实例变量和静态变量。
但局部变量必须显式声明为 final，或等同 final 效果。
 */

/*
【作为闭包的内部类】
这里算是对用匿名内部类实现闭包的一个回顾
 */
class AnonymousClosure {
    IntSupplier makeFun(int x) {
        int i = 0;
        return new IntSupplier() {
            @Override
            public int getAsInt() {
                return x + i;
            }
        };
    }
}
