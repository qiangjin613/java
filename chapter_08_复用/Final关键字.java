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

    // 可以作为编译常量
    private final int valueOne = 9;
    private static final int VALUE_TWO = 99;
    // 典型的公共常量
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
 * 编译器确保空白 final 在使用前必须被初始化。（在构造器或初始化块中给 final 数据赋初值）
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