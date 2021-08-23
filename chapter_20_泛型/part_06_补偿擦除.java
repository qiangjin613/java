/*
【补偿擦除】
因为擦除，我们将失去执行泛型代码中某些操作的能力。
 */

import com.sun.java.swing.plaf.windows.WindowsDesktopIconUI;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 如，无法在运行时知道确切类型：
 */
class Erased<T> {
    private final int SIZE = 100;

    public void f(Object arg) {
        // Error:(14, 28) java: instanceof 的泛型类型不合法
        // if (arg instanceof T) {}

        // Error:类型参数'T'不能直接实例化
        // T var = new T();
        // T[] array = new T[SIZE];

        // 编译器警告：Unchecked cast: 'java.lang.Object[]' to 'T[]'
        T[] array2 = (T[]) new Object[SIZE];
    }
}
/*
有时，我们可以对这些问题进行编程，但是有时必须通过引入 _类型标签_ 来补偿擦除。
这意味着为所需的类型显式传递一个 Class 对象，以在类型表达式中使用它。
接下来会对这些问题一一进行解决。
 */

/**
 * 由于擦除了类型信息，因此在上一个程序中尝试使用 instanceof 将会失败。
 * 但是，类型标签可以使用动态 isInstance() 方法达到同样的作用：
 */
class Building2 {}
class House2 extends Building2 {}
class ClassTypeCapture<T> {
    Class<T> kind;

    public ClassTypeCapture(Class<T> kind) {
        this.kind = kind;
    }

    public boolean f(Object arg) {
        return kind.isInstance(arg);
    }

    public static void main(String[] args) {
        ClassTypeCapture<Building2> ctt1 = new ClassTypeCapture<>(Building2.class);
        System.out.println(ctt1.f(new Building2())); // output: true
        System.out.println(ctt1.f(new House2())); // output: true

        ClassTypeCapture<House2> ctt2 = new ClassTypeCapture<>(House2.class);
        System.out.println(ctt2.f(new Building2())); // output: false
        System.out.println(ctt2.f(new House2())); // output: true
    }
}



/*
【创建类型的实例】
因为试图在 Erased.java 中 new T() 是行不通的，
部分原因是由于擦除，
部分原因是编译器无法验证 T 是否具有默认（无参）构造函数。
（这一点在 C++ 中是可以的）

Java 中的解决方案是传入一个工厂对象，并使用该对象创建新实例。
方便的工厂对象只是 Class 对象，因此，如果使用类型标记，则可以使用 newInstance() 创建该类型的新对象。
 */

/**
 * 使用工厂对象创建实例的例子：
 */
class ClassAsFactory<T> implements Supplier<T> {
    Class<T> kind;

    ClassAsFactory(Class<T> kind) {
        this.kind = kind;
    }

    @Override
    public T get() {
        try {
            return kind.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

class Employee {
    @Override
    public String toString() {
        return "employee";
    }
}

class InstantiateGenericType {
    public static void main(String[] args) {
        ClassAsFactory<Employee> fe = new ClassAsFactory<>(Employee.class);
        System.out.println(fe.get());

        ClassAsFactory<Integer> fi = new ClassAsFactory<>(Integer.class);
        System.out.println(fi.get());
        /*
        在这里回抛出异常：
        java.lang.InstantiationException: java.lang.Integer
         */
    }
}
/*
上述代码在运行时会抛出异常，这是由于 Integer 没有无参构造器造成的。
因为这种错误不是在编译时捕获的，因此语言创建者不赞成这种方法。

他们建议使用显式工厂（Supplier）并约束类型，以便只有实现该工厂的类可以这样创建对象。
（是创建工厂的两种不同方法）
 */

/**
 * 显式工厂的一个例子：
 */
class IntegerFactory implements Supplier<Integer> {
    private int i = 0;

    @Override
    public Integer get() {
        return ++i;
    }
}

class Widget {
    private int id;

    Widget(int n) {
        id = n;
    }

    @Override
    public String toString() {
        return "Widget{" + "id=" + id + '}';
    }

    public static class Factory implements Supplier<Widget> {
        private int i = 0;
        @Override
        public Widget get() {
            return new Widget(++i);
        }
    }
}

class Fudge {
    private static int count = 1;
    private int n = count++;

    @Override
    public String toString() {
        return "Fudge{" + "n=" + n + '}';
    }
}

class Foo2<T> {
    private List<T> x = new ArrayList<>();

    Foo2(Supplier<T> factory) {
        Stream.generate(factory)
                .limit(5)
                .forEach(x::add);
    }

    @Override
    public String toString() {
        return x.toString();
    }
}

class FactoryConstraint {
    public static void main(String[] args) {
        System.out.println(new Foo2<>(new IntegerFactory()));
        System.out.println(new Foo2<>(new Widget.Factory()));
        System.out.println(new Foo2<>(Fudge::new));
    }
}
/*
上例中，IntegerFactory 和 Widget.Factory 都是工厂。

在 Fudge 中，并没有做任何类似于工厂的操作，但传递 Fudge::new 仍然会产生工厂行为。
因为编译器将对函数方法 ::new 的调用转换为对 get() 的调用。
 */


/*
另一种方法是模板方法设计模式。
 */

/**
 * 模板模式的例子：
 */
abstract class GenericWithCreate<T> {
    final T element;

    GenericWithCreate() {
        element = create();
    }

    abstract T create();
}

class XXX {}

class XCreate extends GenericWithCreate<XXX> {
    @Override
    XXX create() {
        return new XXX();
    }

    void f() {
        System.out.println(element.getClass().getSimpleName());
    }
}

class CreatorGeneric {
    public static void main(String[] args) {
        XCreate xc = new XCreate();
        xc.f();
    }
}




/*
【创建泛型数组】
Erased.java 看到，我们无法创建泛型数组。
通用解决方案是在试图创建泛型数组的时候使用 ArrayList 代替直接创建泛型数组。
 */

/**
 * 如下，使用 ArrayList 创建数组：
 */
class ListOfGenerics<T> {
    private List<T> array = new ArrayList<>();

    public void add(T item) {
        array.add(item);
    }

    public T get(int index) {
        return array.get(index);
    }
}
/*
通过这种方式，不仅可以获得数组的行为，并且还具有泛型提供的编译时类型安全性。
 */

/**
 * 有时，仍然会创建泛型类型的数组。
 * 可以通过使编译器满意的方式定义对数组的通用引用：
 */
class Generic<T> {}
class ArrayOfGenericReference {
    static Generic<Integer>[] gia;
}

/**
 * 如果将上述创建 ArrayList 的例子进行扩展，创建 Object 类型的数组是否可以？
 * 因为所有数组，无论它们持有什么类型，都具有相同的结构（每个数组插槽的大小和数组布局）
 * 测试如下：
 */
class ArrayOfGeneric {
    static final int SIZE = 100;
    static Generic<Integer>[] gia;

    public static void main(String[] args) {
        try {
            /* 抛出异常 */
            gia = (Generic<Integer>[]) new Object[SIZE];
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
            /* output:
            [Ljava.lang.Object; cannot be cast to [LGeneric;
             */
        }

        /* 可以正常执行 */
        gia = (Generic<Integer>[]) new Generic[SIZE];
        System.out.println(gia.getClass().getSimpleName());
        gia[0] = new Generic<>();
        System.out.println(gia[0]);

        // 编译错误：编译时，类型不匹配
        // gia[1] = new Object();
        // 编译错误：编译时，类型不匹配
        // gia[2] = new Generic<Double>();
    }
}
/*
对于数组会跟踪其实际类型（因为要确定空间开辟的大小），而该类型是在创建数组时建立的。
因此，即使 gia 被强制转换为 Generic<Integer>[] ，该信息也仅在编译时存在。
在运行时，它仍然是一个 Object 数组，这会引起问题。

成功创建泛型类型的数组的唯一方法是创建一个已擦除类型的新数组，并将其强制转换。
如：
    Generic<Integer>[] gia = (Generic<Integer>[]) new Generic[SIZE];
 */



/**
 * 看一个更复杂的示例。
 * 考虑一个包装数组的简单泛型包装器：
 */
class GenericArray<T> {
    private T[] array;

    public GenericArray(int sz) {
        array = (T[]) new Object[sz];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    // 返回 array，公开底层实现
    public T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        GenericArray<Integer> gai = new GenericArray<>(10);
        try {
            // 抛出异常
            Integer[] ia = gai.rep();
            /*
            [Ljava.lang.Object; cannot be cast to [Ljava.lang.Integer;
             */
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }

        // 这样是可以的：
        Object[] oa = gai.rep();
    }
}
/*
观察上述方法：
（和以前一样，我们不能说 T[] array = new T[sz] ，所以我们创建了一个 Object 数组并将其强制转换）
rep() 方法返回一个 T[] ，在主方法中它应该是 gai 的 Integer[]，
但是如果调用它并尝试将结果转换为 Integer[] 引用，则会得到 ClassCastException，
这再次是因为实际的运行时类型为 Object[] 。

由于擦除，数组的运行时类型只能是 Object[]。
如果我们立即将其转换为 T[]，则在编译时会丢失数组的实际类型，
并且编译器可能会错过一些潜在的错误检查。
因此，最好在集合中使用 Object[]，并在使用数组元素时向 T 添加强制类型转换。
 */

/**
 * 如下：
 */
class GenericArray2<T> {
    private Object[] array;

    public GenericArray2(int sz) {
        array = new Object[sz];
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return (T) array[index];
    }

    public T[] rep() {
        return (T[]) array;
    }

    public static void main(String[] args) {
        GenericArray2<Integer> gai = new GenericArray2<>(10);
        for (int i = 0; i < 10; i++) {
            gai.put(i, i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(gai.get(i) + " ");
        }

        try {
            Integer[] ia = gai.rep();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            /* output:
            [Ljava.lang.Object; cannot be cast to [Ljava.lang.Integer;
             */
        }
    }
}
/*
这个例子与上个示例的最大不同是，使用了 Object[] 作为数组类型而不是 T[]。
调用 get() 时，它将对象强制转换为 T ，实际上这是正确的类型，因此很安全。

但是，如果调用 rep() ，它将再次尝试将 Object[] 强制转换为 T[]，
但仍然不正确，并在编译时生成警告，并在运行时生成异常。
因此，无法破坏基础数组的类型，该基础数组只能是 Object[]。

在内部将数组视为 Object[] 而不是 T[] 的优点是，
我们不太可能会忘记数组的运行时类型并意外地引入了bug，
尽管大多数（也许是全部）此类错误会在运行时被迅速检测到。
 */


/**
 * 对于新代码，最好使用类型标记与 Array.newInstance() 进行创建数组。
 * 如下：
 */
class GenericArrayWithTypeToken<T> {
    private T[] array;

    // 类型标记 Class<T> 被传递到构造中（使用强制转型）以从擦除中恢复，
    // 可以使用 @SuppressWarnings 关闭来自强制类型转换的警告
    public GenericArrayWithTypeToken(Class<T> type, int sz) {
        array = (T[]) Array.newInstance(type, sz);
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    public T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        GenericArrayWithTypeToken<Integer> gai = new GenericArrayWithTypeToken<>(Integer.class, 10);

        // 是正常工作的
        Integer[] ia = gai.rep();
        System.out.println(Arrays.toString(ia));
    }
}
/*
从上述示例中，看到，可以使用 Array.newInstance() 创建实际类型的数组。
一旦获得了实际的类型，就可以返回它并产生所需的结果，如 rep() 中那样。
 */

/*
不幸的是，如果查看 Java 标准库中的源代码，你会发现到处都有从 Object 数组到参数化类型的转换。

Neal Gafter（Java 5 的主要开发人员之一）在他的博客中指出，
他在重写 Java 库时是很随意、马虎的，我们不应该像他那样做。
Neal 还指出，他在不破坏现有接口的情况下无法修复某些 Java 库代码。
因此，即使在 Java 库源代码中出现了一些习惯用法，它们也不一定是正确的做法。
当查看库代码时，我们不能认为这就是要在自己代码中必须遵循的示例。
 */
