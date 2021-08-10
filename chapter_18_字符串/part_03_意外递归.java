import java.util.stream.Stream;

/**
 * 一个小而重要的例子揭示意外递归
 */
class InfiniteRecursion {
    @Override
    public String toString() {
        /*
        当运行该代码时："InfiniteRecursion address: " + this
           编译器发现一个 String 对象后面跟着一个 “+”，
           而 “+” 后面的对象不是 String，
           于是编译器试着将 this 转换成一个 String。
       就发生了自动类型转换，InfiniteRecursion 类型转换为 String 类型。
       它怎么转换呢？
       正是通过调用 this 上的 toString() 方法，
       于是就发生了递归调用，就引发了 StackOverflowError 错误！
       【注意】这种错误在编译器中是无法检测出来的！
        */
        // return "InfiniteRecursion address: " + this + "\n";

        // 可以使用基类的 toString() 方法
        return "InfiniteRecursion address: " + super.toString() + "\n";
    }

    public static void main(String[] args) {
        Stream.generate(InfiniteRecursion::new)
                .limit(10)
                .forEach(System.out::println);
    }
}
