package aaa;

public class A {
    // 构造器测试
    public A() {}
    protected A(int a) {}
    A(int a, int b) {}
    private A(int a, int b, int c) {}

    // 方法测试
    public void publicObjMethod() {}
    protected void protectedObjMethod() {}
    void defaultObjMethod() {}
    private void privateObjMethod() {}

    public static void publicClassMethod() {}
    protected static void protectedClassMethod() {}
    static void defaultClassMethod() {}
    private void privateClassMethod() {}
}
