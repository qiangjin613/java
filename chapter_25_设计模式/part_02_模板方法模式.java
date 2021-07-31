import java.util.stream.IntStream;

/*
【构建应用程序框架】
应用程序框架允许您从一个类或一组类开始，
创建一个新的应用程序，重用现有类中的大部分代码，
并根据需要覆盖一个或多个方法来定制应用程序。

【模板方法模式】
应用程序框架中的一个基本概念是模板方法模式，
它通常隐藏在底层，通过调用基类中的各种方法来驱动应用程序。

模板方法模式的一个重要特性是它是在基类中定义的，并且不能更改。
它有时是一个 private 方法，但实际上总是 final。
它调用其他基类方法(您覆盖的那些)来完成它的工作，
但是它通常只作为初始化过程的一部分被调用(因此框架使用者不一定能够直接调用它)。
 */
abstract class ApplicationFramework {
    ApplicationFramework() {
        templateMethod();
    }

    abstract void customize1();
    abstract void customize2();

    private void templateMethod() {
        IntStream.range(0, 5).forEach(n -> { customize1(); customize2(); });
    }
}

class MyApp extends ApplicationFramework {
    @Override
    void customize1() {
        System.out.print("Hello ");
    }

    @Override
    void customize2() {
        System.out.println("World!");
    }
}

class TemplateMethod {
    public static void main(String[] args) {
        new MyApp();
    }
}
/*
基类构造函数负责执行必要的初始化，
然后启动运行应用程序的“engine”(模板方法模式)(在GUI应用程序中，这个“engine”是主事件循环)。
框架使用者只提供 customize1() 和 customize2() 的定义，然后“应用程序”已经就绪运行。
 */
