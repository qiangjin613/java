/*
【泛型擦除】
当你开始更深入地钻研泛型时，会发现有大量的东西初看起来是没有意义的。
 */

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 例如，尽管可以说 ArrayList.class，但不能说成 ArrayList<Integer>.class。
 * 考虑下面的情况：
 */
class ErasedTypeEquivalence {
    public static void main(String[] args) {
        Class c1 = new ArrayList<String>().getClass();
        Class c2 = new ArrayList<Integer>().getClass();
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c1 == c2);
    }
}
/*
ArrayList<String> 和 ArrayList<Integer> 应该是不同的类型。不同的类型会有不同的行为。
然而上面的程序认为它们是相同的类型。
 */

/**
 * 再一个例子，对该谜题的补充：
 */
class Frob {}
class Fnorkle {}
class Quark<Q> {}
class Particle<POSTION, MOMENTUM> {}
class LostInformation {
    public static void main(String[] args) {
        List<Frob> list = new ArrayList<>();
        Map<Frob, Fnorkle> map = new HashMap<>();
        Quark<Fnorkle> quark = new Quark<>();
        Particle<Long, Double> p = new Particle<>();

        System.out.println(Arrays.toString(list.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(map.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(quark.getClass().getTypeParameters()));
        System.out.println(Arrays.toString(p.getClass().getTypeParameters()));
    }
}
/*
TypeVariable<Class<T>>[] getTypeParameters()，TypeVariable 对象数组，表示泛型声明中声明的类型参数。
暗示你可以发现这些参数类型。但是正如上例中输出所示，你只能看到用作参数占位符的标识符，这并非有用的信息。

** 残酷的现实是：在泛型代码内部，无法获取任何有关泛型参数类型的信息。
** 因此，你可以知道如类型参数标识符和泛型边界这些信息，但无法得知实际的类型参数从而用来创建特定的实例。
如果你曾是 C++ 程序员，那么这个事实会让你很沮丧，在使用 Java 泛型工作时，它是必须处理的最基本的问题。

** Java 泛型是使用擦除实现的。
这意味着当你在使用泛型时，任何具体的类型信息都被擦除了，你唯一知道的就是你在使用一个对象。
因此，List<String> 和 List<Integer> 在运行时实际上是相同的类型。它们都被擦除成原生类型 List。

理解擦除并知道如何处理它，是你在学习 Java 泛型时面临的最大障碍之一。
 */

/*
【C++ 的方式】
下面是使用模版的 C++ 示例。你会看到类型参数的语法十分相似，因为 Java 是受 C++ 启发的：
---------------
#include <iostream>
using namespace std;

template<class T> class Manipulator {
    T obj;
public:
    Manipulator(T x) { obj = x; }
    void manipulate() { obj.f(); }
};

class HasF {
public:
    void f() { cout << "HasF::f()" << endl; }
};

int main() {
    HasF hf;
    Manipulator<HasF> manipulator(hf);
    manipulator.manipulate();  // output: HasF::f()
}
---------------
Manipulator 类存储了一个 T 类型的对象。manipulate() 方法会调用 obj 上的 f() 方法。
可是，它是如何知道类型参数 T 中存在 f() 方法的呢？

C++ 编译器会在你实例化模版时进行检查，
所以在 Manipulator<HasF> 实例化的那一刻，它看到 HasF 中含有一个方法 f()。
（如果 HasF 没有 f() 就会报错）

用 C++ 编写这种代码很简单，因为当模版被实例化时，模版代码就知道模版参数的类型。
Java 泛型就不同了。
 */

/**
 * HasF 的 Java 版本：
 */
class Manipulator<T> {
    private T obj;
    public Manipulator(T x) {
        obj = x;
    }
    public void manipulate() {
        /* 这里将编译失败 */
        // obj.f();
    }
}

class HasF {
    public void f() {
        System.out.println("HasF::f()");
    }
}

class Manipulation {
    public static void main(String[] args) {
        HasF hf = new HasF();
        Manipulator<HasF> m = new Manipulator<>(hf);
        m.manipulate();
    }
}
/*
因为擦除，Java 编译器无法将 manipulate() 方法必须能调用 obj 的 f() 方法这一需求
映射到 HasF 具有 f() 方法这个事实上。
为了调用 f()，我们必须协助泛型类，给定泛型类一个边界，以此告诉编译器只能接受遵循这个边界的类型。
 */

/**
 * 下面给泛型类一个边界，实现上述不能完成的效果：
 */
class Manipulator2<T extends HasF> {
    private T obj;
    public Manipulator2(T x) {
        obj = x;
    }
    public void manipulate() {
         obj.f();
    }
}
/*
边界 <T extends HasF> 声明了 T 必须是 HasF 类型或其子类。
这样一来，就可以在 obj 上安全的调用 f() 方法。
 */


/*
我们说泛型类型参数会擦除到它的第一个边界（可能有多个边界，稍后你将看到）。
我们还提到了类型参数的擦除。编译器实际上会把类型参数替换为它的擦除，
就像上面的示例，T 擦除到了 HasF，就像在类的声明中用 HasF 替换了 T 一样。
 */

/**
 * 可能正确地观察到了 泛型 在 Manipulator2.java 中没有贡献任何事。
 * 我们可以很轻松地自己去执行擦除，生成没有泛型的类：
 */
class Manipulator3 {
    private HasF obj;
    public Manipulator3(HasF x) {
        obj = x;
    }
    public void manipulate() {
        obj.f();
    }
}


/*
（虽然这样，不代表泛型就没有用）
这提出了很重要的一点：
泛型只有在类型参数比某个具体类型（以及其子类）更加“泛化”——代码能跨多个类工作时才有用。
因此，类型参数和它们在有用的泛型代码中的应用，通常比简单的类替换更加复杂。
但是，不能因此认为使用 <T extends HasF> 形式就是有缺陷的。

例如，如果某个类有一个返回 T 的方法，那么泛型就有所帮助，因为它们之后将返回确切的类型：
 */

/**
 * 使用泛型的一个原因：
 */
class ReturnGenericType<T extends HasF> {
    private T obj;
    public ReturnGenericType(T x) {
        obj = x;
    }
    public void manipulate() {
        obj.f();
    }

    public T get() {
        return obj;
    }
}


/*
必须查看所有的代码，从而确定代码是否复杂到必须使用泛型的程度。
 */


/*
【迁移兼容性】
** 为了减少潜在的关于擦除的困惑，你必须清楚地认识到这不是一个语言特性。
** 它是 Java 实现泛型的一种妥协，因为泛型不是 Java 语言出现时就有的，所以就有了这种妥协。
它会使你痛苦，因此你需要尽早习惯它并了解为什么它会这样。

如果 Java 1.0 就含有泛型的话，那么这个特性就不会使用擦除来实现——它会使用具体化，保持参数类型为第一类实体，
因此你就能在类型参数上执行基于类型的语言操作和反射操作。
本章稍后你会看到，擦除减少了泛型的泛化性。
泛型在 Java 中仍然是有用的，只是不如它们本来设想的那么有用，而原因就是擦除。

在基于擦除的实现中，泛型类型被当作第二类类型处理，即不能在某些重要的上下文使用泛型类型。
** 泛型类型只有在静态类型检测期间才出现，在此之后，程序中的所有泛型类型都将被擦除，替换为它们的非泛型上界。
** 例如， List<T> 这样的类型注解会被擦除为 List，普通的类型变量在未指定边界的情况下会被擦除为 Object。

擦除的核心动机是你可以在泛化的客户端上使用非泛型的类库，反之亦然。这经常被称为“迁移兼容性”。
在理想情况下，所有事物将在指定的某天被泛化。
在现实中，即使程序员只编写泛型代码，他们也必须处理 Java 5 之前编写的非泛型类库。
这些类库的作者可能从没想过要泛化他们的代码，或许他们可能刚刚开始接触泛型。

** 因此 Java 泛型不仅必须支持向后兼容性——现有的代码和类文件仍然合法，继续保持之前的含义——而且还必须支持迁移兼容性，
** 使得类库能按照它们自己的步调变为泛型，当某个类库变为泛型时，不会破坏依赖于它的代码和应用。
在确定了这个目标后，Java 设计者们和从事此问题相关工作的各个团队决策认为擦除是唯一可行的解决方案。
擦除使得这种向泛型的迁移成为可能，允许非泛型的代码和泛型代码共存。

例如，假设一个应用使用了两个类库 X 和 Y，Y 使用了类库 Z。
随着 Java 5 的出现，这个应用和这些类库的创建者最终可能希望迁移到泛型上。
但是当进行迁移时，它们有着不同的动机和限制。
为了实现迁移兼容性，每个类库与应用必须与其他所有的部分是否使用泛型无关。
因此，它们不能探测其他类库是否使用了泛型。
因此，某个特定的类库使用了泛型这样的证据必须被”擦除“。

如果没有某种类型的迁移途径，所有已经构建了很长时间的类库就需要与希望迁移到 Java 泛型上的开发者们说再见了。
类库毫无争议是编程语言的一部分，对生产效率有着极大的影响，所以这种代码无法接受。
擦除是否是最佳的或唯一的迁移途径，还待时间来证明。
 */


/*
【擦除的问题】
** 因此，擦除主要的正当理由是从非泛化代码到泛化代码的转变过程，
** 以及在不破坏现有类库的情况下将泛型融入到语言中。
擦除允许你继续使用现有的非泛型客户端代码，直至客户端准备好用泛型重写这些代码。
这是一个崇高的动机，因为它不会骤然破坏所有现有的代码。

** 擦除的代价是显著的。泛型不能用于显式地引用运行时类型的操作中，
** 例如转型、instanceof 操作和 new 表达式。
因为所有关于参数的类型信息都丢失了，当你在编写泛型代码时，必须时刻提醒自己，
你只是看起来拥有有关参数的类型信息而已。
 */

/**
 * 一个例子：
 */
class Demo<T> {
    T var;
    Demo(T x) {
        var = x;
    }

    void desc() {
        System.out.println(this.var.getClass().getSimpleName());
    }
}
class DemoTest {
    public static void main(String[] args) {
        Demo<String> d = new Demo<>("strings");
        d.desc(); // output: String
        System.out.println(d.var.getClass().getSimpleName()); // output: String
    }
}
/*
不是说 T 被擦除为 Object 了？ 怎么还是输出 String 呢？
 */

/*
** 另外，擦除和迁移兼容性意味着，使用泛型并不是强制的。
 */
class GenericBase<T> {
    private T element;
    public void set(T arg) {
        element = arg;
    }
    public T get() {
        return element;
    }
}
class Derived3<T> extends GenericBase<T> {}
// 一个不使用泛型的版本：编译器警告（参数化类'GenericBase'的原始使用）
class Derived4 extends GenericBase {}
class Derived6<T> extends GenericBase {}

// 错误的示例：Cannot resolve symbol 'T'  无法解析符号“T”
// class Derived5 extends GenericBase<T> {}

class ErasureAndInteritance {
    public static void main(String[] args) {
        Derived4 d = new Derived4();
        Object obj = d.get();
        // 编译器警告：Unchecked call to 'set(T)' as a member of raw type 'GenericBase'
        d.set(obj);
    }
}
/*
Derived4 继承自 GenericBase，但是没有任何类型参数，编译器没有发出任何警告。
直到调用 set() 方法时才出现警告。
 */

/*
当你希望将类型参数不仅仅当作 Object 处理时，就需要付出额外努力来管理边界，
并且与在 C++、Ada 和 Eiffel 这样的语言中获得参数化类型相比，
你需要付出多得多的努力来获得少得多的回报。
这并不是说，对于大多数的编程问题而言，这些语言通常都会比 Java 更得心应手，
只是说它们的参数化类型机制相比 Java 更灵活、更强大。
 */


/*
【边界处的动作】
因为擦除，我发现了泛型最令人困惑的方面是可以表示没有任何意义的事物。
 */

/**
 * 一个例子：
 */
class ArrayMaker<T> {
    private Class<T> kind;

    public ArrayMaker(Class<T> kind) {
        this.kind = kind;
    }

    T[] create(int size) {
        // 这里必须转型，因为 Class<T> 被擦除为 Class
        return (T[]) Array.newInstance(kind, size);
    }

    public static void main(String[] args) {
        ArrayMaker<String> stringMaker = new ArrayMaker<>(String.class);
        String[] strings = stringMaker.create(9);
        System.out.println(Arrays.toString(strings));
        /* output:
        [null, null, null, null, null, null, null, null, null]
         */
    }
}
/*
即使 kind 被存储为 Class<T>，擦除也意味着它实际被存储为没有任何参数的 Class。
因此，当你在使用它时，例如创建数组，Array.newInstance() 实际上并未拥有 kind 所蕴含的类型信息。
所以它不会产生具体的结果，因而必须转型，这会产生一条令你无法满意的警告。

还有一点，对于在泛型中创建数组，使用 Array.newInstance() 是推荐的方式。
 */

/**
 * 另一个例子：如果创建一个集合而不是数组，情况就不同了
 */
class ListMaker<T> {
    List<T> create() {
        return new ArrayList<>();
    }

    List<T> create2() {
        /* 编译器警告 */
        return new ArrayList();
    }

    public static void main(String[] args) {
        ListMaker<String> stringMaker = new ListMaker<>();
        List<String> stringList = stringMaker.create();
    }
}
/*
create() 中，编译器不会给出任何警告，
尽管我们知道（从擦除中）在 create() 内部的 new ArrayList<>() 中的 <T> 在运行时被移除了，
类内部没有任何 <T>，因此这看起来毫无意义。

但是如果你遵从这种思路，并将这个表达式改为 create2()，编译器就会发出警告。
 */

/**
 * 上述例子中这么做真的毫无意义吗？
 * 如果在创建 List 的同时向其中放入一些对象呢，
 * 像这样：
 */
class FilledList<T> extends ArrayList<T> {
    public FilledList(Supplier<T> gen, int size) {
        Stream.generate(gen)
                .limit(size)
                .forEach(this::add);
    }
    public FilledList(T t, int size) {
        for (int i = 0; i < size; i++) {
            this.add(t);
        }
    }

    public static void main(String[] args) {
        List<Integer> ilist = new FilledList<>(() -> 47, 4);
        System.out.println(ilist);
        /*
        [47, 47, 47, 47]
         */

        List<String> list = new FilledList<>("Hello", 4);
        System.out.println(list);
        /*
        [Hello, Hello, Hello, Hello]
         */
    }
}
/*
即使编译器无法得知 add() 中的 T 的任何信息，但它仍可以在编译期确保你放入 FilledList 中的对象是 T 类型。
因此，即使擦除移除了方法或类中的实际类型的信息，编译器仍可以确保方法或类中使用的类型的内部一致性。

因为擦除移除了方法体中的类型信息，所以在运行时的问题就是边界：即对象进入和离开方法的地点。
这些正是编译器在编译期执行类型检查并插入转型代码的地点。
 */

/**
 * 考虑下面非泛型的示例：
 */
class SimpleHolder {
    private Object obj;

    public void set(Object obj) {
        this.obj = obj;
    }

    public Object get() {
        return obj;
    }

    public static void main(String[] args) {
        SimpleHolder holder = new SimpleHolder();
        holder.set("Item");
        String s = (String) holder.get();
    }
}
/*
如果用 javap -c SimpleHolder 反编译这个类，会得到如下内容（经过编辑）：
public void set(java.lang.Object);
   0: aload_0
   1: aload_1
   2: putfield #2; // Field obj:Object;
   5: return

public java.lang.Object get();
   0: aload_0
   1: getfield #2; // Field obj:Object;
   4: areturn

public static void main(java.lang.String[]);
   0: new #3; // class SimpleHolder
   3: dup
   4: invokespecial #4; // Method "<init>":()V
   7: astore_1
   8: aload_1
   9: ldc #5; // String Item
   11: invokevirtual #6; // Method set:(Object;)V
   14: aload_1
   15: invokevirtual #7; // Method get:()Object;
   18: checkcast #8; // class java/lang/String
   21: astore_2
   22: return

看到，set() 和 get() 方法存储和产生值，转型在调用 get() 时接受检查。
 */

/**
 * 现将泛型融入上例代码中：
 */
class GenericHolder2<T> {
    private T obj;

    public void set(T obj) {
        this.obj = obj;
    }

    public T get() {
        return obj;
    }

    public static void main(String[] args) {
        GenericHolder2<String> holder = new GenericHolder2<>();
        holder.set("Item");
        String s = holder.get();
    }
}
/*
继续用 javap -c SimpleHolder 反编译这个类，得到如下内容（经过编辑）：
public void set(java.lang.Object);
   0: aload_0
   1: aload_1
   2: putfield #2; // Field obj:Object;
   5: return

public java.lang.Object get();
   0: aload_0
   1: getfield #2; // Field obj:Object;
   4: areturn

public static void main(java.lang.String[]);
   0: new #3; // class GenericHolder2
   3: dup
   4: invokespecial #4; // Method "<init>":()V
   7: astore_1
   8: aload_1
   9: ldc #5; // String Item
   11: invokevirtual #6; // Method set:(Object;)V
   14: aload_1
   15: invokevirtual #7; // Method get:()Object;
   18: checkcast #8; // class java/lang/String
   21: astore_2
   22: return

所产生的字节码是相同的。
对进入 set() 的类型进行检查是不需要的，因为这将由编译器执行。
而对 get() 返回的值进行转型仍然是需要的，只不过不需要你来操作，
它由编译器自动插入，这样你就不用编写（阅读）杂乱的代码。

SimpleHolder 和 GenericHolder2 中的 get()、set() 产生了相同的字节码，
** 这就告诉我们泛型的所有动作（对入参的编译器检查和对返回值的转型）都发生在边界处。
** 这有助于澄清对擦除的困惑，记住：“边界就是动作发生的地方”。
 */
