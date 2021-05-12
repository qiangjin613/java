import java.util.Random;

/**
 * 根据上下文环境，Java 的关键字 final 的含义有些微的不同，
 * 但通常它指的是“这是不能被改变的”。
 *
 * 防止改变有两个原因：设计或效率。因为这两个原因相差很远，所以有可能误用关键字 final。
 */

/**
 * 【final 数据】
 * 什么时候使用 final 关键字：
 *      1) 一个永不改变的编译时常量
 *      2) 一个在运行时初始化就不会改变的值
 *
 * final 修饰基本类型和引用的区别：
 *      1) 基本类型：数值恒定不变
 *      2) 引用：引用恒定不变，但引用所指向的对象却没有限定。（数组也是对象）
 */
class Value {
    int i;
    Value(int i) {
        this.i = i;
    }
}
class FinalData {
    private static Random rand = new Random(47);
    private String id;

    public FinalData(String id) {
        this.id = id;
    }

    // 可以作为编译时常量
    private final int valueOne = 9;
    private static final int VALUE_TWO = 99;
    // 典型的公共常量（也是编译时常量）
    private static final int VALUE_THREE = 39;
    // 不能作为编译常量（在编译时不知道它的值）
    private final int i4 = rand.nextInt(20);
    static final int INT_5 = rand.nextInt(20);
    private Value v1 = new Value(11);
    private final Value v2 = new Value(22);
    private static final Value VAL_3 = new Value(33);
    // 数组
    private final int[] a = {1, 2, 3, 4, 5, 6};

    @Override
    public String toString() {
        return id + ": " + "i4 = " + i4 + ", INT_5 = " + INT_5;
    }

    public static void main(String[] args) {
        FinalData fd1 = new FinalData("fd1");
        // [1] 常量不可改变
        // fd1.valueOne++;
        // fd1.VAL_3 = new Value(1);

        // [2] 对象引用不能被改变
        // fd1.v2 = new Value(9);
        fd1.v2.i++;
        // 数组也是一种对象
        // fd1.a = new int[3];
        for (int i = 0; i < fd1.a.length; i++) {
            // “对象”本身是没有限制的
            fd1.a[i]++;
        }

        fd1.v1 = new Value(9);

        System.out.println(fd1);
        System.out.println("Creating new FinalData");
        FinalData fd2 = new FinalData("fd2");
        System.out.println(fd1);
        System.out.println(fd2);
    }
}


/**
 * 【空白 final】
 * 空白 final 指的是没有初始化值的 final 属性。
 * 但是，编译器确要保空白 final 在使用前必须被初始化。（在构造器或初始化块中给 final 数据赋初值）
 * 这样既能使一个类的每个对象的 final 属性值不同，也能保持它的不变性。
 */
class Poppet {
    private int i;
    Poppet(int i) {
        this.i = i;
    }
    @Override
    public String toString() {
        return "Poppet{" +
                "i=" + i +
                '}';
    }
}
class BlankFinal {
    private final int i = 0;
    // 空白 final
    private final int j;
    private final Poppet p;

    // [1] 初始化块中初始化（使用实例初始化）
    // {
    //     j = 1;
    //     p = new Poppet(1);
    // }

    // [2] 构造函数中初始化
    public BlankFinal() {
        j = 1;
        p = new Poppet(1);
    }
    public BlankFinal(int x) {
        j = x;
        p = new Poppet(x);
    }

    @Override
    public String toString() {
        return "BlankFinal{" +
                "i=" + i +
                ", j=" + j +
                ", p=" + p +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(new BlankFinal());
        System.out.println(new BlankFinal(47));
    }
}


/**
 * 【final 参数】
 * 在参数列表中，将参数声明为 final 意味着在方法中不能改变参数指向的对象或基本变量
 */
class Gizmo {
    public void spin() {}
}
class FinalArguments {
    void with(final Gizmo g) {
        // 编译错误，不能给 final 变量 g 赋值
        // g = new Gizmo();
    }
    void without(Gizmo g) {
        g = new Gizmo();
        g.spin();
    }

    void f(final int i) {
        // 编译错误，不能给 final 变量 i 赋值
        // i++;
    }
    int g(final int i) {
        return i + 1;
    }

    // final 修饰引用参数
    void change(final Value v) {
        // 编译错误，不能给 final 变量 v 赋值
        // v = new Value(47);

        // 但可以改变 v 的属性值
        v.i++;
    }

    public static void main(String[] args) {
        FinalArguments bf = new FinalArguments();
        bf.without(null);
        bf.with(null);
    }
}


/**
 * 【final 方法】
 * 使用 final 方法的原因有两个：
 * 1) 给方法上锁
 *    防止子类通过覆写方法的行为。这是出于继承的考虑，确保方法的行为不会因继承而改变。
 * 2) 效率
 *    现在基本上是不用这种方法来提升效率的！
 *    应该让编译器和 JVM 处理性能问题，只有在为了明确禁止覆写方法时才使用 final。
 */


/**
 * 【final 和 private】
 * 类中所有的 private 方法都隐式地指定为 final。
 * 因为不能访问 private 方法，所以不能覆写它。
 * 可以给 private 方法添加 final 修饰，但是并不能给方法带来额外的含义。
 *
 * final：可以访问，但不可覆写
 * private：不能访问，也就不可覆写
 * final 给的权限要更大一些（相比与 private，final 最起码可以访问）
 */
class WithFinals {
    // [1] 与 g() 效果相同，private 默认就是 final 的
    // 修饰方法时：private final 就等同于 private
    // 注意一点，修饰变量时，private final 与 private 作用差大了！
    private final void f() {
        System.out.println("WithFinals.f()");
    }
    // [2] 自动地为 final
    private void g() {
        System.out.println("WithFinals.g()");
    }

    public final void h() {
        System.out.println("WithFinals.h()");
    }
}

class OverridingPrivate extends WithFinals {
    // f() 和 g() 与基类的方法没有任何关系，只是恰好有同名的方法而已，没有“覆写”关系
    private final void f() {
        System.out.println("OverridingPrivate.f()");
    }
    private void g() {
        System.out.println("OverridingPrivate.g()");
    }
    // 编译错误，不能覆盖 WithFinals 的 final h() 方法
    // public void h() {
    //     System.out.println("OverridingPrivate.h()");
    // }
}

class OverridingPrivate2 extends OverridingPrivate {
    public final void f() {
        System.out.println("OverridingPrivate2.f()");
    }
    public void g() {
        System.out.println("OverridingPrivate2.g()");
    }
}

class FinalOverridingIllusion {
    public static void main(String[] args) {
        OverridingPrivate2 op2 = new OverridingPrivate2();
        op2.f();
        op2.g();
        // 可以向上转型：
        OverridingPrivate op = op2;
        op.h();
        // 但是不可以使用方法：
        // op.f();
        // op.g();

        // 一样不可以使用该方法：
        WithFinals wf = op2;
        wf.h();
        // wf.f();
        // wf.g();
    }
}

/**
 * 一段需要理解的话，解释 private 的奥秘：
 * 如果一个方法是 private 的，它就不是基类接口的一部分。
 * 它只是隐藏在类内部的代码，且恰好有相同的命名而已。
 * 但是如果你在派生类中以相同的命名创建了 public，protected 或包访问权限的方法，
 * 这些方法与基类中的方法没有联系，你没有覆写方法，只是在创建新的方法而已。
 * 由于 private 方法无法触及且能有效隐藏，除了把它看作类中的一部分，其他任何事物都不需要考虑到它。
 */


/**
 * 【final 类】
 * 当一个类是 final 的，就意味着它不能被继承！（也就没有“覆写”）
 * 之所以这么做，是因为类的设计就是永远不需要改动，或者处于安全考虑不希望它有子类。
 *
 * 之外，由于 final 类禁止继承，也就禁止覆写。
 * 可以在 final 类中的方法加上 final 修饰符，但这并不会有任何意义。
 *
 * “覆写”关系是建立在“继承”之上的！
 */
class SmallBrain {}

final class Dinosaur {
    // [1] final 类的字段并不会被隐式地指定为 final
    int i = 7;
    final int j = 1;
    SmallBrain x = new SmallBrain();
    // [2] final 类的方法隐式指定为 fianl
    void f() {}
}
// 不能继承 fianl 类 Dinosaur
// class Further extends Dinosaur {}

class Jurassic {
    public static void main(String[] args) {
        Dinosaur n = new Dinosaur();
        n.f();
        n.i = 40;
        // 不可以为 final 字段赋值
        // n.j++;
        n.x = new SmallBrain();
    }
}