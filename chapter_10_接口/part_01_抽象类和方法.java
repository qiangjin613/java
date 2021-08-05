/*
【引言】
接口和抽象类提供了一种将接口与实现分离的更加结构化的方法。
这种机制在编程语言中不常见，例如 C++ 只对这种概念有间接的支持。
而在 Java 中存在这些关键字，说明这些思想很重要，Java 为它们提供了直接支持。
 *
首先，学习抽象类：一种介于普通类和接口之间的折中手段。
尽管你的第一想法是创建接口，但是对于构建具有属性和未实现方法的类来说，
抽象类也是重要且必要的工具。
因为你不可能总是使用纯粹的接口。
 */

/*
【抽象类和方法】
对于像上一章乐器（Instrument）那样的抽象类来说，它的对象几乎总是没有意义的。
创建一个抽象类是为了通过通用接口操纵一系列类。
因此，Instrument 只是表示接口，不是具体实现，所以创建一个 Instrument 的对象毫无意义，
我们可能希望阻止用户这么做。
通过让 Instrument 所有的方法产生错误，就可以达到这个目的，
但是这么做会延迟到运行时才能得知错误信息，并且需要用户进行可靠、详尽的测试。
最好能在编译时捕捉问题。
 *
为了解决这个麻烦，Java 提供了一个叫做抽象方法的机制，
这个方法是不完整的：它只有声明没有方法体。
 *
包含抽象方法的类叫做抽象类。
如果一个类包含一个或多个抽象方法，
那么类本身也必须限定为抽象的，否则，编译器会报错。
 */

/**
 * 抽象类的示例
 */
abstract class Basic {
    /* 抽象方法不可以有方法体 */
    abstract void unimplemented();
}

class AttemptToUseBasic {
    Basic b;
    // 'Basic' is abstract; cannot be instantiated
    // b = new Basic();
}

/**
 * 继承抽象类，派生类可以为抽象类，也可以不是抽象类
 */
// 派生类是一个抽象类：
abstract class Basic2 extends Basic {
    int f() {
        return 111;
    }

    abstract void g();
}
// 派生类不是一个抽象类：
class Instantiable extends Basic2 {
    @Override
    void unimplemented() {
        System.out.println("");
    }

    @Override
    void g() {
        System.out.println("");
    }

    public static void main(String[] args) {
        Basic2 b = new Instantiable();
        b.f();
        b.g();
        b.unimplemented();
    }
}


/**
 * 可以将一个不包含任何抽象方法的类指明为 abstract，
 * 在类中的抽象方法没啥意义但想阻止创建类的对象时，这么做就很有用。
 */
abstract class Basic3 {
    int f() {
        return 111;
    }
}

class AbstractWithoutAbstracts {
    // 'Basic3' is abstract; cannot be instantiated
    // Basic3 b = new Basic3();
}
/*
虽然不允许创建 abstract Basic3 类，但这并不组织它被继承。
 */
class SubBasic extends Basic3 {
    @Override
    int f() {
        return 1;
    }

    public static void main(String[] args) {
        SubBasic s = new SubBasic();
        s.f();
    }
}


/**
 * 接口只允许 public 方法（如果不加访问修饰符的话，接口的方法不是 friendly 而是 public）；
 * 然而，抽象类允许每一个修饰符：
 */
abstract class AbstractAccess {
    private void m1() {}
    // private 和 abstract 的组合是非法的：
    // private abstract void m1a();

    protected void m2() {}
    protected abstract void m2a();

    void m3() {}
    abstract void m3a();

    public void m4() {}
    public abstract void m4a();
}

/**
 * 【小结】
 * 抽象方法结构刨析：（abstract 就是用来修饰方法的）
 * abstract 在一定程度上声明了“这个东西是要被重写的”与 private、static 本身的含义就是冲突的！
 * 而在抽象类中的 变量、类方法、初始化块、构造器 这些东西，派生类中对于这个压根谈不上重写，所以放在一起也就没有任何意义了。
 *
 * 创建抽象类和抽象方法是有帮助的，因为它们使得类的抽象性很明确，并能告知用户和编译器使用意图。
 * 抽象类同时也是一种有用的重构工具，使用它们使得我们很容易地将沿着继承层级结构上移公共方法。
 */

class V {
    static void f() {
        System.out.println("v.f()");
    }
}
class VV extends V {
    // @Override static 方法是不支持重写的，不支持多态
    static void f() {
        System.out.println("vv.f()");
    }

    public static void main(String[] args) {
        V v = new VV();
        // static 不支持多态
        v.f(); // v.f()
        VV.f(); // vv.f()
        V.f(); // v.f()
    }
}
