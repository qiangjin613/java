import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
在终端操作之前，考虑一个问题：
    如果在流中出现了一个空元素，或者在一个空流中取元素会发生什么？
    是中断、还是抛出异常？

是否有某种对象，可作为流元素的持有者，
即使查看的元素不存在也能友好地提示我们（也就是说，不会发生异常）？

Optional 可以实现这样的功能。
一些标准流操作返回 Optional 对象，因为它们并不能保证预期结果一定存在。
包括：
1.findFirst() 返回一个包含第一个元素的 Optional 对象，如果流为空则返回 Optional.empty
2.findAny() 返回包含任意元素的 Optional 对象，如果流为空则返回 Optional.empty
3.max() 和 min() 返回一个包含最大值或者最小值的 Optional 对象，如果流为空则返回 Optional.empty

4.reduce() 不再以 identity 形式开头，而是将其返回值包装在 Optional 中。
    （identity 对象成为其他形式的 reduce() 的默认结果，因此不存在空结果的风险）
5.对于数字流 IntStream、LongStream 和 DoubleStream，
    average() 会将结果包装在 Optional 以防止流为空。

下面对这些进行测试：
（在流中放置 null 是很好的中断方法）
 */
class OptionalsFromEmptyStreams {
    public static void main(String[] args) {
        /* findFirst() */
        System.out.println(Stream.<String>empty()
                .findFirst()); // out: Optional.empty
        /* findAny() */
        System.out.println(Stream.<String>empty()
                .findAny()); // out: Optional.empty
        /* max() and min() */
        System.out.println(Stream.<String>empty()
                .max(String.CASE_INSENSITIVE_ORDER)); // out: Optional.empty
        System.out.println(Stream.<String>empty()
                .min(String.CASE_INSENSITIVE_ORDER)); // out: Optional.empty
        /* reduce() */
        System.out.println(Stream.<String>empty()
                .reduce((s1, s2) -> s1 + s2)); // out: Optional.empty
        /* average() */
        System.out.println(IntStream.empty()
                .average()); // out: OptionalDouble.empty
    }
}
/*
根据结果显式，当流为空的时候会获得一个 Optional.empty 对象，而不是抛出异常。
Optional 拥有 toString() 方法可以用于展示有用信息。（正如上述的输出）
 */

/*
Optional 基本用法：
1. optionalObj.isPresent()
2. optionalObj.get()
 */
class OptionalBasics {
    static void test(Optional<String> optString) {
        /* 检查其中是否包含元素 */
        if (optString.isPresent()) {
            /* 获取元素 */
             System.out.println(optString.get());
        } else {
            System.out.println("Nothing inside!");
        }
    }

    public static void main(String[] args) {
        test(Stream.of("Epithets").findFirst());
        test(Stream.<String>empty().findFirst());
    }
}
/*
当接收到 Optional 对象时，应首先调用 isPresent() 检查其中是否包含元素。
如果存在，就可使用 get() 获取。
 */


/*
【便利函数】
Optional 中包含了许多方法，可以简化类似上述“对所包含的对象的检查和执行操作”的操作。
如：
1. ifPresent(Consumer)：当值存在时调用 Consumer，否则什么也不做。
2. orElse(otherObject)：如果值存在则直接返回，否则生成 otherObject。
3. orElseGet(Supplier)：如果值存在则直接返回，否则使用 Supplier 函数生成一个可替代对象。
4. orElseThrow(Supplier)：如果值存在直接返回，否则使用 Supplier 函数生成一个异常。
 */
class Optionals {
    static void ifPresent(Optional<String> optString) {
        optString.ifPresent(System.out::println);
    }
    static void orElse(Optional<String> optString) {
        System.out.println(optString.orElse("Nada"));
    }
    static void orElseGet(Optional<String> optString) {
        System.out.println(optString.orElse("Generated"));
    }
    static void orElseThrow(Optional<String> optString) {
        try {
            System.out.println(optString.orElseThrow(() -> new Exception("Supplied")));
        } catch (Exception e) {
            System.out.println("Catch: " + e);
        }
    }
    static void test(String testName, Consumer<Optional<String>> cos) {
        System.out.println("====== " + testName + " =======");
        cos.accept(Stream.of("Epithets").findFirst());
        cos.accept(Stream.<String>empty().findFirst());
    }

    public static void main(String[] args) {
        test("ifPresent", Optionals::ifPresent);
        test("orElse", Optionals::orElse);
        test("orElseGet", Optionals::orElseGet);
        test("orElseThrow", Optionals::orElseThrow);
    }
}


/*
【创建 Optional】
有 3 个静态方法可用于生成 Optional 对象：
1. empty()：生成一个空 Optional。
2. of(value)：将一个非空值包装到 Optional 里。（包装空值时抛出异常）
3. ofNullable(value)：针对一个可能为空的值，
    为空时自动生成 Optional.empty，否则将值包装在 Optional 中。
 */
class CreatingOptionals {
    static void test(String testName, Optional<String> opt) {
        System.out.println("====== " + testName + " =======");
        System.out.println(opt.orElse("Null"));
    }

    public static void main(String[] args) {
        test("empty", Optional.empty());
        try {
            test("of", Optional.of(null));
        } catch (Exception e) {
            System.out.println(e);
        }
        test("ofNullable", Optional.ofNullable("Hi"));
        test("ofNullable", Optional.ofNullable(null));
    }
}
/*
注意：不能通过传递 null 到 of() 来创建 Optional 对象。
最安全的方法是， 使用 ofNullable() 来优雅地处理 null。
 */


/*
【Optional 对象操作】
1. filter(Predicate)：将 Predicate 应用于 Optional 中的内容并返回结果。
    当 Optional 不满足 Predicate 时返回空。如果 Optional 为空，则直接返回。
2. map(Function)：如果 Optional 不为空，应用 Function 于 Optional 中的内容，并返回结果。
    否则直接返回 Optional.empty。
3. flatMap(Function)：同 map()，但是提供的映射函数将结果包装在 Optional 对象中，
    因此 flatMap() 不会在最后进行任何包装。
 */

/**
 * filter(Predicate) 使用
 */
class OptionalFilter {
    static String[] elements = {"Foo", "", "Bar", "Baz", "Bingo"};
    static Stream<String> testStream() {
        return Arrays.stream(elements);
    }
    static void test(String descr, Predicate<String> pred) {
        System.out.println(" ---( " + descr + " )---");
        for (int i = 0; i <= elements.length; i++) {
            System.out.println(testStream()
                    .skip(i)
                    .findFirst()
                    .filter(pred));
        }
        /*
        注意，不同于普通 for 循环，这里的索引值范围并不是 i < elements.length，
        而是 i <= elements.length。
        所以最后一个元素实际上超出了流。
        方便的是，这将自动成为 Optional.empty，可以在每一个测试的结尾中看到。
         */
    }

    public static void main(String[] args) {
        test("true", str -> true);
        test("false", str -> false);
        test("str != \"\"", str -> str != "");
        test("str.length == 3", str -> str.length() == 3);
        test("str.startsWith(\"B\")", str -> str.startsWith("B"));
    }
}


/**
 * map(Function) 使用
 *
 * 同 map() 一样，
 * Optional.map() 应用于函数。
 * 它仅在 Optional 不为空时才应用映射函数，
 * 并将 Optional 的内容提取到映射函数。
 */
class OptionalMap {
    static String[] elements = {"12", "", "23", "45"};
    static Stream<String> testStream() {
        return Arrays.stream(elements);
    }

    static void test(String descr, Function<String, String> func) {
        System.out.println(" ---( " + descr + " )---");
        for (int i = 0; i <= elements.length; i++) {
            System.out.println(testStream()
                    .skip(i)
                    .findFirst() // 产生一个 Optional
                    .map(func)); // 映射函数的返回结果会自动包装成为 Optional
        }
    }

    public static void main(String[] args) {
        test("Add brackets", s -> "[" + s + "]");
        test("Increment", s -> {
            try {
                return Integer.parseInt(s) + 1 + "";
            } catch (NumberFormatException e) {
                return s;
            }
        });
        test("Replace", s -> s.replace("2", "9"));
        test("Take last digit", s -> s.length() > 0? s.charAt(s.length() - 1) + "": s);
    }
}


/**
 * Optional 的 flatMap() 使用
 */
class OptionalFlatMap {
    static String[] elements = {"12", "", "23", "45"};
    static Stream<String> testStream() {
        return Arrays.stream(elements);
    }

    static void test(String descr, Function<String, Optional<String>> func) {
        System.out.println(" ---( " + descr + " )---");
        for (int i = 0; i <= elements.length; i++) {
            System.out.println(testStream()
                    .skip(i)
                    .findFirst() // 产生一个 Optional
                    .flatMap(func)); // 映射函数的返回结果会自动包装成为 Optional
        }
    }

    public static void main(String[] args) {
        test("Add brackets", s -> Optional.of("[" + s + "]"));
        test("Increment", s -> {
            try {
                return Optional.of(Integer.parseInt(s) + 1 + "");
            } catch (NumberFormatException e) {
                return Optional.of(s);
            }
        });
        test("Replace", s -> Optional.of(s.replace("2", "9")));
        test("Take last digit", s -> Optional.of(s.length() > 0? s.charAt(s.length() - 1) + "": s));
    }
}
/*
很显然 Optional.flatMap() 是为那些自己已经生成 Optional 的函数而设计的。
 */


/*
【Optional 流】
假设你的生成器可能产生 null 值，那么当用它来创建流时，
会自然地想到用 Optional 来包装元素。

如下：
 */
class Signal {
    private final String msg;
    public Signal(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
    @Override
    public String toString() {
        return "Signal{" + "msg='" + msg + '\'' + '}';
    }

    static Random rand = new Random(47);
    public static Signal morse() {
        switch (rand.nextInt(4)) {
            case 1: return new Signal("dot");
            case 2: return new Signal("dash");
            default: return null;
        }
    }
    public static Stream<Optional<Signal>> stream() {
        return Stream.generate(Signal::morse)
                .map(signal -> Optional.ofNullable(signal));
    }
}

class StreamOfOptionals {
    public static void main(String[] args) {
        Signal.stream()
                .limit(10)
                .forEach(System.out::println);
        System.out.println();
        Signal.stream()
                .limit(10)
                .filter(Optional::isPresent)  /* 我们使用 filter() 来保留那些非空 Optional */
                .map(Optional::get)  /* 使用 get() 获取元素 */
                .forEach(System.out::println);
    }
}
