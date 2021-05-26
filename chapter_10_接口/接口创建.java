/**
 * 在 Java 8 之前。接口只允许抽象方法。
 *
 * 在这个时期的接口中，都不用为方法加上 abstract 关键字，
 * 因为在这个时期，接口方法只允许抽象方法！
 *
 * 因此，在 Java 8之前我们可以这么说：
 *      interface 关键字产生一个完全抽象的类，没有提供任何实现。
 */
interface PureInterface {
    int m1();
    void m2();
}

/*
Java 8 之后的接口
 */
interface Concept {
    void idea1();
    void idea2();
}

/**
 * 因为默认中的接口方法被隐式指明为 static 和 final，
 * 所以当实现一个接口时，来自接口中的方法必须被定义为 public。
 * （**在继承时，编译器不允许可执行权限的降低**）
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
【默认方法】
Java 8 为关键字 default 增加了一个新的用途（之前只用于 switch 语句和注解中）。
（默认方法比抽象类中的方法受到更多的限制，但是非常有用）
 */
interface InterfaceWithDefault {
    void firstMethod();
    void secondMethod();

    default void defaultMethod() {
        System.out.println("接口的默认方法 newMethod()");
    }
    default void defaultMethod2() {
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
【接口中的静态方法】




 */












