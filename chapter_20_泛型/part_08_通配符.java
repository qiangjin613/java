/*
【通配符】
 */

import java.util.*;

/**
 * 起始示例要展示数组的一种特殊行为：
 * 可以将派生类的数组赋值给基类的引用：
 */
class Fruit3 {}
class Orange3 extends Fruit3 {}
class Apple3 extends Fruit3 {}
class Jonathan3 extends Apple3 {}

class CovariantArrays {
    public static void main(String[] args) {
        Fruit3[] fruit = new Apple3[10];
        // 下面的两条语句是合法的：
        fruit[0] = new Apple3();
        fruit[1] = new Jonathan3();

        // 会抛出异常的两条语句：
        try {
            fruit[2] = new Fruit3();
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            fruit[3] = new Orange3();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
/*
在上例中，main() 中的第一行创建了 Apple 数组，并赋值给一个 Fruit 数组引用。
这是有意义的，因为 Apple 也是一种 Fruit，因此 Apple 数组应该也是一个 Fruit 数组。

但是，如果实际的数组类型（运行时类型）是 Apple[]，
你可以在其中放置 Apple 或 Apple 的子类型，这在编译期和运行时都可以工作。
但是你也可以在数组中放置 Fruit 对象。这对编译器来说是有意义的，
因为它有一个 Fruit[] 引用——它有什么理由不允许将 Fruit 对象或任何从 Fruit 继承出来的对象（比如 Orange），
放置到这个数组中呢？因此在编译期，这是允许的。
然而，运行时的数组机制知道它处理的是 Apple[]，因此会在向数组中放置异构类型时抛出异常。

向上转型用在种情况下是不合适的。
（
【个人看法】因为数组的大小和类型在运行时就已经确定了，如下：
Fruit3[] fruit = new Apple3[10]; 的编译类型是 Fruit，但实际类型确是 Apple
这时在编译期 fruit[2] = new Fruit3(); 和 fruit[3] = new Orange3();
是允许的，但在运行时，Fruit 或者 Orange 转型为 Apple 都是不合法的。
）
你真正在做的是将一个数组赋值给另一个数组。
数组的行为是持有其他对象，这里只是因为我们能够向上转型而已，
所以很明显，数组对象可以保留有关它们包含的对象类型的规则。
看起来就像数组对它们持有的对象是有意识的，因此在编译期检查和运行时检查之间，你不能滥用它们。

数组的这种赋值并不是那么可怕，因为在运行时你可以发现插入了错误的类型。

但是泛型的主要目标之一是将这种错误检测移到编译期。
所以当我们试图使用泛型集合代替数组时，会发生什么呢？
 */

class NonCovariantGenerics {
    // Error: 不兼容的类型: java.util.ArrayList<Apple3>无法转换为java.util.List<Fruit3>
    // List<Fruit3> flist = new ArrayList<Apple3>();
}

/*
尽管在 NonCovariantGenerics 类中，编译器报错，无法将 x 转换为 y。
但记住，泛型不仅仅是关于集合，
它真正要表达的是“不能把一个涉及 Apple 的泛型赋值给一个涉及 Fruit 的泛型”。

如果像在数组中的情况一样，编译器对代码的了解足够多，可以确定所涉及到的集合，
那么它可能会留下一些余地。
但是它不知道任何有关这方面的信息，因此它拒绝向上转型。

然而实际上这也不是向上转型—— Apple 的 List 不是 Fruit 的 List。
Apple 的 List 在类型上不等价于 Fruit 的 List，即使 Apple 是一种 Fruit 类型。

--------------------

真正的问题是我们在讨论的集合类型，而不是集合持有对象的类型。
而不是集合持有对象的类型。与数组不同，泛型没有内建的协变类型。
这是因为数组是完全在语言中定义的，因此可以具有编译期和运行时的内建检查，
但是在使用泛型时，编译器和运行时系统不知道你想用类型做什么，以及应该采用什么规则。（具体类型被擦除了）

但是，有时你想在两个类型间建立某种向上转型关系。通配符可以产生这种关系。
 */

class GenericsAndCovariance {
    public static void main(String[] args) {
        List<? extends Fruit3> flist = new ArrayList<>();
        // 编译错误：不能添加任何类型的对象：
        // flist.add(new Apple3());
        // flist.add(new Fruit3());
        // flist.add(new Orange3());

        // 可以添加 null ，但是是没有任何意义的：
        flist.add(null);
        // 但是知道，最少返回 Fruit 类型：
        Fruit3 f = flist.get(0);
    }
}

/*
可能认为事情开始变得有点走极端了，
因为现在甚至不能往 List<? extends Fruit3> 对象的 List 中放入任何 Fruit3。
 */


/*
【编译器有多聪明】
现在你可能会猜想自己不能去调用任何接受参数的方法，但是考虑下面的代码：
 */
class CompilerIntelligence {
    public static void main(String[] args) {
        List<? extends Fruit3> flist = Arrays.asList(new Apple3());
        System.out.println(flist);
        // flist.add(new Apple3());
        Apple3 a = (Apple3) flist.get(0);
        System.out.println(flist.contains(new Apple3())); // false
        System.out.println(flist.indexOf(new Apple3())); // -1
    }
}
/*
通过查看 ArrayList 的文档，我们发现编译器没有那么聪明：
boolean add(E e);
boolean contains(Object o);
int indexOf(Object o);

当你指定一个 ArrayList<? extends Fruit> 时，add() 的参数就变成了"? extends Fruit"。
从这个描述中，编译器无法得知这里需要 Fruit 的哪个具体子类型，因此它不会接受任何类型的 Fruit。
如果你先把 Apple 向上转型为 Fruit，也没有关系——编译器仅仅会拒绝调用像 add() 这样参数列表中涉及通配符的方法。

contains() 和 indexOf() 的参数类型是 Object，不涉及通配符，所以编译器允许调用它们。

这意味着将由泛型类的设计者来决定哪些调用是“安全的”，并使用 Object 类作为它们的参数类型。
为了禁止对类型中使用了通配符的方法调用，需要在参数列表中使用类型参数。
（这是不是意味着使用通配符来作为类型参数的话，就无法被调用了，编译就会失败？）
 */

/**
 * 看一个简单的 Holder 类：
 */
class Holder<T> {
    private T value;

    public Holder() {}
    public Holder(T val) {
        value = val;
    }

    public void set(T val) {
        value = val;
    }
    public T get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Holder<?> holder = (Holder<?>) o;
        return Objects.equals(value, holder.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public static void main(String[] args) {
        Holder<Apple3> apple = new Holder<>(new Apple3());
        Apple3 d = apple.get();
        apple.set(d);

        // 不能向上转型：
        // Error: 不兼容的类型: Holder<Apple3>无法转换为Holder<Fruit3>
        // Holder<Fruit3> fruit = apple;
        Holder<? extends Fruit3> fruit = apple; /* OK */

        Fruit3 p = fruit.get();
        d = (Apple3) fruit.get();
        try {
            // No warning
            Orange3 c = (Orange3) fruit.get();
        } catch (Exception e) {
            System.out.println(e); // java.lang.ClassCastException: Apple3 cannot be cast to Orange3
        }

        // Error: 不兼容的类型: Apple3无法转换为capture#1, 共 ? extends Fruit3
        // fruit.set(new Apple3());
        // fruit.set(new Fruit3());

        System.out.println(fruit.equals(d)); // false
    }
}
/*
Holder 有一个接受 T 类型对象的 set() 方法，一个返回 T 对象的 get() 方法和一个接受 Object 对象的 equals() 方法。

正如你所见，如果创建了一个 Holder<Apple>，就不能将其向上转型为 Holder<Fruit>，
但是可以向上转型为 Holder<? extends Fruit>。

如果调用 fruit.get()，只能返回一个 Frui
（这就是在给定“任何扩展自 Fruit 的对象”这一边界后，它所能知道的一切了）
如果你知道更多的信息，就可以将其转型到某种具体的 Fruit 而不会导致任何警告，
但是存在得到 ClassCastException 的风险。

fruit.set() 方法不能工作在 Apple 和 Fruit 上，
因为 set() 的参数也是"? extends Fruit"，意味着它可以是任何事物，
编译器无法验证“任何事物”的类型安全性。

但是，equals() 方法可以正常工作，因为它接受的参数是 Object 而不是 T 类型。
因此，编译器只关注传递进来和要返回的对象类型。
它不会分析代码，以查看是否执行了任何实际的写入和读取操作。

除此之外，Java 7 引入了 java.util.Objects 库，
使创建 equals() 和 hashCode() 方法变得更加容易，当然还有很多其他功能。
 */


/*
【逆变】
除了 extends 在泛型中的应用外，
还可以使用超类型通配符。形如：
<? super MyClass> 或 <? super T>
（MyClass 和 T 是下界）
（注意：你不能对泛型参数给出一个超类型边界；即不能声明 <T super MyClass>）
这使得你可以安全地传递一个类型对象到泛型类型中。
 */

/**
 * 有了超类型通配符，就可以向 Collection 写入了：
 */
class SuperTypeWildcards {
    static void writeTo(List<? super Apple3> apples) {
        apples.add(new Apple3());
        apples.add(new Jonathan3());

        // apples.add(new Fruit3()); /* ERROR */
    }
}
/*
参数 apples 是 Apple 的某种基类型的 List（List<? super Apple3>），
这样你就知道向其中添加 Apple 或 Apple 的子类型是安全的。
因为这将使这个 List 敞开口子，从而可以向其中添加非 Apple 类型的对象，而这是违反静态类型安全的。
 */

/**
 * 下面的示例复习了一下逆变和通配符的的使用：
 * 比较 f()1、f2()、f3() 分析方法传参，及可返回的类型
 */
class GenericReading {
    static List<Apple3> apples = Arrays.asList(new Apple3());
    static List<Fruit3> fruit = Arrays.asList(new Fruit3());

    static <T> T readExact(List<T> list) {
        return list.get(0);
    }

    static void f1() {
        Apple3 a = readExact(apples);
        Fruit3 f = readExact(fruit);
        f = readExact(apples);
    }

    static class Reader<T> {
        T readExact(List<T> list) {
            return list.get(0);
        }
    }

    static void f2() {
        Reader<Fruit3> fruit3Reader = new Reader<>();
        Fruit3 f = fruit3Reader.readExact(fruit);
        // Error: 不兼容的类型: java.util.List<Apple3>无法转换为java.util.List<Fruit3>
        // Fruit3 a = fruit3Reader.readExact(apples);
    }

    static class CovariantReader<T> {
        T readCovariant(List<? extends T> list) {
            return list.get(0);
        }
    }

    static void f3() {
        CovariantReader<Fruit3> fruitReader = new CovariantReader<>();
        Fruit3 f = fruitReader.readCovariant(fruit);
        Fruit3 a = fruitReader.readCovariant(apples);
    }

    public static void main(String[] args) {
        f1();
        f2();
        f3();
    }
}



/*
【无界通配符】
无界通配符 <?> 看起来意味着“任何事物”，
因此使用无界通配符好像等价于使用原生类型。
（事实上，编译器初看起来是支持这种判断的）
 */
class UnboundedWildcards1 {
    static List list1;
    static List<?> list2;
    static List<? extends Object> list3;

    static void assign1(List list) {
        list1 = list;
        list2 = list;
        // warning: Unchecked assignment: 'java.util.List' to 'java.util.List<? extends java.lang.Object>'
        list3 = list;
    }
    static void assign2(List<?> list) {
        list1 = list;
        list2 = list;
        list3 = list;
    }
    static void assign3(List<? extends Object> list) {
        list1 = list;
        list2 = list;
        list3 = list;
    }

    public static void main(String[] args) {
        assign1(new ArrayList());
        assign2(new ArrayList());
        assign3(new ArrayList());

        assign1(new ArrayList<>());
        assign2(new ArrayList<>());
        assign3(new ArrayList<>());

        // 这两种形式都可以接受List<?>：
        List<?> wildList = new ArrayList();
        wildList = new ArrayList<>();
        assign1(wildList);
        assign2(wildList);
        assign3(wildList);
    }
}
/*
有很多情况都和你在这里看到的情况类似，即编译器很少关心使用的是原生类型还是 <?> 。
在这些情况中，<?> 可以被认为是一种装饰，但是它仍旧是很有价值的，
因为，实际上它是在声明：
“我是想用 Java 的泛型来编写这段代码，我在这里并不是要用原生类型，
但是在当前这种情况下，泛型参数可以持有任何类型。”
 */

/*
List 实际上表示“持有任何 Object 类型的原生 List”，
而 List<?> 表示“具有某种特定类型的非原生 List ，只是我们不知道类型是什么。”
 */



/**
 * 第二个示例展示了无界通配符的一个重要应用。
 * 当你在处理多个泛型参数时，有时允许一个参数可以是任何类型，
 * 同时为其他参数确定某种特定类型的这种能力会显得很重要：
 */
class UnboundedWildcards2 {
    static Map map1;
    static Map<?, ?> map2;
    static Map<String, ?> map3;

    static void assign1(Map map) {
        map1 = map;
    }
    static void assign2(Map<?, ?> map) {
        map2 = map;
    }
    static void assign3(Map<String, ?> map) {
        map3 = map;
    }

    public static void main(String[] args) {
        assign1(new HashMap());
        assign2(new HashMap());
        assign3(new HashMap());

        assign1(new HashMap<>());
        assign2(new HashMap<>());
        assign3(new HashMap<>());
    }
}


/**
 * 编译器何时才会关注原生类型和涉及无界通配符的类型之间的差异呢？
 * 下面的示例使用了前面定义的 Holder<T> 类，
 * 它包含接受 Holder 作为参数的各种方法，但是它们具有不同的形式：
 * 作为原生类型，具有具体的类型参数以及具有无界通配符参数
 */
class Wildcards {
    static void rawArgs(Holder holder, Object arg) {
        holder.set(arg); // warning
        // 可以这样做，但是类型信息丢失了:
        Object obj = holder.get();
    }
    static void unboundedArg(Holder<?> holder, Object arg) {
        // 这里编译报错：
        // holder.set(arg);
        // 可以这样做，但是类型信息丢失了:
        Object obj = holder.get();

        // 但是编译器可以通过捕获转换获取这个实际类型
        System.out.println(obj.getClass().getSimpleName());
    }

    static <T> T exact1(Holder<T> holder) {
        return holder.get();
    }
    static <T> T exact2(Holder<T> holder, T arg) {
        holder.set(arg);
        return holder.get();
    }

    static <T> T wildSubtype(Holder<? extends T> holder, T arg) {
        // 编译报错：
        // holder.set(arg);
        return holder.get();
    }
    static <T> void wildSupertype(Holder<? super T> holder, T arg) {
        holder.set(arg);
        // 编译报错：
        // T t = holder.get();
        Object obj = holder.get();
    }

    public static void main(String[] args) {
        Holder raw = new Holder<>();
        /* or */
        raw = new Holder();

        Holder<Long> qualified = new Holder<>();
        /* 没有边界的 */
        Holder<?> unbounded = new Holder<>();
        /* 有边界的 */
        Holder<? extends Long> bounded = new Holder<>();

        Long lng = 1L;

        rawArgs(raw, lng);
        rawArgs(qualified, lng);
        rawArgs(unbounded, lng);
        rawArgs(bounded, lng);

        unboundedArg(raw, lng);
        unboundedArg(qualified, lng);
        unboundedArg(unbounded, lng);
        unboundedArg(bounded, lng);

        Object r1 = exact1(raw); // warning
        Long r2 = exact1(qualified);
        Object r3 = exact1(unbounded); // 必须返回 Object，T 被擦除了
        Long r4 = exact1(bounded);

        Long r5 = exact2(raw, lng); // warning
        Long r6 = exact2(qualified, lng);
        // exact2(unbounded, lng); // error
        // exact2(bounded, lng); // error

        Long r9 = wildSubtype(raw, lng); // warning
        Long r10 = wildSubtype(qualified, lng);
        Object r11 = wildSubtype(unbounded, lng); // 必须返回 Object
        Long r12 = wildSubtype(bounded, lng);

        wildSupertype(raw, lng); // warning
        wildSupertype(qualified, lng);
        // wildSupertype(unbounded, lng);  // error
        // wildSupertype(bounded, lng);  // error
    }
}
/*
--------------方法解析-----------------
【void rawArgs(Holder holder, Object arg)】
在这个方法中，编译器知道 Holder 是一个泛型类型，
因此即使它在这里被表示成一个原生类型，编译器仍旧知道向 set() 传递一个 Object 是不安全的。（编译器发出警告）
由于 holder 是原生类型，你可以将任何类型的对象传递给 set() ，而这个对象将被向上转型为 Object 。
因此无论何时，只要使用了原生类型，都会放弃编译期检查。
对 get() 的调用说明了相同的问题：没有任何 T 类型的对象，因此 get() 的结果只能是一个 Object。

【void unboundedArg(Holder<?> holder, Object arg)】
人们很自然地会开始考虑原生 Holder 与 Holder<?> 是大致相同的事物。
但是 unboundedArg() 强调它们是不同的（它揭示了相同的问题），
但是它将这些问题作为错误而不是警告报告，
因为原生 Holder 将持有任何类型的组合，而 Holder<?> 将持有具有某种具体类型的同构集合，
因此不能只是向其中传递 Object 。

【<T> T exact1(Holder<T> holder) 和 <T> T exact2(Holder<T> holder, T arg)】
在 exact1() 和 exact2() 中，你可以看到使用了确切的泛型参数（没有任何通配符）。
你将看到，exact2()与 exact1() 具有不同的限制，因为它有额外的参数。

【<T> T wildSubtype(Holder<? extends T> holder, T arg)】
在 wildSubtype() 中，在 Holder 类型上的限制被放松为包括持有任何扩展自 T 的对象的 Holder 。
这还是意味着如果 T 是 Fruit ，那么 holder 可以是 Holder<Apple> ，这是合法的。
为了防止将 Orange 放置到 Holder<Apple> 中，
对 set() 的调用（或者对任何接受这个类型参数为参数的方法的调用）都是不允许的。
但是，你仍旧知道任何来自 Holder<? extends Fruit> 的对象至少是 Fruit ，
因此 get() （或者任何将产生具有这个类型参数的返回值的方法）都是允许的。

【<T> void wildSupertype(Holder<? super T> holder, T arg)】
wildSupertype() 展示了超类型通配符，这个方法展示了与 wildSubtype() 相反的行为：
holder 可以是持有任何 T 的基类型的容器。
因此， set() 可以接受 T ，因为任何可以工作于基类的对象都可以多态地作用于导出类（这里就是 T ）。
但是，尝试着调用 get() 是没有用的，
因为由 holder 持有的类型可以是任何超类型，因此唯一安全的类型就是 Object 。

在上述例子中，还展示了对于在 Holder<?> unbounded 中（使用无界通配符）能够做什么不能做什么所做出的限制：
因为你没有 T，所以你不能将 set() 或 get() 作用于 T 上。

----------------------------------------------------

为了迁移兼容性，rawArgs() 将接受所有 Holder 的不同变体，而不会产生警告。
unboundedArg() 方法也可以接受相同的所有类型。
尽管如前所述，但它在方法体内部处理这些类型的方式并不相同。

----------------------小结一下------------------------------

如果向接受“确切”泛型类型（没有通配符）的方法传递一个原生 Holder 引用
（比如向 Holder<T> holder 的参数传递原生引用），就会得到一个警告，
因为确切的参数期望得到在原生类型中并不存在的信息。

如果向 exact1() 传递一个无界引用，就不会有任何可以确定返回类型的类型信息。

exact2() 具有最多的限制，因为它希望精确地得到一个 Holder<T> ，
以及一个具有类型 T 的参数，正由于此，它将产生错误或警告，除非提供确切的参数。
有时，这样做很好，但是如果它过于受限，那么就可以使用通配符（如 wildSubtype() 和 wildSupertype()）。
这取决于是否想要从泛型参数中返回类型确定的返回值（就像在 wildSubtype() 中看到的那样），
或者是否想要向泛型参数传递类型确定的参数（就像在 wildSupertype() 中看到的那样）。

因此，使用确切类型来替代通配符类型的好处是，
可以用泛型参数来做更多的事，但是使用通配符使得你必须接受范围更宽的参数化类型作为参数。
因此，必须逐个情况地权衡利弊，找到更适合你的需求的方法。
 */



/*
【捕获转换】
有一种特殊情况需要使用 <?> 而不是原生类型。
如果向一个使用 <?> 的方法传递原生类型，那么对编译器来说，可能会推断出实际的类型参数，
使得这个方法可以回转并调用另一个使用这个确切类型的方法。（在下例 f2() 中调用 f1()）
 * 它被称为 _捕获转换_ ，因为未指定的通配符类型被捕获，并被转换为确切类型。
 */
class CaptureConversion {
    static <T> void f1(Holder<T> holder) {
        T t = holder.get();
        System.out.println("f1: " + t.getClass().getSimpleName());
    }

    static void f2(Holder<?> holder) {
        Object o = holder.get();
        System.out.println("f2: " + o.getClass().getSimpleName());
        f1(holder);
    }

    public static void main(String[] args) {
        Holder raw = new Holder<>(1); /* or new Holder(1); */
        f1(raw);  // warning
        /* Output:
        f1: Integer
         */

        f2(raw);
        /* Output:
        f2: Integer
        f1: Integer
         */

        Holder rawBasic = new Holder();
        rawBasic.set(new Object()); // warning
        f2(rawBasic);
        /* Output:
        f2: Object
        f1: Object
         */

        Holder<?> wildcarded = new Holder<>(1.0);
        f2(wildcarded);
        /* Output:
        f2: Double
        f1: Double
         */
    }
}
/*
f1() 中的类型参数都是确切的，没有通配符或边界。
在 f2() 中，Holder 参数是一个无界通配符，因此它看起来是未知的。
但是，在 f2() 中调用了 f1()，而 f1() 需要一个已知参数。

这里所发生的是：在调用 f2() 的过程中捕获了参数类型，并在调用 f1() 时使用了这种类型。
你可能想知道这项技术是否可以用于写入，但是这要求在传递 Holder<?> 时同时传递一个具体类型。

捕获转换只有在这样的情况下可以工作：即在方法内部，你需要使用确切的类型。
注意，不能从 f2() 中返回 T，因为 T 对于 f2() 来说是未知的。捕获转换十分有趣，但是非常受限。
 */
