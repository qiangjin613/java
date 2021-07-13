/*
在以下操作将会获取流的最终结果。
至此我们无法再继续往后传递流。
可以说，终端操作总是我们在流管道中所做的最后一件事。
 */

import java.io.IOException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
【数组】
1. toArray()：将流转换成适当类型的数组。
2. toArray(generator)：在特殊情况下，生成自定义类型的数组。
当我们需要得到数组类型的数据以便于后续操作时，上面的方法就很有用。

示例：
假设我们需要复用流产生的随机数。
 */
class RandInts {
    private static int[] rints = new Random(47).ints(0, 1000)
            .limit(100)
            .toArray();
    public static IntStream rands() {
        return Arrays.stream(rints);
    }
}
/*
上例将获取的随机数流保存在 rints 中。
这样一来，每次调用 rands() 的时候可以重复获取相同的整数流。
 */




/*
【循环】
1. forEach(Consumer)：常见如 System.out::println 作为 Consumer 函数。
2. forEachOrdered(Consumer)： 保证 forEach 按照原始流顺序操作。

说明：第一种形式forEach(Consumer)：无序操作，仅在引入并行流时才有意义。
parallel()：可实现多处理器并行操作。
实现原理为将流分割为多个（通常数目为 CPU 核心数）并在不同处理器上分别执行操作。
因为我们采用的是内部迭代，而不是外部迭代，所以这是可能实现的。
 */
class ForEach {
    static final int SZ = 9;

    public static void main(String[] args) {
        RandInts.rands()
                .limit(SZ)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        RandInts.rands()
                .limit(SZ)
                .parallel()
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        RandInts.rands()
                .limit(SZ)
                .parallel()
                .forEachOrdered(n -> System.out.format("%d ", n));
    }
}
/*
结果显式：
在第一个流中，未使用 parallel()，所以 rands() 按照元素迭代出现的顺序显示结果；
在第二个流中，引入parallel() ，即便流很小，输出的结果顺序也和前面不一样。
    这是由于多处理器并行操作的原因。
    多次运行测试，结果均不同。
    多处理器并行操作带来的非确定性因素造成了这样的结果。
在最后一个流中，同时使用了 parallel() 和 forEachOrdered() 来强制保持原始流顺序。
    因此，对非并行流使用 forEachOrdered() 是没有任何影响的。
 */



/*
【集合】
1. collect(Collector)：使用 Collector 收集流元素到结果集合中。
2. collect(Supplier, BiConsumer, BiConsumer)：同上，
    第一个参数 Supplier 创建了一个新结果集合，
    第二个参数 BiConsumer 将下一个元素包含到结果中，
    第三个参数 BiConsumer 用于将两个值组合起来。

说明：实际上，它还有一些非常复杂的操作实现，可通过查看 java.util.stream.Collectors 的 API 文档了解。
例如，我们可以将元素收集到任意一种特定的集合中。
 */
class TreeSetOfWords {
    public static void main(String[] args) throws IOException {
        Set<String> words = FileToWords.stream()
                .flatMap(s -> Arrays.stream(s.split("\\W+")))
                .filter(s -> !s.matches("\\d+"))
                .map(String::trim)
                .filter(s -> s.length() > 2)
                .limit(10)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println(words);
    }
}
/*
上例中，为了保证元素有序，将元素存储在 TreeSet 中。
Collectors 里面没有特定的 toTreeSet()，
但是我们可以通过将集合的构造函数引用传递给 Collectors.toCollection()，
从而构建任何类型的集合。
 */

/*
进一步展示在流中生成 Map。如下：
 */
class Pair {
    public final Character c;
    public final Integer i;
    Pair(Character c, Integer i) {
        this.c = c;
        this.i = i;
    }
    public Character getC() {
        return c;
    }
    public Integer getI() {
        return i;
    }
    @Override
    public String toString() {
        return "Pair{" + "c=" + c + ", i=" + i + '}';
    }
}
class RandomPair {
    Random rand = new Random(47);
    Iterator<Character> capChars = rand.ints(65, 91)
            .mapToObj(i -> (char)i)
            .iterator();
    public Stream<Pair> stream() {
        /*
        在 Java 中，我们不能直接以某种方式组合两个流。
        所以这里创建了一个整数流，并且使用 mapToObj() 将其转化成为 Pair 流。
         */
        return rand.ints(100, 1000)
                .distinct()
                .mapToObj(i -> new Pair(capChars.next(), i));
    }
}
class MapCollector {
    public static void main(String[] args) {
        Map<Integer, Character> map = new RandomPair().stream()
                .limit(8)
                .collect(Collectors.toMap(Pair::getI, Pair::getC));
        System.out.println(map);
    }
}
/*
在这里，我们只使用最简单形式的 Collectors.toMap()，
这个方法值需要一个可以从流中获取键值对的函数。
还有其他重载形式，其中一种形式是在遇到键值冲突时，需要一个函数来处理这种情况。
 */

/*
大多数情况下，java.util.stream.Collectors 中预设的 Collector 就能满足我们的要求。
除此之外，还可以使用第二种形式的 collect()。
下例给出基本用法：
 */
class SpecialCollector {
    public static void main(String[] args) {
        ArrayList<String> words = FileToWords.stream()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        words.stream()
                .filter(s -> s.equals("cheese"))
                .forEach(System.out::println);
    }
}



/*
【组合】
1. reduce(BinaryOperator)：使用 BinaryOperator 来组合所有流中的元素。
    因为流可能为空，其返回值为 Optional。
2. reduce(identity, BinaryOperator)：功能同上，但是使用 identity 作为其组合的初始值。
    因此如果流为空，identity 就是结果。
3. reduce(identity, BiFunction, BinaryOperator)：更复杂的使用形式（暂不介绍），
    这里把它包含在内，因为它可以提高效率。
    通常，我们可以显式地组合 map() 和 reduce() 来更简单的表达它。
 */
class Frobnitz {
    int size;
    Frobnitz(int size) {
        this.size = size;
    }
    @Override
    public String toString() {
        return "Frobnitz{" + "size=" + size + '}';
    }

    static Random rand = new Random(47);
    static final int BOUND = 100;
    /**
     * 这是一个与 Supplier<Frobnitz> 是签名兼容的生成器方法
     * （这种签名兼容性被称作结构一致性）
     */
    static Frobnitz supply() {
        return new Frobnitz(rand.nextInt(BOUND));
    }
}
class Reduce {
    public static void main(String[] args) {
        Stream.generate(Frobnitz::supply)
                .limit(10)
                .peek(System.out::println)
                .reduce((fr0, fr1) -> fr0.size < 50 ? fr0 : fr1)
                .ifPresent(System.out::println);
        /*
        无“初始值”的 reduce()方法返回值是 Optional 类型。
        Optional.ifPresent() 只有在结果非空的时候才会调用 Consumer<Frobnitz>。

        Lambda 表达式中的第一个参数 fr0 是上一次调用 reduce() 的结果。
        而第二个参数 fr1 是从流传递过来的值。

        reduce() 中的 Lambda 表达式使用了三元表达式来获取结果，
        当其长度小于 50 的时候获取 fr0 否则获取序列中的下一个值 fr1。
        当取得第一个长度小于 50 的 Frobnitz，只要得到结果就会忽略流传递进来的值。
        这是个非常奇怪的约束， 也确实让我们对 reduce() 有了更多的了解。
         */
    }
}



/*
【匹配】
1. allMatch(Predicate)
    如果流的每个元素根据提供的 Predicate 都返回 true 时，结果返回为 true。
    在第一个 false 时，则停止执行计算。
2. anyMatch(Predicate)
    如果流中的任意一个元素根据提供的 Predicate 返回 true 时，结果返回为 true。
    在第一个 false 是停止执行计算。
3. noneMatch(Predicate)
    如果流的每个元素根据提供的 Predicate 都返回 false 时，结果返回为 true。
    在第一个 true 时停止执行计算。

上述方法都是有短路行为的。
 */
class Matching {
    static void show(BiPredicate<Stream<Integer>, Predicate<Integer>> match, int val) {
        System.out.println(match.test(
                IntStream.rangeClosed(1, 9)
                        .boxed()
                        .peek(n -> System.out.format("%d ", n)),
                n -> n < val));
    }

    public static void main(String[] args) {
        show(Stream::allMatch, 10);
        show(Stream::allMatch, 4);
        show(Stream::anyMatch, 6);
        show(Stream::anyMatch, 0);
        show(Stream::noneMatch, 6);
        show(Stream::noneMatch, 0);
    }
}



/*
【查找】
1. findFirst()：返回第一个流元素的 Optional，如果流为空返回 Optional.empty。
2. findAny()：返回含有任意流元素的 Optional，如果流为空返回 Optional.empty。
 */
class SelectElement {
    public static void main(String[] args) {
        System.out.println(RandInts.rands()
                .findFirst()
                .getAsInt()); // out: 258
        System.out.println(RandInts.rands().parallel().
                findFirst()
                .getAsInt()); // out: 258
        /*
        findFirst() 无论流是否为并行化的，总是会选择流中的第一个元素。
         */

        System.out.println(RandInts.rands()
                .findAny()
                .getAsInt()); // out: 258
        System.out.println(RandInts.rands().parallel()
                .findAny()
                .getAsInt()); // out: 402
        /*
        对于非并行流，findAny()会选择流中的第一个元素（即使从定义上来看是选择任意元素）。
        在这个例子中，我们使用 parallel() 来并行流从而引入 findAny() 选择非第一个流元素的可能性。
         */
    }
}

/*
如果必须选择流中最后一个元素，那就使用 reduce()。
如下：
 */
class LastElement {
    public static void main(String[] args) {
        OptionalInt last = IntStream.range(10, 20).reduce((n1, n2) -> n2);
        System.out.println(last.orElse(-1));

        Optional<String> lastObj = Stream.of("one", "two", "three").reduce((n1, n2) -> n2);
        System.out.println(lastObj.orElse("Nothing!"));
    }
}
/*
reduce() 的参数只是用最后一个元素替换了最后两个元素，最终只生成最后一个元素。
如果为数字流，你必须使用相近的数字 Optional 类型（ numeric optional type），
否则使用 Optional 类型，就像上例中的 Optional<String>。
 */



/*
【信息】
1. count()：流中的元素个数。
2. max(Comparator)：根据所传入的 Comparator 所决定的“最大”元素。
3. min(Comparator)：根据所传入的 Comparator 所决定的“最小”元素。
 */
class Informational {
    public static void main(String[] args) {
        System.out.println(FileToWords.stream()
                .count());
        System.out.println(FileToWords.stream()
                .min(String.CASE_INSENSITIVE_ORDER)
                .orElse("NONE"));
        System.out.println(FileToWords.stream()
                .max(String.CASE_INSENSITIVE_ORDER)
                .orElse("NONE"));
    }
}
/*
min() 和 max() 的返回类型为 Optional，这需要我们使用 orElse()来解包。
 */



/*
【数字流信息】
1. average() ：求取流元素平均值。
2. max() 和 min()：数值流操作无需 Comparator。
3. sum()：对所有流元素进行求和。
4. summaryStatistics()：生成可能有用的数据。
    目前并不太清楚这个方法存在的必要性，因为我们其实可以用更直接的方法获得需要的数据。
 */
class NumericStreamInfo {
    public static void main(String[] args) {
        System.out.println(RandInts.rands()
                .average()
                .getAsDouble());
        System.out.println(RandInts.rands()
                .max()
                .getAsInt());
        System.out.println(RandInts.rands()
                .min()
                .getAsInt());
        System.out.println(RandInts.rands()
                .sum());
        System.out.println(RandInts.rands()
                .summaryStatistics());
    }
}
/*
上例操作对于 LongStream 和 DoubleStream 同样适用。
 */
