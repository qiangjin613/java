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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
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

下面的例子并没有使用 distince() 和 filter()，演示了一个寂寞...
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
1.map(Function)：将函数操作应用在输入流的元素中，并将返回值传递到输出流中。

2.mapToInt(ToIntFunction)、mapToLong(ToLongFunction) 和
mapToDouble(ToDoubleFunction) ：操作同上，但结果是 IntStream、LongStream 和 DoubleStream。
 */
class FunctionMap {
    static String[] elements = {"12", "", "23", "1s", "45"};
    static Stream<String> testStream() {
        return Arrays.stream(elements);
    }
    static void test(String descr, Function<String, String> func) {
        System.out.println(" ---( " + descr + " )---");
        testStream()
                .map(func)
                .forEach(System.out::println);
    }

    public static void main(String[] args) {
        test("使用 [] 包裹", s -> "[" + s + "]");
        test("尝试转换为 Integer 类型", s -> {
            try {
                return Integer.parseInt(s) + 1 + "";
            } catch (NumberFormatException e) {
                return s;
            }
        });
        test("将 2 替换为 9", s -> s.replace("2", "9"));
        test("打印最后一个字符", s -> s.length() > 0? s.charAt(s.length() - 1) + "": s);
    }
}

/*
在以上例子中，map() 将一个字符串映射为另一个字符串。

进一步，我们完全可以产生和接收类型完全不同的类型，从而改变流的数据类型。
如下：
 */
class Numbered {
    final int n;
    Numbered(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "Numbered{" + "n=" + n + '}';
    }
}
class FunctionMap2 {
    public static void main(String[] args) {
        Stream.of(1, 3, 2, 4, 5, 8, 6, 9)
                .map(Numbered::new)
                .forEach(System.out::println);
    }
}
/*
将获取到的整数通过构造器 Numbered::new 转化成为 Numbered 类型。
（将整数类型的流转为了 Numbered 类型的流）
 */

/*
如果使用 Function 返回的结果是数值类型的一种，
我们必须使用合适的 mapToXxx() 进行替代。
如下：
 */
class FunctionMap3 {
    public static void main(String[] args) {
        Stream.of("5", "7", "9")
                .mapToInt(Integer::parseInt)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        Stream.of("17", "324", "897")
                .mapToLong(Long::parseLong)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        Stream.of("14", "2.8", "1.234")
                .mapToDouble(Double::parseDouble)
                .forEach(n -> System.out.format("%f ", n));
    }
}



/*
【在 map() 中组合流：使用 flatMap()】
【场景】假设现在有一个流元素，并且打算对流元素使用 map() 函数。
有一个问题：map() 是产生一个流。我们想要产生一个元素流，而 map() 实际却产生了一个元素流的流。
（很绕... 可能翻译的人也不知道翻译小哥翻译的是什么意思...）
（继续往下看，实际很简单...）

flatMap() 做了两件事：将产生流的函数应用在每个元素上（与 map() 所做的相同），
然后将每个流都扁平化为元素，因而**最终产生的仅仅是元素**。
（flatMap() 最终产生的是元素。map() 产生的是流）

【使用】
flatMap(Function)：当 Function 产生流时使用。
flatMapToInt(Function)：当 Function 产生 IntStream 时使用。
flatMapToLong(Function)：当 Function 产生 LongStream 时使用。
flatMapToDouble(Function)：当 Function 产生 DoubleStream 时使用。
 */

/*
先来一个例子，对 map() 和 flatMap() 进行比较，一探究竟。
如下：
 */
class StreamOfStreams {
    public static void main(String[] args) {
        Stream.of(1, 2, 3)
                .map(i -> Stream.of("AAA", "BBB", "CCC"))
                .forEach(System.out::println);
    }
}
/*
输出：
java.util.stream.ReferencePipeline$Head@7ba4f24f
java.util.stream.ReferencePipeline$Head@3b9a45b3
java.util.stream.ReferencePipeline$Head@7699a589

在上述例子中，希望将一个整数流元素转化为三个字符串流，
但实际得到的却是 “Head”流的流。
 */

/*
使用 flatMap() 试试：
 */
class FlatMap {
    public static void main(String[] args) {
        Stream.of(1, 2, 3)
                .flatMap(i -> Stream.of("AAA", "BBB", "CCC"))
                .forEach(System.out::println);
    }
}
/*
输出：（3组 "AAA", "BBB", "CCC"）
AAA
BBB
CCC
AAA
BBB
CCC
AAA
BBB
CCC

从上述示例中，可以看到：从映射返回的每个流都会自动扁平为组成它的字符串。
 */

/*
进一步演示 flatMap() 的功能。
下面 从一个整数流开始，然后使用每一个整数去创建更多的随机数 的例子：
 */
class StreamOfRandoms {
    static Random rand = new Random(47);

    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4)
                .flatMapToInt(i -> IntStream.concat(
                                rand.ints(0, 100).limit(i),
                                IntStream.of(-1)))
                .forEach(n -> System.out.format("%d ", n));
    }
}
/*
输出：
58 -1 55 93 -1 61 61 29 -1 68 0 22 7 -1

IntStream.concat(IntStream a, IntStream b) 方法以参数顺序组合两个流。
在上述例子中，在每个随机 Integer 流的末尾添加一个 -1 作为标记。
（可以看到最终流确实是从一组扁平流中创建的）

又因为随机生成的 Integer 是一个 IntStream，所以必须使用 flatMapToInt() 的特定整数形式。
 */


/*
下面再次看一下将文件划分为单词流的任务（解决一下在 *part_02_流创建* 中埋的坑...）

上一个问题是需要将整个文件读入行列表中 —— 显然需要存储该列表。
而我们真正想要的是创建一个不需要中间存储层的单词流。

下面来使用 flatMap() 解决这个问题：
 */
class FileToWords {
    public static Stream<String> stream(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .skip(1) // 跳过首行
                .flatMap(line -> Pattern.compile("\\W+").splitAsStream(line));
        // 或者可以使用String.split() 生成一个数组，再使用 Arrays.stream() 转化为一个流。如下：
        //      .flatMap(line -> Arrays.stream(line.split("\\W+")))
    }
    public static Stream<String> stream() {
        return Pattern.compile("[ .,?]+").splitAsStream("Not much of a cheese shop really, is it?");
    }
}

/*
因为流并不能被复用，所以每次使用都必须从头创建。
如下：
 */
class FileToWordsTest {
    public static void main(String[] args) throws IOException {
        FileToWords.stream("cheese.dat")
                .limit(7)
                .forEach(s -> System.out.format("%s ", s));
        System.out.println();
        FileToWords.stream("cheese.dat")
                .skip(2)
                .limit(3)
                .forEach(s -> System.out.format("%s ", s));
    }
}
