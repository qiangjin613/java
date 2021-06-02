import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/*
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
如果使用内部类，还可以获得其他一些特性：
1.内部类可以有多个实例，每个实例都有自己的状态信息，并且与其外部类对象的信息相互独立。
2.在单个外部类中，可以让多个内部类以不同的方式实现同一个接口，或继承同一个类。
3.创建内部类对象的时刻并不依赖于外部类对象的创建（是嵌套类不需要吧？）
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
通过这个定义，
可以看出内部类是面向对象的闭包，因为它不仅包含外部类对象（创建内部类的作用域）的信息，
还自动拥有一个指向此外部类对象的引用。
在此作用域内，内部类有权操作所有的成员，包括 private 成员。

Java 最引人争议的问题之一就是：（因为闭包，所以要类似指针的机制，用于回调）
人们认为 Java 应该包含某种类似指针的机制，以允许回调（callback）。
通过回调，对象能够携带一些信息，这些信息允许它在稍后的某个时刻调用初始的对象。
但是，如果回调是通过指针实现的，那么就只能寄希望于程序员不会误用该指针。
Java 更小心仔细，所以没有在语言中包括指针。
通过内部类提供闭包的功能是优良的解决方案，它比指针更灵活、更安全。

在 Java 8 之前，内部类是实现闭包的唯一方式。
在 Java 8 中，我们可以使用 lambda 表达式来实现闭包行为，并且语法更加优雅和简洁


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
