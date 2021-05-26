/**
 * 抽象类的示例
 */
abstract class Basic {
    /* 抽象方法不可以有方法体 */
    abstract void unimplemented();
}

class AttemptToUseBasic {
    // 'Basic' is abstract; cannot be instantiated
    // Basic b = new Basic();
}

/**
 * 继承抽象类，派生类可以为抽象类，也可以不是抽象类
 */
// 派生类是一个抽象类：
abstract class Basic2 extends Basic {
    int f() {
        return 111;
    }

    abstract void g();
}
// 派生类不是一个抽象类：
class Instantiable extends Basic2 {
    @Override
    void unimplemented() {
        System.out.println("");
    }

    @Override
    void g() {
        System.out.println("");
    }

    public static void main(String[] args) {
        Basic2 b = new Instantiable();
        b.f();
        b.g();
    }
}


/**
 * 可以将一个不包含任何抽象方法的类指明为 abstract，
 * 在类中的抽象方法没啥意义但想阻止创建类的对象时，这么做就很有用。
 */
abstract class Basic3 {
    int f() {
        return 111;
    }
}

class AbstractWithoutAbstracts {
    // 'Basic3' is abstract; cannot be instantiated
    // Basic3 b = new Basic3();
}

/**
 * 接口只允许 public 方法，如果不加访问修饰符的话，
 * 接口的方法不是 friendly 而是 public
 */
abstract class AbstractAccess {
    private void m1() {}
    // private 和 abstract 的组合是非法的：
    // private abstract void m1a();

    protected void m2() {}
    protected abstract void m2a();

    void m3() {}
    abstract void m3a();

    public void m4() {}
    public abstract void m4a();
}

/**
 * 【小结】
 * 抽象方法结构刨析：（abstract 就是用来修饰方法的）
 * abstract 在一定程度上声明了“这个东西是要被重写的”与 private 本身的含义就是冲突的！
 * 而在抽象类中的 变量、类方法、初始化块、构造器 这些东西，派生类中对于这个压根谈不上重写，所以放在一起也就没有任何意义了。
 */
abstract class AbstractDemo {
    // 变量：
    private int i = 1;
    protected int j = 2;
    int k = 3;
    public int p = 5;
    private static int i2s = 1;
    protected static int j2s = 2;
    static int k2s = 3;
    public static int p2s = 5;

    // abstract 不能修饰变量：因为没意义！
    // abstract int i = 23;
    // abstract static int i = 23;

    // 方法：
    private void m1() {}
    // private 和 abstract 的组合是非法的：
    // private abstract void m1a();

    protected void m2() {}
    protected abstract void m2a();

    void m3() {}
    abstract void m3a();

    public void m4() {}
    public abstract void m4a();

    // 静态方法：
    private static void m1s() {}

    protected static void m2s() {}
    /* abstract 不允许和 static 一起使用 */
    // protected static abstract void m2as();

    void m3s() {}
    // static abstract void m3as();

    public void m4s() {}
    // public static abstract void m4as();


    // 初始化块：
    // abstract {
    //     System.out.println("");
    // }
    // abstract static {
    //    System.out.println("");
    // }

    // 构造器：
    AbstractDemo() {
        i = 32;
    }
    // abstract AbstractDemo(int a) {
    //     i = 32;
    // }
}

/*
创建抽象类和抽象方法是有帮助的，因为它们使得类的抽象性很明确，并能告知用户和编译器使用意图。
 */
