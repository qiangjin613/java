package org.example.classloading;

public class ConstClass {

    static {
        System.out.println("ConstClass 初始化");
    }

    public static final String CONST_HELLOWORD = "hello world!";
}
