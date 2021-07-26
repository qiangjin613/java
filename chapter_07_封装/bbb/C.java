package bbb;

import aaa.A;

public class C {
    C() {

    }

    public static void main(String[] args) {
        // 0.首先，对于创建对象，貌似在其他类中创建对象，只能调用该类的 public 构造器
        new A();
        // 'A(int, int)' is not public in 'aaa.A'. Cannot be accessed from outside package:
        // new A(1, 2);


        // 1.可在派生类中通过基类类名调用基类的 public 方法：
        A.publicClassMethod();

        // 2.可以通过派生类的实例调用基类的 public、protected 方法：
        A a = new A();
        a.publicObjMethod();
        /* 也可以这样调用基类的类方法，不过显然是“别扭”的... */
        a.publicClassMethod();
    }
}
