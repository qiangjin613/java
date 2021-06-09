/*
函数组合（Function Composition）意为“多个函数组合成新函数”。
它通常是函数式编程的基本组成部分。
 */

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/*
常用的有：
andThen(argument)、compose(argument)
and(argument)、or(argument)、negate()
 */

/**
 * andThen(argument)、compose(argument) 示例：
 */
class FunctionComposition {
    static Function<String, String>
            f1 = s -> {
                    System.out.println("f1: " + s);
                    return s.replace('A', '_'); },
            f2 = s -> {
                    System.out.println("f2: " + s);
                    return s.substring(3); },
            f3 = s -> {
                    System.out.println("f3: " + s);
                    return s.toLowerCase(); },
            /* 组合了操作 f1、f2、f3：f2 --> f1 --> f3 */
            f4 = f1.compose(f2).andThen(f3);

    public static void main(String[] args) {
        System.out.println(f4.apply("GO AFTER ALL AMBULANCES"));
    }
}

/**
 * and(argument)、or(argument)、negate() 示例：
 */
class PredicateComposition {
    static Predicate<String>
            p1 = s -> s.contains("bar"),
            p2 = s -> s.length() < 5,
            p3 = s -> s.contains("foo"),
            /*
            执行顺序从左往右：p1 --> p2 --> p3，等价于：
            if ((!s.contains("bar") && s.length() < 5) || s.contains("foo"))
                return true;
             */
            p4 = p1.negate().and(p2).or(p3);

    public static void main(String[] args) {
        Stream.of("bar", "foobar", "foobaz", "fongopuckey")
                .filter(p4)
                .forEach(System.out::println);
    }
}
