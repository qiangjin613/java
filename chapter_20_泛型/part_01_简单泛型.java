/*
【前言】
---普通参数的局限性---
普通的类和方法只能使用特定的类型（基本数据类型或类类型）。
如果编写的代码需要应用于多种类型，这种严苛的限制对代码的束缚就会很大。

---使用多态---
多态是一种面向对象思想的泛化机制。你可以将方法的参数类型设为基类，
这样的方法就可以接受任何派生类作为参数，包括暂时还不存在的类。
这样的方法更通用，应用范围更广。
在类内部也是如此，在任何使用特定类型的地方，基类意味着更大的灵活性。
除了 final 类（或只提供私有构造函数的类）任何类型都可被扩展，所以大部分时候这种灵活性是自带的。

---使用接口---
拘泥于单一的继承体系太过局限，因为只有继承体系中的对象才能适用基类作为参数的方法中。
如果方法以接口而不是类作为参数，限制就宽松多了，只要实现了接口就可以。
这给予调用方一种选项，通过调整现有的类来实现接口，满足方法参数要求。
接口可以突破继承体系的限制。

---引出泛型---
即便是接口也还是有诸多限制。一旦指定了接口，它就要求你的代码必须使用特定的接口。
而我们希望编写更通用的代码，能够适用“非特定的类型”，而不是一个具体的接口或类。

这就是泛型的概念，是 Java 5 的重大变化之一。
泛型实现了参数化类型，这样你编写的组件（通常是集合）可以适用于多种类型。
“泛型”这个术语的含义是“适用于很多类型”。
编程语言中泛型出现的初衷是通过解耦类或方法与所使用的类型之间的约束，使得类或方法具备最宽泛的表达力。
（随后你会发现 Java 中泛型的实现并没有那么“泛”，你可能会质疑“泛型”这个词是否合适用来描述这一功能）

如果你从未接触过参数化类型机制，你会发现泛型对 Java 语言确实是个很有益的补充。
在你实例化一个类型参数时，编译器会负责转型并确保类型的正确性。这是一大进步。
（泛型，工作在编译期）

然而，如果你了解其他语言（例如 C++ ）的参数化机制，你会发现，Java 泛型并不能满足所有的预期。
使用别人创建好的泛型相对容易，但是创建自己的泛型时，就会遇到很多意料之外的麻烦。

这并不是说 Java 泛型毫无用处。
在很多情况下，它可以使代码更直接更优雅。
不过，如果你见识过那种实现了更纯粹的泛型的编程语言，那么，Java 可能会令你失望。
本章会介绍 Java 泛型的优点与局限。
会解释 Java 的泛型是如何发展成现在这样的，希望能够帮助你更有效地使用这个特性。

【与 C++ 的比较】
Java 的设计者曾说过，这门语言的灵感主要来自 C++ 。尽管如此，学习 Java 时基本不用参考 C++ 。

但是，Java 中的泛型需要与 C++ 进行对比，理由有两个：
    首先，理解 C++ 模板（泛型的主要灵感来源，包括基本语法）的某些特性，有助于理解泛型的基础理念。
    同时，非常重要的一点是，你可以了解 Java 泛型的局限是什么，以及为什么会有这些局限。
    最终的目标是明确 Java 泛型的边界，让你成为一个程序高手。
    只有知道了某个技术不能做什么，你才能更好地做到所能做的（部分原因是，不必浪费时间在死胡同里）。

    其次，在 Java 社区中，大家普遍对 C++ 模板有一种误解，而这种误解可能会令你在理解泛型的意图时产生偏差。

因此，本章中会介绍少量 C++ 模板的例子，仅当它们确实可以加深理解时才会引入。
 */


/*
【简单泛型】
促成泛型出现的最主要的动机之一是为了创建集合类。
集合用于存放要使用到的对象。数组也是如此，不过集合比数组更加灵活，功能更丰富。
几乎所有程序在运行过程中都会涉及到一组对象，
因此集合是可复用性最高的类库之一。
 */

import java.util.*;
import java.util.stream.IntStream;

/**
 * 先看一个只能持有单个对象的类：
 */
class Automobile {}
class Holder1 {
    private Automobile a;
    public Holder1(Automobile a) {
        this.a = a;
    }
    Automobile getA() {
        return a;
    }
}
/*
这个类的可复用性不高，因为它无法持有其他类型的对象。
在 Java 5 之前，可以使用 多态 进行处理。
 */
class ObjectHolder {
    private Object a;
    public ObjectHolder(Object a) {
        this.a = a;
    }
    public void setA(Object a) {
        this.a = a;
    }
    public Object getA() {
        return a;
    }

    public static void main(String[] args) {
        ObjectHolder oh = new ObjectHolder(new Automobile());
        Automobile a = (Automobile) oh.getA();
        System.out.println(a.getClass().getSimpleName());

        oh.setA("来一个String");
        String aStr = (String) oh.getA();
        System.out.println(aStr.getClass().getSimpleName());
    }
}
/*
一个集合中存储多种不同类型的对象的情况很少见，通常而言，我们只会用集合存储同一种类型的对象。
泛型的主要目的之一就是用来约定集合要存储什么类型的对象，并且通过编译器确保规约得以满足。
因此，与其使用 Object ，我们更希望先指定一个类型占位符，稍后再决定具体使用什么类型。
下面，使用泛型进行进一步优化：
 */
class GenericHolder<T> {
    private T a;
    public GenericHolder(T a) {
        this.a = a;
    }
    public void setA(T a) {
        this.a = a;
    }
    public T getA() {
        return a;
    }

    public static void main(String[] args) {
        GenericHolder<Automobile> gh = new GenericHolder<Automobile>(new Automobile());
        /* 这里不需要类型转换 */
        Automobile a = gh.getA();
        System.out.println(a.getClass().getSimpleName());

        /* 编译报错 */
        // Error:(128, 17) java: 不兼容的类型: java.lang.String无法转换为Automobile
        // gh.setA("");
    }
}
/*
创建对象时，使用 <> 进行指定持有对象的类型，就使用了泛型机制。
当调用 getA() 时，直接就是正确的类型。

这就是 Java 泛型的核心概念：你只需告诉编译器要使用什么类型，剩下的细节交给它来处理。

附上 GenericHolder.class 文件：
    public static void main(String[] args) {
            GenericHolder<Automobile> gh = new GenericHolder(new Automobile());
            Automobile a = (Automobile)gh.getA();
            System.out.println(a.getClass().getSimpleName());
    }
 */


/*
【菱形语法 | “钻石语法”】
在上述 GenericHolder 的定义中，<> 需要使用两次，非常繁复。
这在 Java 5 中是“必要的”，但在 Java 7 中修正了这个问题。
 */
class Diamond<T> {
    public static void main(String[] args) {
        GenericHolder<Automobile> gh = new GenericHolder<>(new Automobile());
    }
}


/*
【小结】
一般来说，你可以认为泛型和其他类型差不多，只不过它们碰巧有类型参数罢了。
在使用泛型时，你只需要指定它们的名称和类型参数列表即可。
 */


/*
【一个元组类库】
有时一个方法需要能返回多个对象。而 return 语句只能返回单个对象，
解决方法就是创建一个对象，用它打包想要返回的多个对象。
当然，可以在每次需要的时候，专门创建一个类来完成这样的工作。

但是有了泛型，我们就可以一劳永逸。同时，还获得了编译时的类型安全。
这个概念称为元组，它是将一组对象直接打包存储于单一对象中。
可以从该对象读取其中的元素，但不允许向其中存储新对象（这个概念也称为 数据传输对象 或 信使 ）。

通常，元组可以具有任意长度，元组中的对象可以是不同类型的。
不过，我们希望能够为每个对象指明类型，并且从元组中读取出来时，能够得到正确的类型。
要处理不同长度的问题，我们需要创建多个不同的元组。
 */

/**
 * 下面提供一个存储两个对象的元组：
 */
class Tuple2<A, B> {
    public final A a;
    public final B b;
    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }
    public String rep() {
        return a + ", " + b;
    }

    @Override
    public String toString() {
        return "(" + rep() + ")";
    }
}
/*
构造函数传入要存储的对象。这个元组隐式地保持了其中元素的次序。

Q：上面的代码有没有违反 Java 编程的封装原则？
A：NO。
    在之前的封装中，a1 和 a2 应该声明为 private，然后提供 getFirst() 和 getSecond() 取值方法。
    考虑这样提供的“安全性”是什么：
        元组的使用程序可以读取 a1 和 a2 然后对它们执行任何操作，
        但无法对 a1 和 a2 重新赋值。
    在上例中的 final 可以实现同样的效果，并且更加简洁明了。
另一种涉及思路是允许元组用户给 a 和 b 重新赋值。
（然而，上例中的形式无疑更加安全，如果用户想存储不同的元素，就会强制他们创建新的 Tuple2 对象。）
 */

/**
 * 使用继承机制实现更长的元组：
 */
class Tuple3<A, B, C> extends Tuple2<A, B> {
    public final C c;
    public Tuple3(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }
    @Override
    public String rep() {
        return super.rep() + ", " + c;
    }
}

/**
 * 演示元组用法：
 */
class Amphibian {}
class Vehicle {}

class TupleTest {
    static Tuple2<String, Integer> f() {
        return new Tuple2<>("hi", 47);
    }

    public static void main(String[] args) {
        Tuple2<String, Integer> tsi = f();
        System.out.println(tsi);
        /* final 的 a 不能重新赋值 */
        // tsi.a = "";
    }
}
/*
有了泛型，你可以很容易地创建元组，令其返回一组任意类型的对象。
 */



/**
 * 【一个堆栈类】
 * 使用泛型实现一个链栈：
 */
class LinkedStack<T> {
    // 元素节点：
    private static class Node<U> {
        U item;
        Node<U> next;

        Node() {
            item = null;
            next = null;
        }

        // 也作为入栈操作：
        Node(U item, Node<U> next) {
            this.item = item;
            this.next = next;
        }

        /* 末端标识 */
        boolean end() {
            return item == null && next == null;
        }
    }

    // 栈顶：
    private Node<T> top = new Node<>();

    // 入栈：
    public void push(T item) {
        top = new Node<>(item, top);
    }

    // 出栈：
    public T pop() {
        T result = top.item;
        if (!top.end()) {
            top = top.next;
        }
        return result;
    }

    // test:
    public static void main(String[] args) {
        LinkedStack<String> ls = new LinkedStack<>();
        for (String s : "Hello LinkedStack!".split(" ")) {
            ls.push(s);
        }
        String s;
        /* 有意思的语法 */
        while ((s = ls.pop()) != null) {
            System.out.println(s);
        }
    }
}
/*
内部类 Node 也是一个泛型，它拥有自己的类型参数。
 */


/**
 * 再一个作为容器的例子：
 * RandomList，
 * 假设我们需要一个持有特定类型对象的列表，每次调用它的 select() 方法时都随机返回一个元素。
 * 如果希望这种列表可以适用于各种类型，就需要使用泛型：
 */
class RandomList<T> extends ArrayList<T> {
    private Random rand = new Random(47);

    public T select() {
        return get(rand.nextInt(size()));
    }

    public static void main(String[] args) {
        RandomList<String> rl = new RandomList<>();
        rl.addAll(Arrays.asList("I am Iron Man".split(" ")));
        IntStream.range(0, 11).forEach(i -> System.out.println(i + ":\t" + rl.select()));
    }
}
