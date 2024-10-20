package org.example.classloading;

public class SubClass extends SuperClass {

    static {
        System.out.println("子类初始化");
    }
}
