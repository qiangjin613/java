/*
在 Java 8 引入 default 方法之后，
选择用抽象类还是用接口变得更加令人困惑。

下面先做出 抽象类、接口 的区分，
再进行总结如何使用及什么情况下使用抽象类|接口
 */

/*
【抽象类】
1.变量
2.初始化块
3.构造器
4.方法

abstract 不能修饰变量、初始化块、构造器；
abstract 不能修饰 private 方法，不能和 private、static连用；

Illegal combination of modifiers:
    'abstract' and 'private'
    'abstract' and 'static'
    'abstract' and 'final'

最后，抽象类除了不能被实例化，其他方面和普通类没有什么区别。
 */
abstract class AbstractClass {
    /**
     * 1.变量
     */
    /* 实例变量 */
    private int i = 1;
    protected int j = 2;
    int k = 3;
    public int p = 5;
    /* final变量 */
    private final int i3f = 1;
    protected final int j3f = 2;
    final int k3f = 3;
    public final int p3f = 5;
    /* 类变量 */
    private static int i2s = 1;
    protected static int j2s = 2;
    static int k2s = 3;
    public static int p2s = 5;
    /* 编译常量 */
    private final static int i2fs = 1;
    protected final static int j2fs = 2;
    final static int k2fs = 3;
    public final static int p2fs = 5;

    /* abstract 不能修饰变量：因为没意义！ */
    // abstract int i = 23;
    // abstract static int i = 23;


    /**
     * 2.初始化块
     */
    /* 实例初始化块 */
    {
        System.out.println("实例初始化块");
    }
    /* 静态初始化块 */
    static {
        System.out.println("静态初始化块");
    }
    /* abstract 不能修饰初始化块 */
    // abstract {
    //     System.out.println("");
    // }
    // abstract static {
    //    System.out.println("");
    // }


    /**
     * 3.构造器
     */
    AbstractClass() {
        i = 32;
    }
    AbstractClass(int i) {
        this.i = i;
    }

    /* abstract 不能修饰构造器 */


    /**
     * 4.方法
     */
    /* 实例方法 */
    private void m1() {}
    protected void m2() {}
    void m3() {}
    public void m4() {}
    /* 类方法 */
    private static void m1s() {}
    protected static void m2s() {}
    static void m3s() {}
    public static void m4s() {}
    /* final 实例方法 */
    private final void m1f() {}
    protected final void m2f() {}
    final void m3f() {}
    public final void m4f() {}
    /* final 类方法 */
    private final static void m1sf() {}
    protected final static void m2sf() {}
    static final void m3sf() {}
    public final static void m4sf() {}
    /* private、static 方法可以被看作是 fianl 的 */

    /* 默认方法只能在接口中使用 */
    // default void m() {}

    /* 抽象方法 */
    /* private 和 abstract 的组合是非法的 */
    // private abstract void m1a();
    protected abstract void m2a();
    abstract void m3a();
    public abstract void m4a();

    /* abstract 不允许和 static、final 一起使用 */

    public static void main(String[] args) {
        /* 抽象类不能被实例化 */
        // new AbstractClass();

        AbstractClass.m1s();
        System.out.println(AbstractClass.j2s);
    }
}



/*
【接口】
1.变量：都是 public、static、final 修饰的常量
2.初始化块  （x）
3.构造器   （x）
4.方法

接口中的变量都是 public static final 的；
接口中的实例方法都是 public abstract 的、静态方法都是 public static 有方法体的具体方法。
Java 8 之后接口允许包含默认方法（给实例的）和静态方法（给类的）


Illegal combination of modifiers:
    'static' and 'final'
 */
interface InterfaceDemo {
    /**
     * 1.变量
     */
    int I = 1;


    /**
     * 2.初始化块
     */
    /* 不允许有初始化块 */


    /**
     * 3.构造器
     */
    /* 不允许有构造器 */


    /**
     * 4.方法
     */
    /* 实例方法（默认是 abstract 的） */
    public abstract void m4();
    /* 类方法 */
    public static void m3s() {}
    /* 没有 final 方法 */
    /* 默认方法（默认为 public 的，也只能为 public） */
    public default void me() {

    }
}



/*
【小结】
组合（继承|实现）
    接口：新类可以组合多个接口
    抽象类：只能继承单一抽象类
状态（变量）
    接口：不能包含属性（除了静态属性，不支持对象状态）
    抽象类：可以包含属性，非抽象方法（有方法体的）可能引用这些属性
默认方法和抽象方法
    接口：所有方法都是 public 的！
        实例方法默认是 public abstract
        静态方法默认是 public static
        默认方法默认是 public default
    抽象类：基本和普通类没什么不同
构造器
    接口：没有
    抽象类：有
可见性
    接口：隐式都为 public
    抽象类：可以为 public、protected、"friendly"、private
初始化块
    接口：没有
    抽象类：有
 */

/*
1.在合理的范围内尽可能地抽象（更倾向使用接口而不是抽象类）。
2.只有当必要时才使用抽象类。
3.除非必须使用，否则不要用接口和抽象类。
（大多数时候，普通类已经做得很好，如果不行的话，再移动到接口或抽象类中）
 */
