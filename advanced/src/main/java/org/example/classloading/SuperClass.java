package org.example.classloading;

public class SuperClass {

    static {
        System.out.println("父类初始化");
    }

    public static int value = 1232;
}
