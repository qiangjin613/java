import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 创建一个后面都要用到的辅助类
 */
class Bubble {
    public final int i;
    public Bubble(int n) {
        i = n;
    }
    private static int count = 0;
    public static Bubble bubbler() {
        return new Bubble(count++);
    }
    @Override
    public String toString() {
        return "Bubble{" + "i=" + i + '}';
    }
}

/*
【流创建】
1. 通过 Stream.of() 将一组元素转为流。
2. 每个集合都可以调用 stream() 产生一个流。
 */
class StreamOf {
    public static void main(String[] args) {
        Stream.of(new Bubble(1), new Bubble(2), new Bubble(3))
                .forEach(System.out::println);
        Stream.of("It's ", "a ", 12, "Wonderful").forEach(System.out::println);
    }
}
class CollectionToStream {
    public static void main(String[] args) {
        List<Bubble> bubbles = Arrays.asList(new Bubble(1), new Bubble(2), new Bubble(3));
        System.out.println(bubbles.stream()
                .mapToInt(b -> b.i)
                .sum());

        Set<String> strs = new HashSet<>(Arrays.asList("It's a wonderful day for pie!".split(" ")));
        strs.stream()
                .map(x -> x + " ")
                .forEach(System.out::println);

        Map<String, Double> map = new HashMap<>();
        map.put("pi", 3.14159265);
        map.put("e", 2.71828);
        map.put("phi", 1.618);
        map.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .forEach(System.out::println);
    }
}




/*
【随机数流】
Random 类被一组生成流的方法增强了。

注：Random 类只能生成基本类型 int， long， double 的流。
幸运的是， boxed() 流操作将会自动地把基本类型包装成为对应的装箱类型，
从而使得 show() 能够接受流。
 */
class RandomGenerators {
    public static <T> void show(Stream<T> stream) {
        stream
                .limit(4)
                .forEach(System.out::println);
        System.out.println("--------------------");
    }

    public static void main(String[] args) {
        Random rand = new Random(47);
        show(rand.ints().boxed());
        show(rand.doubles().boxed());
        show(rand.longs().boxed());
        /* 控制流大小 */
        show(rand.ints(2).boxed());
        show(rand.doubles(3).boxed());
        show(rand.longs(4).boxed());
        /* 控制上限和下限，范围：[a, b)   */
        show(rand.ints(4, 10).boxed());
        show(rand.doubles(6, 10).boxed());
        show(rand.longs(8, 10).boxed());
        /* 控制流的大小和界限 */
        show(rand.ints(1, 4, 10).boxed());
        show(rand.doubles(2, 6, 10).boxed());
        show(rand.longs(3, 8, 10).boxed());
    }
}
/*
rand.ints()、rand.doubles() 和 rand.longs()
分别产生 IntStream、DoubleStream 和 LongStream。
通过 XxxStream 的 boxed() 生成对应基本类型包装成为对应的装箱类型的流。
 */


/*
【进阶：使用 Stream.generate() 生成流】
Stream.generate() 可以把任意 Supplier<T> 用于生成 T 类型的流。

下面使用 Random 为任意对象集合创建 Supplier 的例子：
 */
class RandomWords implements Supplier<String> {
    List<String> words = new ArrayList<>();
    Random rand = new Random(47);
    public RandomWords() {
        words.addAll(Arrays.asList("Not much of a cheese shop really, is it?".split(" ")));
    }

    @Override
    public String get() {
        return words.get(rand.nextInt(words.size()));
    }

    @Override
    public String toString() {
        return words.stream().collect(Collectors.joining(" "));
        // 等效于：
        // return String.join(" ", words);
    }

    public static void main(String[] args) {
        System.out.println(
                Stream.generate(new RandomWords())
                        .limit(10)
                        .collect(Collectors.joining(" "))
        );
    }
}



/*
【int 类型的范围】
IntStream 类提供了 range() 方法用于生成整型序列的流。
编写循环时，这个方法会更加便利。
下面使用三种方法进行对比：
 */
class Ranges {
    public static void main(String[] args) {
        // 传统方法：
        int result = 0;
        for (int i = 10; i < 20; i++) {
            result += i;
        }
        System.out.println(result);

        // for-in 循环：
        result = 0;
        for (int i : IntStream.range(10, 20).toArray()) {
            result += i;
        }
        System.out.println(result);

        // 使用流：
        System.out.println(IntStream.range(10, 20).sum());
    }
}

/**
 * 使用流来代替简单的 for 循环：
 */
class Repeat {
    public static void repeat(int n, Runnable action) {
        IntStream.range(0, n).forEach(i -> action.run());
    }
}

class Looping {
    static void hi() {
        System.out.println("Hi!");
    }

    public static void main(String[] args) {
        Repeat.repeat(3, () -> System.out.println("Looping!"));
        Repeat.repeat(2, Looping::hi);
    }
}
/*
在这个上面使用 repeat 这样的方法并不显得简洁，但这也是一个相当透明的工具。
 */





/*
【Stream.generate() 的再次解析】

 */
class Generator implements Supplier<String> {
    Random rand = new Random(47);
    char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    @Override
    public String get() {
        return "" + letters[rand.nextInt(letters.length)];
    }

    public static void main(String[] args) {
        String word = Stream.generate(new Generator())
                .limit(30)
                .collect(Collectors.joining());
        System.out.println(word);
    }
}
/**
 * 创建相同对象的流：
 */
class Duplicator {
    public static void main(String[] args) {
        Stream.generate(() -> "duplicate")
                .limit(3)
                .forEach(System.out::println);
    }
}

/**
 * 介绍一种单独创建工厂的形式：
 */
class Bubble2 {
    public final int i;
    public Bubble2(int n) {
        i = n;
    }

    @Override
    public String toString() {
        return "Bubble2{" + "i=" + i + '}';
    }
    private static int count = 0;
    public static Bubble2 bubble2() {
        return new Bubble2(count++);
    }

    public static void main(String[] args) {
        Stream.generate(Bubble2::bubble2)
                .limit(2)
                .forEach(System.out::println);
    }
}
/*
这是创建单独工厂类（Separate Factory class）的另一种方式。
在很多方面它更加整洁，
但是这是一个对于代码组织和品味的问题——因为总是可以创建一个完全不同的工厂类。
 */


/*
【Stream.iterate() 的使用】
Stream.iterate() 以种子（第一个参数）开头，并将其传给方法（第二个参数）。
方法的结果将添加到流，并存储作为第一个参数用于下次调用 iterate()，依次类推。
我们可以利用 iterate() 生成一个斐波那契数列。
如下：
 */
class Fibonacci {
    int x = 1;
    Stream<Integer> numbers() {
        return Stream.iterate(0, i -> {
            int result = x + i;
            x = i;
            return result;
        });
    }

    public static void main(String[] args) {
        new Fibonacci().numbers()
                .skip(3)  // 过滤前 3 个
                .limit(10)  // 取 10 个
                .forEach(System.out::println);
    }
}


/*
【流的建造者模式】

 */
class FileToWordsBuilder {
    Stream.Builder<String> builder = Stream.builder();

    /* 在构造器中向 builder 中添加单词 */
    public FileToWordsBuilder() {
        String line = "// streams/Cheese.dat\n" +
                "Not much of a cheese shop really, is it?\n" +
                "Finest in the district, sir.\n" +
                "And what leads you to that conclusion?\n" +
                "Well, it's so clean.\n" +
                "It's certainly uncontaminated by cheese.";
        for (String w : line.split("[ .?,]+")) {
            builder.add(w);
        }
    }

    Stream<String> stream() {
        return builder.build();
    }

    public static void main(String[] args) {
        new FileToWordsBuilder()
                .stream()
                .limit(7)
                .map(w -> w + " ")
                .forEach(System.out::print);
    }
}




/*
【Arrays】
Arrays 类中含有一个名为 stream() 的静态方法用于把数组转换成为流。
 */
class Machine2 {
    public static void main(String[] args) {
        /* new Operations[] 表达式动态创建了 Operations 对象的数组 */
        Arrays.stream(new Operations[] {
                () -> Operations.show("Bing"),
                () -> Operations.show("Crack"),
                () -> Operations.show("Twist")
        }).forEach(Operations::execute);
    }
}

/*
stream() 同样可以产生 IntStream，LongStream 和 DoubleStream。
Arrays.stream() 有多个重载方法来获取各种 XxxStream 对象
 */
class ArraysStream {
    public static void main(String[] args) {
        Arrays.stream(new double[] { 3.12, 324.32, 4.32 })
                .forEach( n -> System.out.format("%f ", n));
        System.out.println();

        Arrays.stream(new int[] { 1, 3, 5 })
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();

        Arrays.stream(new long[] { 11, 22, 44, 66 })
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();

        /* 索引位置：[startIndex, endIndex) */
        Arrays.stream(new int[] {1, 2, 3, 4, 5, 6}, 3, 6)
                .forEach(n -> System.out.format("%d ", n));
    }
}



/*
【正则表达式】
 */















