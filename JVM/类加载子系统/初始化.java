package 类加载子系统;

/**
 * 演示：类加载过程中的 "链接 -> 初始化" 环节，对变量的影响
 */
class InitTest {
    private static int num = 1;

    static {
        num = 2;
        number = 20;

        System.out.println(num);
        // System.out.println(number);  /* 非法的向前引用 */
    }

    // linking 的 prepare 中：置为 0 值；在 init 过程中：先置为 20，后在此处置为 10
    private static int number = 10;

    public static void main(String[] args) {
        System.out.println(InitTest.num); // output：2  过程：（链接的准备阶段）0 -> （初始化阶段）1 -> 2
        System.out.println(InitTest.number); // output：10  过程：（链接的准备阶段）0 -> （初始化阶段）20 -> 10

    }
}
/*
在类的 linking 的 prepare 过程中：为类变量分配内存并设置该变量的默认初始值（零值）
        在这个环节要说明的是：
        1）不包含 final static 的变量，因为 final 在编译的时候就会分配了，准备阶段会显式进行初始化；
        2）这里不会对实例变量进行分配，因为实例变量还未产生！

在类的 初始化阶段：执行类构造方法 <clinit>() 的过程（这个与构造器不同）
        在这里需要说明的是：
        1）此方法不需要定义，是 javac 编译器自动收集类中的所有变量的复制动作和静态代码块中的语句合并而来；
        2）<clinit>() 中按指令语句在源文件中出现的顺序执行（如同上例中的 number：20 -> 10）

另外，对于“向前引用”：如同 number，不可以在变量定义之前就使用该变量。
 */
