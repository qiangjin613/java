/**
 * 在 Java 8 之前。接口只允许抽象方法（都是 public abstract 修饰的）
 *
 * 在这个时期的接口中，都不用为方法加上 public、abstract 关键字，
 * 因为在这个时期，接口方法只允许抽象方法！
 *
 * 因此，在 Java 8之前我们可以这么说：
 *      interface 关键字产生一个完全抽象的类，没有提供任何实现。
 */
interface PureInterface {
    // 接口同样可以包含属性，这些属性被隐式指明为 static 和 final。
    public static final int i = 1;

    // 默认方法修饰符就是 public abstract 的
    int m1();
    public abstract void m2();
}

/*
Java 8 之后的接口（开始允许接口包含默认方法和静态方法）
 *
接口的基本概念仍然没变，介于类型之上、实现之下。
接口与抽象类最明显的区别可能就是使用上的惯用方式。
接口的典型使用是代表一个类的类型或一个形容词，如 Runnable 或 Serializable；
而抽象类通常是类层次结构的一部分或一件事物的类型。
 */
interface Concept {
    void idea1();
    void idea2();
}
/*
使用 implements 关键字使一个类遵循某个特定接口（或一组接口），
它表示：
    接口只是外形，现在我要说明它是如何工作的。
除此之外，它看起来像继承。
 */
class ImplementationC implements Concept {
    @Override
    public void idea1() {
        System.out.println("实现类的 idea1()");
    }

    @Override
    public void idea2() {
        System.out.println("实现类的 idea2()");
    }
}
/*
因为默认中的接口方法被隐式指明为 pulic abstract，
所以当实现一个接口时，来自接口中的方法必须被定义为 public。
（**在继承时，编译器不允许可执行权限的降低**）
 */


/*
【默认方法】
Java 8 为关键字 default 增加了一个新的用途（之前只用于 switch 语句和注解中）。
（默认方法也默认为 public 的，也只能为 public）
（默认方法比抽象类中的方法受到更多的限制，但是非常有用）
 */
interface InterfaceWithDefault {
    void firstMethod();
    void secondMethod();

    default void defaultMethod() {
        System.out.println("接口的默认方法 newMethod()");
    }
    public default void defaultMethod2() {
        /* 使用了多态，调用实现类的 secondMethod() */
        secondMethod();
        System.out.println("接口的默认方法 newMethod()2");
    }
}

class Implementation2 implements InterfaceWithDefault {
    @Override
    public void firstMethod() {
        System.out.println("实现类的方法 firstMethod()");
    }

    @Override
    public void secondMethod() {
        System.out.println("实现类的方法 secondMethod()");
    }

    public static void main(String[] args) {
        Implementation2 i = new Implementation2();
        i.firstMethod();
        i.secondMethod();
        i.defaultMethod();
        i.defaultMethod2();
    }
}

/*
增加默认方法的极具说服力的理由是：
    它允许在不破坏已使用接口的代码的情况下，在接口中增加新的方法。
    （增加默认方法，对于其实现类而言，不需要额外的工作）
默认方法有时也被称为守卫方法或虚拟扩展方法。
 */


/*
【多继承】
多继承意味着一个类可能从多个父类型中继承特征和特性。

Java 在设计之初，C++ 的多继承机制饱受诟病。
Java 过去是一种严格要求单继承的语言：只能继承自一个类（或抽象类），但可以实现任意多个接口。
在 Java 8 之前，接口没有包袱，它只是方法外貌的描述，没有具体的功能实现。

但在 Java 8 后，Java 通过默认方法具有了某种多继承的特性。
结合带有默认方法的接口意味着结合了多个基类中的行为。（具有了某种多继承的特性）
因为接口中仍然不允许存在属性（只有静态属性，不适用），所以属性仍然只会来自单个基类或抽象类，
也就是说，虽然放宽了多继承的限制，但依旧不会存在状态的（真正）多继承。
 */
interface One {
    default void first() {
        System.out.println("One 的默认方法 first()");
    }
}
interface Two {
    default void second() {
        System.out.println("Two 的默认方法 second()");
    }
}
interface Three {
    default void third() {
        System.out.println("Three 的默认方法 third()");
    }
}
class MI implements One, Two, Three {}

class MultipleInheritance {
    public static void main(String[] args) {
        MI m = new MI();
        m.first();
        m.second();
        m.third();
    }
}


/*
现在，做些在 Java 8 之前不可能完成的事：结合多个源的实现。

只要基类方法中的方法名和参数列表不同，就能工作得很好。

但在两个接口中有相同的方法签名，就会得到编译器错误。
 */
interface Bob1 {
    default void bob() {
        System.out.println("Bob1 的 bob()");
    }
}
interface Bob2 {
    default void bob() {
        System.out.println("Bob2 的 bob()");
    }
}
/* 报错，因为 Bob1、Bob2 的 bob() 冲突了 */
// class Bob implements Bob1, Bob2 {}

interface Sam1 {
    default void sam() {
        System.out.println("Sam1 的 sam()");
    }
}
interface Sam2 {
    default void sam(int i) {
        System.out.println("Sam2 的 sam(int i)");
    }
}
/* 因为两个接口的方法签名不一样，所以不会有什么问题 */
class Sam implements Sam1, Sam2 {}

interface Max1 {
    default void max() {
        System.out.println("Max1 的 max()");
    }
}
interface Max2 {
    default int max() {
        return 47;
    }
}
/* 尽管 Max1、Max2 的 max() 的返回值不同，
但两个接口的方法签名一样（返回值不是签名的一部分），
编译器就认为这是同一个方法 */
// class Max implements Max1, Max2 {}

/*
为了解决这个问题，就需要在实现类中覆写这个冲突的方法。
 */
interface Jim1 {
    default void jim() {
        System.out.println("Jim1 的 jim()");
    }
}
interface Jim2 {
    default void jim() {
        System.out.println("Jim2 的 jim()");
    }
}
class Jim implements Jim1, Jim2 {
    @Override
    public void jim() {
        // 可以使用 super 关键字显示调用确定接口的确定的方法
        Jim2.super.jim();
    }

    public static void main(String[] args) {
        new Jim().jim();
    }
}
/*
在上述”冲突“中，jim() 方法虽然产生了冲突，但好在其返回值是相同的，
要是冲突方法的返回值不同，就难办了！（现在没找到解决方法）
 */



/*
【接口中的静态方法】
Java 8 允许在接口中添加静态方法。
能恰当地把工具功能置于接口中，从而操作接口，或者成为通用的工具。
 */
interface Operations {
    void execute();

    static void runOps(Operations... ops) {
        for (Operations op : ops) {
            /* （多态调用）实现类中被重写具体方法的 execute() */
            op.execute();
        }
    }

    static void show(String msg) {
        System.out.println(msg);
    }
}
/*
上述例子是模板方法的一个版本，runOps() 是一个模板方法。
runOps() 使用可变参数列表，
因而我们可以传入任意多的 Operation 参数并按顺序运行它们。

使用如下：
 */
class Bing implements Operations {
    @Override
    public void execute() {
        Operations.show("Bing");
    }
}
class Crack implements Operations {
    @Override
    public void execute() {
        Operations.show("Crack");
    }
}
class Machine {
    public static void main(String[] args) {
        Operations.runOps(new Bing(), new Crack());
    }
}
/*
（接口中允许静态方法）这个特性是一项改善，因为它允许把静态方法放在更合适的地方。
 */



/*
【Instrument 作为接口】
对乐器的例子使用接口进行改造
 */
interface Instrument3 {
    /* 静态常量|编译时常量：（默认是 static final 的） */
    int VALUE = 5;
    default void play(Note n) {
        System.out.println(this + ".play() " + n);
    }
    default void adjust() {
        System.out.println("Adjusting " + this);
    }
}
class Wind3 implements Instrument3 {
    @Override
    public String toString() {
        return "Wind3";
    }
}
class Stringed3 implements Instrument3 {
    @Override
    public String toString() {
        return "Stringed3";
    }
}
class Brass3 extends Wind3 {
    @Override
    public String toString() {
        return "Brass3";
    }
}
class Music3 {
    static void tune(Instrument3 i) {
        i.play(Note.MIDDLE_C);
    }
    static void tuneAll(Instrument3[] e) {
        for (Instrument3 i : e) {
            tune(i);
        }
    }

    public static void main(String[] args) {
        Instrument3[] orchestra = {new Wind3(), new Brass3(), new Stringed3()};
        tuneAll(orchestra);
    }
}
/*
在这个例子中，
无论是将其向上转型为称作 Instrument 的普通类，
或称作 Instrument 的抽象类，
还是叫作 Instrument 的接口，
其行为都是相同的。

事实上，
从 tune() 方法上看不出来 Instrument 到底是一个普通类、抽象类，
还是一个接口。
 */
