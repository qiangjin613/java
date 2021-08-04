/*
有时候我们需要解决的问题很简单，仅仅是“我没有需要的接口”而已。
 *
有两种设计模式用来解决这个问题：
1.适配器模式 接受一种类型并且提供一个对其他类型的接口。
2.外观模式 为一组类创建了一个接口。
 *
这样做只是为了提供一种更方便的方法来处理库或资源。
 */

/*
【适配器模式（Adapter）】
当我们手头有某个类，而我们需要的却是另外一个类，
我们就可以通过 适配器模式 来解决问题。
唯一需要做的就是产生出我们需要的那个类，
有许多种方法可以完成这种适配：
 */
class WhatIHave {
    public void g() {}
    public void h() {}
}

interface WhatIWant {
    void f();
}

class ProxyAdapter implements WhatIWant {
    WhatIHave whatIHave;

    ProxyAdapter(WhatIHave wih) {
        whatIHave = wih;
    }

    @Override
    public void f() {
        whatIHave.g();
        whatIHave.h();
    }
}

// 方法1：
class WhatIUse {
    public void op(WhatIWant wiw) {
        wiw.f();
    }
}
// 方法2：
class WhatIUse2 extends WhatIUse {
    public void op(WhatIHave wih) {
        new ProxyAdapter(wih).f();
    }
}
// 方法3：
class WhatIHave2 extends WhatIHave implements WhatIWant {
    @Override
    public void f() {
        g();
        h();
    }
}
// 方法4：
class WhatIHave3 extends WhatIHave {
    private class InnerAdapter implements WhatIWant {
        @Override
        public void f() {
            g();
            h();
        }
    }
    public WhatIWant whatIWant() {
        return new InnerAdapter();
    }
}

class Adapter {
    public static void main(String[] args) {
        WhatIUse whatIUse = new WhatIUse();
        WhatIHave whatIHave = new WhatIHave();

        // 方法1：
        WhatIWant adapt = new ProxyAdapter(whatIHave);
        whatIUse.op(adapt);

        // 方法2：
        WhatIUse2 whatIUse2 = new WhatIUse2();
        whatIUse2.op(whatIHave);

        // 方法3：
        WhatIHave2 whatIHave2 = new WhatIHave2();
        whatIUse.op(whatIHave2);

        // 方法4：
        WhatIHave3 whatIHave3 = new WhatIHave3();
        whatIUse.op(whatIHave3.whatIWant());
    }
}


/*
【外观模式（Facade）】
当我想方设法试图将需求初步（first-cut）转化成对象的时候，通常我使用的原则是：
    “把所有丑陋的东西都隐藏到对象里去”。
基本上说，外观模式 干的就是这个事情。
如果我们有一堆让人头晕的类以及交互（Interactions），
而它们又不是客户端程序员必须了解的，
那我们就可以为客户端程序员创建一个接口只提供那些必要的功能。

外观模式经常被实现为一个符合单例模式（Singleton）的抽象工厂（abstract factory）。
当然，你可以通过创建包含 静态 工厂方法（static factory methods）的类来达到上述效果。
 */

/**
 * 一个例子：
 */
class A1 {
    A1(int x) {}
}
class B1 {
    B1(long x) {}
}
class C1 {
    C1(double x) {}
}

class Facade {
    static A1 makeA1(int x) {
        return new A1(x);
    }
    static B1 makeB1(long x) {
        return new B1(x);
    }
    static C1 makeC1(double x) {
        return new C1(x);
    }

    public static void main(String[] args) {
        A1 a = Facade.makeA1(1);
        B1 b = Facade.makeB1(1);
        C1 c = Facade.makeC1(1.0);
    }
}


/*
【包（Package）作为外观模式的变体】
我（作者）感觉，外观模式 更倾向于“过程式的（procedural）”，
也就是非面向对象的（non-object-oriented）：我们是通过调用某些函数才得到对象。
 *
它和抽象工厂（Abstract factory）到底有多大差别呢？
<strong> 外观模式 关键的一点是隐藏某个库的一部分类（以及它们的交互），使它们对于客户端程序员不可见，</strong>
这样那些类的接口就更加简练和易于理解了。
 *
其实，这也正是 Java 的 packaging（包）的功能所完成的事情：
在库以外，我们只能创建和使用被声明为公共（public）的那些类；
所有非公共（non-public）的类只能被同一 package 的类使用。
看起来，外观模式 似乎是 Java 内嵌的一个功能。
 *
公平起见，《设计模式》 主要是写给 C++ 读者的。
尽管 C++ 有命名空间（namespaces）机制来防止全局变量和类名称之间的冲突，
但它并没有提供类隐藏的机制，
而在 Java 里我们可以通过声明 non-public 类来实现这一点。
我认为，大多数情况下 Java 的 package 功能就足以解决针对 外观模式 的问题了。
 */
