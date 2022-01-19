package test;

public interface PureInterface {

    int i = 1;
    public static int i2 = 3;
    public final static int i3 = 4;

//    int i = m1();

    int m1();
    abstract int m2();
    public int m3();
    public abstract int m4();
}

interface InterfaceWithDefault {
    default int defaultMethod() {
        System.out.println("Java 8 之后的 default 方法");
        return 1;
    }
}

interface A {
    default void hello() {
        System.out.println("Hello A");
    }
}
interface B {
    default void hello() {
        System.out.println("Hello A");
    }
}
class Q implements A, B {
    @Override
    public void hello() {
        A.super.hello();
    }
}