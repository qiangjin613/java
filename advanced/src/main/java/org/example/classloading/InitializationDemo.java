package org.example.classloading;

/**
 * 演示类 “被动引用”
 */
public class InitializationDemo {

    public static void main(String[] args) {
//        demo1();
//        demo2();
        demo3();
    }

    /**
     * 通过子类引用父类的静态字段，不会导致子类初始化
     */
    public static void demo1() {
        System.out.println(SubClass.value);
    }

    /**
     * 通过数组来引用类，不会触发此类的初始化
     */
    public static void demo2() {
        SuperClass[] sca = new SuperClass[10];
    }

    /**
     * 虽然在源码中引用了 ConstClass 的常量，但其实在编译阶段通过常量传播优化，已经将此常量的值直接存储在 InitializationDemo 的常量池中，
     * 之后的常量引用，实际都被转化为对自身常量池的引用了。
     */
    public static void demo3() {
        System.out.println(ConstClass.CONST_HELLOWORD);
    }
}
