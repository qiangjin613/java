import java.util.stream.Stream;

/**
 * 一个“打印类的内存地址”的例子揭示意外递归
 */
class InfiniteRecursion {
    @Override
    public String toString() {
        // 当打印 this 时，就调用 this.toString() 方法，于是就发生了递归调用。从而引发 StackOverflowError 错误
        // 可以使用基类的 toString() 方法
        return "InfiniteRecursion address: " + super.toString() + "\n";
    }

    public static void main(String[] args) {
        Stream.generate(InfiniteRecursion::new)
                .limit(10)
                .forEach(System.out::println);
    }
}