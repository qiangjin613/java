/*
【面向实现】
代理模式和桥接模式都提供了在代码中使用的代理类;
完成工作的真正类隐藏在这个代理类的后面。
当您在代理中调用一个方法时，它只是反过来调用实现类中的方法。

这两种模式非常相似，所以代理模式只是桥接模式的一种特殊情况。
人们倾向于将两者合并，称为代理模式，
但是术语“代理”有一个长期的和专门的含义，这可能解释了这两种模式不同的原因。

代理的基本思想很简单：
从基类派生代理，同时派生一个或多个提供实现的类：
创建代理对象时，给它一个可以调用实际工作类的方法的实现。


在结构上，代理模式和桥接模式的区别很简单：
代理模式只有一个实现，而桥接模式有多个实现。

在设计模式中被认为是不同的：
代理模式用于控制对其实现的访问，而桥接模式允许您动态更改实现。
但是，如果您扩展了“控制对实现的访问”的概念，那么这两者就可以完美地结合在一起。
 */

/**
 * 【代理模式】
 */
interface ProxyBase {
    void f();
    void g();
    void h();
}

class Proxy implements ProxyBase {

    private ProxyBase implementation;

    Proxy(ProxyBase implementation) {
        this.implementation = implementation;
    }

    @Override
    public void f() {
        implementation.f();
    }
    @Override
    public void g() {
        implementation.g();
    }
    @Override
    public void h() {
        implementation.h();
    }
}

class Implementation3 implements ProxyBase {
    @Override
    public void f() {
        System.out.println("Implementation.f()");
    }
    @Override
    public void g() {
        System.out.println("Implementation.g()");
    }
    @Override
    public void h() {
        System.out.println("Implementation.h()");
    }
}

class ProxyDemo {
    public static void main(String[] args) {
        Proxy p = new Proxy(new Implementation3());
        p.f();
        p.g();
        p.h();
    }
}
/*
具体实现不需要与代理对象具有相同的接口;
只要代理对象以某种方式代表具体实现的方法调用，那么基本思想就算实现了。
然而，拥有一个公共接口是很方便的，因此具体实现必须实现代理对象调用的所有方法。
 */



/**
 * 【状态模式】
 * 状态模式向代理对象添加了更多的实现，
 * 以及在代理对象的生命周期内从一个实现切换到另一种实现的方法:
 */
interface StateBase {
    void f();
    void g();
    void h();
    void changeImp(StateBase newImp);
}

class State implements StateBase {

    private StateBase implementation;

    State(StateBase imp) {
        implementation = imp;
    }

    @Override
    public void changeImp(StateBase newImp) {
        implementation = newImp;
    }

    @Override
    public void f() {
        implementation.f();
    }
    @Override
    public void g() {
        implementation.g();
    }
    @Override
    public void h() {
        implementation.h();
    }
}

class Implementation4 implements StateBase {
    @Override
    public void f() {
        System.out.println("Implementation4.f()");
    }
    @Override
    public void g() {
        System.out.println("Implementation4.g()");
    }
    @Override
    public void h() {
        System.out.println("Implementation4.h()");
    }
    @Override
    public void changeImp(StateBase newImp) {}
}
class Implementation5 implements StateBase {
    @Override
    public void f() {
        System.out.println("Implementation5.f()");
    }
    @Override
    public void g() {
        System.out.println("Implementation5.g()");
    }
    @Override
    public void h() {
        System.out.println("Implementation5.h()");
    }
    @Override
    public void changeImp(StateBase newImp) {}
}

class StateDemo {
    static void test(StateBase b) {
        b.f();
        b.g();
        b.h();
    }

    public static void main(String[] args) {
        StateBase b = new State(new Implementation4());
        test(b);
        b.changeImp(new Implementation5());
        test(b);
    }
}
/*
在main()中，首先使用第一个实现，然后改变成第二个实现。

代理模式和状态模式的区别在于它们解决的问题。

设计模式中描述的代理模式的常见用途如下：
1. 远程代理
    它在不同的地址空间中代理对象。远程方法调用(RMI)编译器rmic会自动为您创建一个远程代理。
2. 虚拟代理
    这提供了“懒加载”来根据需要创建“昂贵”的对象。
3. 保护代理
    当您希望对代理对象有权限访问控制时使用。
4. 智能引用
    要在被代理的对象被访问时添加其他操作。
    例如，跟踪特定对象的引用数量，来实现写时复制用法，和防止对象别名。
    一个更简单的例子是跟踪特定方法的调用数量。
    您可以将Java引用视为一种保护代理，因为它控制在堆上实例对象的访问(例如，确保不使用空引用)。
 */

/*
在设计模式中，代理模式和桥接模式并不是相互关联的，因为它们被赋予(我认为是任意的)不同的结构。
桥接模式，特别是使用一个单独的实现，但这似乎对我来说是不必要的，
除非你确定该实现是你无法控制的(当然有可能，但是如果您编写所有代码，那么没有理由不从单基类的优雅中受益)。
此外，只要代理对象控制对其“前置”对象的访问，代模式理就不需要为其实现使用相同的基类。
不管具体情况如何，在代理模式和桥接模式中，代理对象都将方法调用传递给具体实现对象。
 */


/*
【状态机】
桥接模式允许程序员更改实现，状态机利用一个结构来自动地将实现更改到下一个。
当前实现表示系统所处的状态，系统在不同状态下的行为不同(因为它使用桥接模式)。
基本上，这是一个利用对象的“状态机”。
将系统从一种状态移动到另一种状态的代码通常是模板方法模式，如下例所示:
 */
interface State2 {
    void run();
}

abstract class StateMachine2 {
    protected State2 currentState;

    protected abstract boolean changeState();

    // 模板方法：
    protected final void runAll() {
        while (changeState()) {
            currentState.run();
        }
    }
}

class Wash2 implements State2 {
    @Override
    public void run() {
        System.out.println("Washing");
    }
}
class Spin2 implements State2 {
    @Override
    public void run() {
        System.out.println("Spinning");
    }
}
class Rinse2 implements State2 {
    @Override
    public void run() {
        System.out.println("Rinsing");
    }
}

class Washer2 extends StateMachine2 {
    private int i = 0;
    // 状态列表：
    private State2[] states = {
            new Wash2(),
            new Spin2(),
            new Rinse2(),
            new Spin2()};

    Washer2() {
        runAll();
    }

    @Override
    protected boolean changeState() {
        if (i < states.length) {
            currentState = states[i++];
            return true;
        } else {
            return false;
        }
    }
}

class StateMachineDemo {
    public static void main(String[] args) {
        new Washer2();
    }
}
/*
在这里，控制状态的类(本例中是状态机)负责决定下一个状态。
然而，状态对象本身也可以决定下一步移动到什么状态，通常基于系统的某种输入。
这是更灵活的解决方案。
 */
