import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/*
至此，我们已经看到了许多描述内部类的语法和语义，
但是这并不能同答“为什么需要内部类”这个问题。
 *
那么，Java 设计者们为什么会如此费心地增加这项基本的语言特性呢？
一般说来，内部类继承自某个类或实现某个接口，
内部类的代码操作创建它的外围类的对象。
所以可以认为内部类提供了某种进入其外围类的窗口。
 *
内部类必须要回答的一个问题是：
如果只是需要一个对接口的引用，为什么不通过外围类实现那个接口呢？
答案是：“如果这能满足需求，那么就应该这样做。”
 *
那么内部类实现一个接口与外围类实现这个接口有什么区别呢？
答案是：后者不是总能享用到接口带来的方便，有时需要用到接口的实现。
所以，使用内部类最吸引人的原因是：
每个内部类都能独立地继承自一个（接口的）实现，
所以无论外围类是否已经继承了某个（接口的）实现，对于内部类都没有影响。
 *
如果没有内部类提供的、可以继承多个具体的或抽象的类的能力，
一些设计与编程问题就很难解决。
从这个角度看，内部类使得多重继承的解决方案变得完整。
接口解决了部分问题，而内部类有效地实现了“多重继承”。
也就是说，内部类允许继承多个非接口类型。
 */

/*
为了看到更多的细节，让我们考虑这样一种情形：
必须在一个类中以某种方式实现两个接口的例子：（内部类 -- 多继承）
（由于接口的灵活性，你有两种选择；使用单一类，或者使用内部类）
 */
interface A2 {}
interface B2 {}
class X implements A2, B2 {}
class Y implements A2 {
    B2 makeB2() {
        /* 匿名内部类 */
        return new B2() {};
    }
}
class MultiInterfaces {
    static void takesA2(A2 a) {}
    static void takesB2(B2 b) {}

    public static void main(String[] args) {
        X x = new X();
        Y y = new Y();
        takesA2(x);
        takesA2(y);

        takesB2(x);
        takesB2(y.makeB2());
    }
}

/*
如果拥有的是抽象的类或具体的类，而不是接口，
那就只能使用内部类才能实现多重继承。
 */
class D2 {}
abstract class E2 {}
class Z2 extends D2 {
    E2 makeE2() {
        /* 匿名内部类 */
        return new E2() {};
    }
}
class MultiImplementation {
    static void takesD2(D2 d) {}
    static void takesE2(E2 e) {}

    public static void main(String[] args) {
        Z2 z = new Z2();
        takesD2(z);
        takesE2(z.makeE2());
    }
}

/*
如果不需要解决“多重继承”的问题，那么自然可以用别的方式编码，而不需要使用内部类。
如果使用内部类，还可以获得其他一些特性：
1.内部类可以有多个实例，每个实例都有自己的状态信息，并且与其外部类对象的信息相互独立。
2.在单个外部类中，可以让多个内部类以不同的方式实现同一个接口，或继承同一个类。（迭代器、反向迭代器的例子）
3.创建内部类对象的时刻并不依赖于外部类对象的创建
4.内部类并没有令人迷惑的"is-a”关系，它就是一个独立的实体。

比如【链接外部类中的 Sequence 类】
如果 Sequence 不使用内部类，就必须声明"Sequence 是一个 Selector"，
对于某个特定的 Sequence 只能有一个 Selector。
然而，使用内部类很容易就能拥有另一个方法 reverseSelector()，
用它来生成一个反方向遍历序列的 Selector，
只有内部类才有这种【灵活性】。
 */


/*
【闭包与回调】
闭包（closure）是一个可调用的对象，它记录了一些信息，这些信息来自于创建它的作用域。
通过这个定义，可以看出内部类是面向对象的闭包，
因为它不仅包含外部类对象（创建内部类的作用域）的信息，
还自动拥有一个指向此外部类对象的引用。
在此作用域内，内部类有权操作所有的成员，包括 private 成员。

Java 最引人争议的问题之一就是：
人们认为 Java 应该包含某种类似指针的机制，以允许回调（callback）。
通过回调，对象能够携带一些信息，这些信息允许它在稍后的某个时刻调用初始的对象。
但是，如果回调是通过指针实现的，那么就只能寄希望于程序员不会误用该指针。
Java 更小心仔细，所以没有在语言中包括指针。
通过内部类提供闭包的功能是优良的解决方案，它比指针更灵活、更安全。

在 Java 8 之前，内部类是实现闭包的唯一方式。
在 Java 8 中，我们可以使用 lambda 表达式来实现闭包行为，并且语法更加优雅和简洁。


简言之：lambda -> 内部类 -> 指针
 */

/**
 * 使用内部类回调的例子：
 */
interface Incrementable {
    void increment();
}
// 实现接口
class Callee1 implements Incrementable {
    private int i = 0;

    @Override
    public void increment() {
        i++;
        System.out.println(i);
    }
}
class MyIncrement {
    public void increment() {
        System.out.println("其他操作");
    }
    static void f(MyIncrement mi) {
        mi.increment();
    }
}
// 如果类必须以其他方式实现 increment()，则必须使用内部类
class Callee2 extends MyIncrement {
    private int i = 0;

    @Override
    public void increment() {
        super.increment();
        i++;
        System.out.println(i);
    }
    private class Closure implements Incrementable {
        /* 下面的这个方法跟外部类的同签名方法没有任何关系 */
        @Override
        public void increment() {
            System.out.println("内部类的 increment() 方法， i= " + i);
            /* 调用外部类的方法 */
            /* 通过 Callee2 的“钩子”（hook）获取 Callee2 的信息 */
            /* 指定类外方法，否则将得到无限递归 */
            Callee2.this.increment();
        }
    }
    Incrementable getCallbackReference() {
        /* 调用这个方法就可以通过 Closure 类获取外部类的信息了 */
        return new Closure();
    }
}
class Caller {
    private Incrementable callbackReference;
    Caller(Incrementable cbh) {
        callbackReference = cbh;
    }
    void go() {
        callbackReference.increment();
    }
}
class Callbacks {
    public static void main(String[] args) {
        Callee1 c1 = new Callee1();
        Callee2 c2 = new Callee2();
        MyIncrement.f(c2);

        Caller caller1 = new Caller(c1);
        caller1.go();
        caller1.go();

        Caller caller2 = new Caller(c2.getCallbackReference());
        caller2.go();
        caller2.go();
    }
}

/*
上述例子进一步展示了外部类实现一个接口与内部类实现此接口之间的区别：
1. 就代码而言，Callee1 是更简单的解决方式。
2. Callee2 继承自 MyIncrement，后者已经有了一个不同的 increment() 方法（属于MyIncrement），
    与 Incrementable 接口期望的 increment() 方法完全不相关。
    ------------
    如果 Callee2 继承了 MyIncrement，
    就不能为了 Incrementable 的用途而重写 increment() 方法，
    于是只能使用内部类独立地实现 Incrementable。
    ------------
    还要注意，当创建了一个内部类时，
    并没有在外部类的接口中添加东西，也没有修改外部类的接口。

其他的东西：
1. 在 Callee2 中除了 getCallbackReference() 以外，其他成员都是 private 的。
    要想建立与外部世界的任何连接，接口 Incrementable 都是必需的。
2. 可以看到，interface 是如何允许接口与接口的实现完全独立的。
3. 内部类 Closure 实现了 Incrementable，
    以提供一个返回 Callee2 的“钩子”（hook）（通过内部类获取 Callee2 的信息），
    而且是一个安全的钩子。
    无论谁获得此 Incrementable 的引用，都只能调用 increment()，
    除此之外没有其他功能（不像指针那样，允许你做很多事情）。

【小结】
回调的价值在于它的灵活性（可以在运行时动态地决定需要调用什么方法）
例如，在图形界面实现 GUI 功能的时候，到处都用到回调。
 */



/*
【内部类与控制框架】
应用程序框架（application framework）就是被设计用以解决某类特定问题的一个类或一组类。
 *
要运用某个应用程序框架，通常是继承一个或多个类，并覆盖某些方法。
在覆盖后的方法中，编写代码定制应用程序框架提供的通用解决方案，以解决你的特定问题。
这是设计模式中模板方法的一个例子，
模板方法包含算法的基本结构，并且会调用一个或多个可覆盖的方法，以完成算法的动作。
 *
设计模式总是将变化的事物与保持不变的事物分离开，
在这个模式中，模板方法是保持不变的事物，而可覆盖的方法就是变化的事物。

控制框架是一类特殊的应用程序框架，它用来解决响应事件的需求。
主要用来响应事件的系统被称作事件驱动系统。
应用程序设计中常见的问题之一是图形用户接口（GUI），它几乎完全是事件驱动的系统。
 */

/**
 * 描述了所有控制事件的接口
 */
abstract class Event {
    private Instant eventTime;
    protected final Duration delayTime;

    public Event(long millisecondDelay) {
        /* 当希望运行 Event 并随后调用 start() 时，
        那么构造器就会捕获（从对象创建的时刻开始的）时间 */
        delayTime = Duration.ofMillis(millisecondDelay);
        start();
    }

    /* 方法是 public 的，允许重新启动 */
    public void start() {
        eventTime = Instant.now().plus(delayTime);
    }
    public boolean ready() {
        return Instant.now().isAfter(eventTime);
    }
    public abstract void action();
}
/**
 * 一个用来管理并触发事件的实际控制框架
 */
class Controller {
    private List<Event> eventList = new ArrayList<>();

    public void addEvent(Event c) {
        eventList.add(c);
    }
    /*
    run() 循环遍历 evenList，寻找可以执行的 Event 对象。
    只要改对象被使用过，就移除这个对象。
     */
    public void run() {
        while (eventList.size() > 0) {
            for (Event e : new ArrayList<>(eventList)) {
                if (e.ready()) {
                    System.out.println(e);
                    e.action();
                    eventList.remove(e);
                }
            }
        }
    }
}
/*
在上述例子中，并不知道 Event 到底做了什么。
这正是此设计的关键所在："使变化的事物与不变的事物相互分离”。
 */

/**
 * 此控制框架的一个特定实现：（如控制温室的运作）
 * （使用内部类，可以在单一的类里面产生对同一个基类 Event 的多种派生版本）
 */
class GreenhouseControls extends Controller {
    private boolean light = false;

    // 开灯的内部类：
    public class LightOn extends Event {
        public LightOn(long millisecondDelay) {
            super(millisecondDelay);
        }
        @Override
        public void action() {
            light = true;
        }
        @Override
        public String toString() {
            return "Light is on";
        }
    }
    // 开灯的内部类：
    public class LightOff extends Event {
        public LightOff(long millisecondDelay) {
            super(millisecondDelay);
        }
        @Override
        public void action() {
            light = false;
        }
        @Override
        public String toString() {
            return "Light is off";
        }
    }

    private boolean water = false;

    // 放水的内部类：
    public class WaterOn extends Event {
        public WaterOn(long millisecondDelay) {
            super(millisecondDelay);
        }
        @Override
        public void action() {
            water = true;
        }
        @Override
        public String toString() {
            return "Greenhouse water is on";
        }
    }
    // 关水的内部类：
    public class WaterOff extends Event {
        public WaterOff(long millisecondDelay) {
            super(millisecondDelay);
        }
        @Override
        public void action() {
            water = false;
        }
        @Override
        public String toString() {
            return "Greenhouse water is off";
        }
    }

    class Bell extends Event {
        public Bell(long millisecondDelay) {
            super(millisecondDelay);
        }
        @Override
        public void action() {
            addEvent(new Bell(delayTime.toMillis()));
        }
        @Override
        public String toString() {
            return "Bing";
        }
    }

    // 重新运行时间的内部类：
    public class Restart extends Event {
        private Event[] eventList;
        public Restart(long delayTime, Event[] eventList) {
            super(delayTime);
            this.eventList = eventList;
            for (Event e : eventList) {
                addEvent(e);
            }
        }
        @Override
        public void action() {
            for (Event e : eventList) {
                /* 运行每一个 Event */
                e.start();
                addEvent(e);
            }
            /* 重新运行这个 Event */
            start();
            addEvent(this);
        }
        @Override
        public String toString() {
            return "Restarting system";
        }
    }

    // 结束程序的内部类：
    public static class Terminate extends Event {
        public Terminate(long delayTime) {
            super(delayTime);
        }
        @Override
        public void action() { System.exit(0); }
        @Override
        public String toString() {
            return "Terminating";
        }
    }
}
/*
上述例子中，Bell 控制响铃，是多么像多重继承：
Bell 和 Restart 有 Event 的所有方法，
并且似乎也拥有外部类 GreenhouseContrlos 的所有方法。
 */

/**
 * 通过创建一个 GreenhouseControls 对象，
 * 并添加各种不同的 Event 对象来配置该系统
 */
class GreenhouseController {
    public static void main(String[] args) {
        GreenhouseControls gc = new GreenhouseControls();
        gc.addEvent(gc.new Bell(900));

        Event[] eventList = {
                gc.new LightOn(200),
                gc.new LightOff(400),
                gc.new WaterOn(600),
                gc.new WaterOff(800)
        };
        gc.addEvent(gc.new Restart(2000, eventList));

        gc.addEvent(new GreenhouseControls.Terminate(5000));
        gc.run();
    }
}
