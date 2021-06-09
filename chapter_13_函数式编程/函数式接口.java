/*
菜鸟教程的一个例子：
 */

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

class AA {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        System.out.println("----- 1 -----");
        eval(list, n -> true);

        System.out.println("----- 2 -----");
        eval(list, n -> n % 2 == 0);

        System.out.println("----- 3 -----");
        eval(list, n -> n > 2);
    }

    public static void eval(List<Integer> list, Predicate<Integer> p) {
        for (Integer i : list) {
            if (p.test(i)) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }
}


/*
方法引用和 Lambda 表达式都必须被赋值，同时赋值需要类型信息才能使编译器保证类型的正确性。

Q：假设需要传递方法引用|Lambda，怎么知道传递给方法的参数的类型？
A：为了解决这个问题，Java 8 引入了 java.util.function 包。
    它包含一组接口，这些接口是 Lambda 表达式和方法引用的目标类型。
    每个接口只包含一个抽象方法，称为函数式方法。
 */

/*
在编写接口时，可以使用 @FunctionalInterface 注解强制执行此“函数式方法”模式：
（@FunctionalInterface 与 @Override 效果相同）
@FunctionalInterface 要求被标记接口拥有一个目标方法（target method）
 */
@FunctionalInterface
interface Functional {
    String goodbye(String arg);
}

interface FunctionalNoAnn {
    String goodbye(String arg);
}

class FunctionalAnnotation {
    public String goodbye(String arg) {
        return "Goodbye " + arg;
    }

    public static void main(String[] args) {
        FunctionalAnnotation fa = new FunctionalAnnotation();
        /* 将不相关的类的方法进行绑定 */
        Functional f = fa::goodbye;
        FunctionalNoAnn fna = fa::goodbye;

        Functional fl = arg -> "Goodbye " + arg;
        FunctionalNoAnn fnal = arg -> "Goodbye " + arg;

        /*
        Functional 和 FunctionalNoAnn 声明了是接口，然而被赋值的只是方法 goodbye()。

        观察：
        首先，这只是一个方法而不是类；
        其次，它甚至都不是实现了该接口的类中的方法。（压根儿没用到继承）

        这是添加到 Java 8 中的一点小魔法：
        如果将方法引用或 Lambda 表达式赋值给函数式接口（如果），
        Java 会适配你的赋值到目标接口。
        编译器会在后台把方法引用或 Lambda 表达式包装进实现目标接口的类的实例中。
         */
    }
}

/*
Java 8 引进 FP，使用 java.util.function 包。

java.util.function 包旨在创建一组完整的目标接口，
使得我们一般情况下不需再定义自己的接口。

java.util.function 包含了一组函数式接口，
使用了命名模式，顾名思义就能知道特定接口的作用。

【函数式接口基本命名准则】
1. 只处理对象：Function，Consumer，Predicate 等
2. 接收基本类型参数：LongConsumer，DoubleFunction，IntPredicate 等
3. 返回值为基本类型，用 To 表示：ToLongFunction（返回Long类型） 和 IntToLongFunction（接收Int返回Long）
4. 返回值类型与参数类型相同，用 Operator 表示：（单个）UnaryOperator、（两个）BinaryOperator
5. 接收参数，返回布尔值，用 Predicate 表示
6. 接收的两个参数类型不同，则名称中有一个 Bi
7. 无参数，返回类型任意，用 Supplier
 */

/**
 * 使用 java.util.function.Function 的一个例子
 */
class Foo{}
class Bar {
    Foo f;
    Bar(Foo f) {
        this.f = f;
    }
}

class IBaz {
    int i;
    IBaz(int i) {
        this.i = i;
    }
}
class LBaz {
    long l;
    LBaz(long l) {
        this.l = l;
    }
}
class DBaz {
    double d;
    DBaz(Double d) {
        this.d = d;
    }
}

class FunctionVariants {
    /* 接收对象 Foo，返回对象 Bar */
    static Function<Foo, Bar> f1 = foo -> new Bar(foo);

    /* 接收基本类型，返回 IBaz */
    static IntFunction<IBaz> f2 = i -> new IBaz(i);
    static LongFunction<LBaz> f3 = l -> new LBaz(l);
    static DoubleFunction<DBaz> f4 = d -> new DBaz(d);

    /* 接收对象 IBaz，返回 int */
    static ToIntFunction<IBaz> f5 = ib -> ib.i;
    static ToLongFunction<LBaz> f6 = lb -> lb.l;
    static ToDoubleFunction<DBaz> f7 = db -> db.d;

    /* 接收基本类型，返回基本类型 */
    // 在某些情况下有必要进行强制类型转换，否则编译器会报截断错误。
    static IntToLongFunction f8 = i -> i;
    static IntToDoubleFunction f9 = i -> i;

    static LongToIntFunction f10 = l -> (int) l;
    static LongToDoubleFunction f11 = l -> (double) l;

    static DoubleToIntFunction f12 = d -> (int) d;
    static DoubleToLongFunction f13 = d -> (long) d;

    public static void main(String[] args) {
        Bar b = f1.apply(new Foo());
        IBaz ib = f2.apply(12);
        LBaz lb = f3.apply(13);
        DBaz db = f4.apply(14);

        int i = f5.applyAsInt(ib);
        long l = f6.applyAsLong(lb);
        double d = f7.applyAsDouble(db);

        l = f8.applyAsLong(1);
        d = f9.applyAsDouble(2);

        i = f10.applyAsInt(1);
        d = f11.applyAsDouble(1);

        i = f12.applyAsInt(2);
        l = f13.applyAsLong(1);
    }
}


/*
对于方法引用，有自己的小魔法：
 */
class In1 {}
class In2 {}

class MethodConversion {
    static void accept(In1 i1, In2 i2) {
        System.out.println("accept()");
    }
    static void someOtherName(In1 i1, In2 i2) {
        System.out.println("someOtherName()");
    }

    public static void main(String[] args) {
        BiConsumer<In1, In2> bic;

        bic = MethodConversion::accept;
        bic.accept(new In1(), new In2());

        bic = MethodConversion::someOtherName;
        /* 没有这样的方法 */
        // bic.someOtherName(new In1(), new In2());
        bic.accept(new In1(), new In2());
    }
}

/*
又是一点小结：
在使用函数接口时，名称无关紧要，只要参数类型和返回类型相同。
Java 会将你的方法映射到接口方法。

要调用方法，可以调用接口的函数式方法名，而不是你的方法名。
 */

/*
将方法引用应用于基于类的函数式接口的例子：
 */
class AAA {}
class BBB {}
class CCC {}

class ClassFunctionals {
    static AAA f1() {
        return new AAA();
    }
    static int f2(AAA a1, AAA a2) {
        return 1;
    }
    static void f3(AAA a) {}
    static void f4(AAA a, BBB b) {}
    static CCC f5(AAA a) {
        return new CCC();
    }

    public static void main(String[] args) {
        Supplier<AAA> s = ClassFunctionals::f1;
        s.get();

        Comparator<AAA> c = ClassFunctionals::f2;
         int result = c.compare(new AAA(), new AAA());

        Consumer<AAA> cons = ClassFunctionals::f3;
        cons.accept(new AAA());

        BiConsumer<AAA, BBB> bicons = ClassFunctionals::f4;
        bicons.accept(new AAA(), new BBB());

        Function<AAA,CCC> f = ClassFunctionals::f5;
        CCC cc = f.apply(new AAA());
    }
}


/**
 * 【多参数函数式接口】
 * java.util.functional 中的接口是有限的。
 * 比如有 BiFunction，但也仅此而已。 如果需要三参数函数的接口怎么办？
 *
 * 展示如何定义三参数函数的函数式接口：
 */
@FunctionalInterface
interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
/*
测试：
 */
class TriFunctionTest {
    static int f(int i, long l, double d) {
        return 90;
    }

    public static void main(String[] args) {
        TriFunction<Integer, Long, Double, Integer> tf = TriFunctionTest::f;
        System.out.println(tf.apply(1, 2l, 3d));

        // Lambda
        tf  = (i, l, d) -> 12;
        System.out.println(tf.apply(1, 2L, 3D));
    }
}


/**
 * 【缺少基本类型的函数】
 * 展示如何创建各种缺失的预定义组合：
 */
class BiConsumerPermutations {
    static BiConsumer<Integer, Double> bicid = (i, d) -> System.out.format("%d, %f%n", i, d);
    static BiConsumer<Double, Integer> bicdi = (d, i) -> System.out.format("%d, %f%n", i, d);
    static BiConsumer<Integer, Long> bicil = (d, l) -> System.out.format("%d, %d%n", l, d);

    public static void main(String[] args) {
        bicid.accept(47, 11.34);
        bicdi.accept(22.45, 12);
        bicil.accept(1, 111L);
    }
}

/*
在上述代码中，简单使用了合适的包装类型，自动拆装箱负责与基本类型之间的来回转换。

可以使用包装类型和 Function 一起使用，而不去用各种针对基本类型的预定义接口。
 */
class FunctionWithWrapped {
    public static void main(String[] args) {
        /* 如果没有强制转换，则会收到错误消息：Integer cannot be converted to Double */
        Function<Integer, Double> fid = i -> (double) i;

        /* 使用 IntToDoubleFunction 就没有此类问题。 */
        IntToDoubleFunction itdf = i -> i;
        /*
        IntToDoubleFunction 源码为：
        @FunctionalInterface
        public interface IntToDoubleFunction {
            double applyAsDouble(int value);
        }
        用基本类型（IntToDoubleFunction）的唯一理由是
        可以避免传递参数和返回结果过程中的自动拆装箱，进而提升性能。
         */
    }
}
