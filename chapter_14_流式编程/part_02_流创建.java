import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
 */


