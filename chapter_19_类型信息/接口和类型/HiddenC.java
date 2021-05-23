package 接口和类型;

/*
在这个包中唯一 public 的部分就是 HiddenC，
在被调用时将产生 A接口类型的对象。

这里有趣之处在于：
即使你从 makeA() 返回的是 C 类型，
你在包的外部仍旧不能使用 A 之外的任何方法，
因为你不能在包的外部命名 C。
 */
class C implements A {
    @Override
    public void f() {
        System.out.println("public C.f()");
    }
    public void g() {
        System.out.println("public C.g()");
    }
    void u() {
        System.out.println("package C.u()");
    }
    protected void v() {
        System.out.println("protected C.v()");
    }
    private void w() {
        System.out.println("private C.w()");
    }
}

public class HiddenC {
    public static A makeA() {
        return new C();
    }
}
