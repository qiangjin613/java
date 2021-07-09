/*
【概述】
中间操作用于从一个流中获取对象，
并将对象作为另一个流从后端输出，以连接到其他操作。

主要有：
1.跟踪和调试
2.流元素排序
3.移除元素
4.应用函数到元素
5.在 map() 中组合流
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/*
【跟踪和调试 peek()】
peek() 操作的目的是帮助调试。它允许你无修改地查看流中的元素。
如下：
 */
class Peeking {
    public static void main(String[] args) {
        FileToWords.stream()
                .skip(1)
                .limit(4)
                .map(w -> w + " ")
                .peek(System.out::print)
                .map(String::toUpperCase)
                .peek(System.out::print)
                .map(String::toLowerCase)
                .forEach(System.out::print);
    }
}
/*
因为 peek() 符合无返回值的 Consumer 函数式接口，
所以我们只能观察，无法使用不同的元素来替换流中的对象。
 */


/*
【流元素排序 sorted()】
之前熟识了 sorted() 的默认比较器实现。
其实它还有另一种形式的实现：传入一个 Comparator 参数。

如下：使用的是反转“自然排序”
 */
class SortedComparator {
    public static void main(String[] args) {
        FileToWords.stream()
                .limit(10)
                .map(String::toLowerCase)
                .sorted(Comparator.reverseOrder())
                .map(w -> w + " ")
                .forEach(System.out::print);
    }
}


/*
【移除元素 distinct()、filter(Predicate)】
1.distinct()：在 Randoms.java 类中的 distinct() 可用于消除流中的重复元素。
    相比创建一个 Set 集合，该方法的工作量要少得多
2.filter(Predicate)：过滤操作会保留与传递进去的过滤器函数计算结果为 true 元素

下面的例子并没有使用 distince() 和 filter() 演示了一个寂寞...
 */
class Prime {
    public static Boolean isPrime(long n) {
        return LongStream.rangeClosed(2, (long)Math.sqrt(n))
                .noneMatch(i -> n % i == 0);
        /*
        rangeClosed() 包含了上限值。
        noneMatch() 操作一旦有失败就会退出
         */
    }
    public LongStream numbers() {
        return LongStream.iterate(2, i -> i + 1)
                .filter(Prime::isPrime);
    }

    public static void main(String[] args) {
        new Prime().numbers()
                .limit(10)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        new Prime().numbers()
                .skip(90)
                .limit(10)
                .forEach(n -> System.out.format("%d ", n));
    }
}


/*
【应用函数到元素】
 */













class FileToWords {
    public static Stream<String> stream(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .skip(1) // 跳过首行
                .flatMap(line -> Pattern.compile("\\W+").splitAsStream(line));
    }
    public static Stream<String> stream() {
        return Pattern.compile("[ .,?]+").splitAsStream("Not much of a cheese shop really, is it?");
    }
}










