package part_03_最简单的处理器;

/**
 * 提供一个演示示例：
 */
@Simple
class SimpleTest {

    @Simple
    int i;

    @Simple
    public SimpleTest() {}

    @Simple
    public void food() {
        System.out.println("simpleTestObj.food()");
    }

    @Simple
    public void bar(String s, int i, float f) {
        System.out.println("simpleTestObj.bar()");
    }

    @Simple
    public static void main(String[] args) {
        @Simple
        SimpleTest st = new SimpleTest();
        st.food();
    }
}
/*
在上述例子中，使用 @Simple 注解了所有 @Target 声明允许的地方。
SimpleTest.java 只需要 Simple.java 就可以编译成功。当我们编译的时候什么都没有发生（对于 @Simple 而言）。
javac 允许 @Simple 注解（只要它存在）在我们创建处理器并将其 hook 到编译器之前，不做任何事情。
 */

