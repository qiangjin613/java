package bbb;

import aaa.A;

/*
在派生类 B 中，可以使用基类 A 的 public、protected 的方法与构造器，
但是，却无法使用 new 通过 A 的 protected 创建 A 的对象！
 */
public class B extends A {
    B() {
        // 可以使用基类的 public、protected 的构造器：
        super();
//        super(1);


        // 'A(int, int)' is not public in 'aaa.A'. Cannot be accessed from outside package:
//        new A(1, 2);

        // 在构造器中，可使用 super 使用基类的 public、protected 方法：
        super.publicObjMethod();
        super.protectedObjMethod();
    }

    void method() {
        publicObjMethod();
        protectedObjMethod();

        A.publicClassMethod();
        A.protectedClassMethod();
    }

    public static void main(String[] args) {
        // 0.首先，对于创建对象，貌似在其他类中创建对象，只能调用该类的 public 构造器
        A a = new A();
        // 'A(int, int)' is not public in 'aaa.A'. Cannot be accessed from outside package:
        // new A(1, 2);
        a.publicObjMethod();
        // 无法通过 a 的对象访问 A 的 protected 方法：
        // a.protectedObjMethod();
        // 但是，可以使用基类的 protected 类方法。
        a.protectedClassMethod();

        // 1.可在派生类中通过基类类名调用基类的 public、protected 方法：
        A.publicClassMethod();
        A.protectedClassMethod();

        // 2.可以通过派生类的实例调用基类的 public、protected 方法：
        B b = new B();
        b.publicObjMethod();
        b.protectedObjMethod();
        /* 也可以这样调用基类的类方法，不过显然是“别扭”的... */
        b.publicClassMethod();
        b.protectedClassMethod();
    }
}
/*
protected 的两个不可以：
1. 不可以在派生类中使用 new 通过 protected 构造器创建基类对象。
2. 即使是在派生类中，创建了基类的对象，也无法通过基类对象使用基类的 protected 实例方法。
 */

