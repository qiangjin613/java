package bbb;

import aaa.A;

public class B extends A {
    B() {
        // 可以使用父类的构造器
        super(1);

        // 使用父类的方法
        super.f();
        super.f(1);
    }

    public static void main(String[] args) {
        // 这样是不允许的
        // new A(1);

        // 使用父类的方法
        new B().f();
        new B().f(1);
        // 不允许
        // new B().f(1, 2);
    }
}
