import 接口和类型.A;

/*

 */
class B implements A {
    @Override
    public void f() {}
    public void g() {}
}
class InterfaceViolation {
    public static void main(String[] args) {
        A a = new B();
        a.f();
        System.out.println(a.getClass().getName()); // output: B
        /*
        虽然对象 a 没有 g() 方法，但是我们可以通过向下转型获得 B 对象，从而调用 g()
         */
        if (a instanceof B) {
            B b = (B) a;
            b.g();
        }
    }
}
/*
在上述示例中，通过 RTTI 发现 a 是用 B 实现的。
通过将其转型为 B，我们可以调用不在 A 中的方法。

这样的操作完全是合情合理的，但是你也许并不想让客户端开发者这么做，
因为这给了他们一个机会，使得他们的代码与你的代码的耦合度超过了你的预期。
 */

/*
对于接口耦合度的解决方案，一种是直接声明。
一种是用一种更严格的控制方式：让实现类只具有包访问权限，
这样在包外部的客户端就看不到它了。
如：使用 “package 接口和类型” 中的 “HiddenC” 类

如果你试着将其向下转型为 C，则将被禁止，因为在包的外部没有任何 C 类型可用
如下：
 */



