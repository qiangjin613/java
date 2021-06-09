import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

/*
柯里化（Currying）意为：将一个多参数的函数，转换为一系列单参数函数。
 */
class CurryingAndPartials {
    // [1] 未柯里化的函数：
    static String uncurried(String a, String b) {
        return a + b;
    }

    public static void main(String[] args) {
        // [2] 柯里化的函数：
        Function<String, Function<String, String>> sum = a -> b -> a + b;
        /* 等价于： */
        Function<String, Function<String, String>> sum2 = a -> { return b -> {return a + b;}; };

        /*
        现在有了一个“带参函数”和剩下的 “自由函数”（free argument）。
        相当于计算 f(x, y)，现在有了 f(1, y)
        柯里化可以看作是逐步消元的过程。
        （使用了闭包类似于高阶函数）

        Function<"a", Function<String, String>> sum = "a" -> b -> "a" + b;
        Function<"a", Function<String, String>> 的最后一个 String 就是 a + b 的结果的类型
         */
        Function<String, String> hi = sum.apply("a ");
        System.out.println(hi.apply("b"));

        // [3] 部分应用：
        Function<String, String> sumHi = sum.apply("Hup ");
        System.out.println(sumHi.apply("Ho "));
        System.out.println(sumHi.apply("Hey "));
    }
}

/**
 * 乘胜追击，再来柯里化一个三参数函数：
 */
class Curry3Args {
    public static void main(String[] args) {
        Function<String, Function<String, Function<String, String>>> sum = a -> b -> c -> a + b + c;
        Function<String, Function<String, String>> hi = sum.apply("a ");
        Function<String, String> ho = hi.apply("b ");
        System.out.println(ho.apply("c"));
    }
}

/*
处理基本类型和装箱时，使用适当的函数式接口：
 */
class CurriedIntAdd {
    public static void main(String[] args) {
        IntFunction<IntUnaryOperator> curriedIntAdd = a -> b -> a + b;

        IntUnaryOperator add4 = curriedIntAdd.apply(4);
        System.out.println(add4.applyAsInt(5));
    }
}
