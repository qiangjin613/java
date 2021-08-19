/*
【泛型方法】
到目前为止，我们已经研究了参数化整个类。
其实还可以参数化类中的方法。

** 类本身可能是泛型的，也可能不是，不过这与它的方法是否是泛型的并没有什么关系。
** 泛型方法独立于类而改变方法。作为准则，请“尽可能”使用泛型方法。通常将单个方法泛型化要比将整个类泛型化更清晰易懂。
** 如果方法是 static 的，则无法访问该类的泛型类型参数，因此，如果使用了泛型类型参数，则它必须是泛型方法。

要定义泛型方法，请将泛型参数列表放置在返回值之前
 */


import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 一个定义泛型方法的例子：
 */
class GenericMethods {
    public <T> void f(T x) {
        System.out.println(x.getClass().getName());
    }

    public static void main(String[] args) {
        GenericMethods gm = new GenericMethods();
        gm.f("");
        gm.f(1);
        gm.f(1.0);
        gm.f(1.0F);
        gm.f('c');
        gm.f(gm);
    }
}
/*
尽管可以同时对类及其方法进行参数化，但这里未将 GenericMethods 类参数化。
只有方法 f() 具有类型参数，该参数由方法返回类型之前的参数列表指示。

---泛型方法的类型推断---
对于泛型类，必须在实例化该类时指定类型参数。
使用泛型方法时，通常不需要指定参数类型，因为编译器会找出这些类型。这称为类型参数推断。

因此，对 f() 的调用看起来像普通的方法调用，并且 f() 看起来像被重载了无数次一样。
它甚至会接受 GenericMethods 类型的参数。

如果使用基本类型调用 f()，自动装箱就开始起作用，自动将基本类型包装在它们对应的包装类型中。
 */



/*
【变长参数和泛型方法】
泛型方法和变长参数列表可以很好地共存。
 */

/**
 * 一个类似于 Arrays.asList() 的例子：
 */
class GenericVarargs {
    public static <T> List<T> makeList(T... args) {
        List<T> result = new ArrayList<>();
        for (T item : args) {
            result.add(item);
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> ls = makeList("A");
        System.out.println(ls);

        ls = makeList("A", "B", "C");
        System.out.println(ls);

        ls = makeList("ABCDEFG".split(""));
        System.out.println(ls);
    }
}



/*
【一个泛型的 Supplier】
 */

/**
 * 这是一个为任意具有无参构造方法的类生成 Supplier 的类。
 * 为了减少键入，它还包括一个用于生成 BasicSupplier 的泛型方法：
 */
class BasicSupplier<T> implements Supplier<T> {
    private Class<T> type;

    public BasicSupplier(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /* 说明：这个泛型方法的 T 与 泛型类的 T 是相互独立的，二者没有任何关系 */
    public static <T> Supplier<T> create(Class<T> type) {
        return new BasicSupplier<>(type);
    }
}
/**
 * 提供一个具有无参构造器的简单类（供 BasicSupplier 使用）：
 */
class CountedObject {
    private static long counter = 0;
    private final long id = counter++;

    public long id() {
        return id;
    }

    @Override
    public String toString() {
        return "CountedObject{" + "id=" + id + '}';
    }
}
/**
 * BasicSupplier 的 Demo：
 */
class BasicSupplierDemo {
    public static void main(String[] args) {
        Stream.generate(BasicSupplier.create(CountedObject.class))
                .limit(5)
                .forEach(System.out::println);
    }
}




/*
【简化元组的使用】
使用类型参数推断和静态导入，我们可以把早期的元组重写为更通用的库。
 */

/**
 * 在这里，重载的静态方法将之前的元组进行组装：
 */
class Tuple {
    public static <A, B> Tuple2<A, B> tuple(A a,B b) {
        return new Tuple2<>(a, b);
    }
    public static <A, B, C> Tuple3<A, B, C> tuple(A a, B b, C c) {
        return new Tuple3<>(a, b, c);
    }
}

/**
 * TupleTestDemo：
 */
class TupleTestDemo {
    static Tuple2<String, Integer> f() {
        return Tuple.tuple("hi", 47);
    }

    /* 这里编译器发出警告 */
    static Tuple2 f2() {
        return Tuple.tuple("hi", 47);
    }

    static Tuple3<Amphibian, String, Integer> g() {
        return Tuple.tuple(new Amphibian(), "hi", 47);
    }

    public static void main(String[] args) {
        Tuple2<String, Integer> tsi = f();
        System.out.println(tsi);

        /* 这里编译器发出警告 */
        Tuple2 tuple2 = f2();
        System.out.println(tuple2);

        System.out.println(g());
    }
}



/*
【一个 Set 工具】
 */

/**
 * 对于泛型方法的另一个示例，请考虑由 Set 表示的数学关系。
 * 这些被方便地定义为可用于所有不同类型的泛型方法：
 */
class Sets {
    // 并集
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }

    // 交集
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    // 差集
    public static <T> Set<T> difference(Set<T> superset, Set<T> subset) {
        Set<T> result = new HashSet<>(superset);
        result.removeAll(subset);
        return result;
    }

    // 补集
    public static <T> Set<T> complement(Set<T> a, Set<T> b) {
        return difference(union(a, b), intersection(a, b));
    }
}

/**
 * 一个示例：（结合 Enum）
 */
enum Watercolors {
    ZINC, LEMON_YELLOW, MEDIUM_YELLOW, DEEP_YELLOW,
    ORANGE, BRILLIANT_RED, CRIMSON, MAGENTA,
    ROSE_MADDER, VIOLET, CERULEAN_BLUE_HUE,
    PHTHALO_BLUE, ULTRAMARINE, COBALT_BLUE_HUE,
    PERMANENT_GREEN, VIRIDIAN_HUE, SAP_GREEN,
    YELLOW_OCHRE, BURNT_SIENNA, RAW_UMBER,
    BURNT_UMBER, PAYNES_GRAY, IVORY_BLACK
}
class WatercolorSets {
    public static void main(String[] args) {
        Set<Watercolors> set1 = EnumSet.range(Watercolors.BRILLIANT_RED, Watercolors.ULTRAMARINE);
        Set<Watercolors> set2 = EnumSet.range(Watercolors.CRIMSON, Watercolors.RAW_UMBER);

        System.out.println("set1: " + set1);
        System.out.println("set2: " + set2);

        System.out.println(Sets.union(set1, set2));
        System.out.println(Sets.intersection(set1, set2));
        System.out.println(Sets.difference(set1, set2));
        System.out.println(Sets.complement(set1, set2));
    }
}
/*
EnumSet 是 java.util 包中的工具类，EnumSet.range() 轻松从 enum 中创建 Set。
 */

/**
 * 另一个使用上述工具的例子，
 * 展示 java.util 包中各种 Collection 和 Map 类之间的方法差异：
 */
















