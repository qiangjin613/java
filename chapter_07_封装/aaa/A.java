package aaa;

public class A {
    // 构造器测试
    public A() {
        System.out.println("public A()");
    }

    protected A(int a) {
        System.out.println("protected A(int a)");
    }


    A(int a, int b) {
        System.out.println("A(int a, int b)");
    }

    private A(int a, int b, int c) {
        System.out.println("private A(int a, int b, int c)");
    }

    public static A makeA3Args() {
        return new A(2, 3, 4);
    }


    // 方法测试
    public void f() {
        System.out.println("public void f()");
    }
    protected void f(int a) {
        System.out.println("protected void f(int a)");
    }
    void f(int a, int b) {
        System.out.println("void f(int a, int b)");
    }
    private void f(int a, int b, int c) {
        System.out.println("private void f(int a, int b, int c)");
    }
}
