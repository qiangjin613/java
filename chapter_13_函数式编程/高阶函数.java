/*
高阶函数（Higher-order Function）就是一个消费或产生函数的函数。
 */

import java.util.function.Function;

/**
 * 产生函数的高阶函数：
 */
// [1] 使用继承，为基类创建别名
interface FuncSS extends Function<String, String> {}
class ProduceFunction {
    // [2] produce() 是高阶函数
    static FuncSS produce() {
        // [3] 使用 Lambda 可以轻松地在方法中创建和返回一个函数（当然也可以使用方法引用）
        return s -> s.toLowerCase();
    }

    public static void main(String[] args) {
        FuncSS f = produce();
        System.out.println(f.apply("YELLING"));
    }
}

/**
 * 进行消费的高阶函数：
 * （消费函数需要在参数列表正确地描述函数类型）
 */
class Onee {}
class Twoo {}
class ConsumeFunction {
    static Twoo consume(Function<Onee, Twoo> onetwo) {
        return onetwo.apply(new Onee());
    }

    public static void main(String[] args) {
        Twoo two = consume(one -> new Twoo());
    }
}

/**
 * 当基于消费函数生成新函数时的场景：
 */
class I {
    @Override
    public String toString() {
        return "I";
    }
}
class O {
    @Override
    public String toString() {
        return "O";
    }
}
class TransformFunction {
    /*
    在这里，transform() 生成一个与传入的函数具有相同签名的函数，
    但是你可以生成任何你想要的类型。

    transform() 产生的是一个新函数，它将 in 的动作与 andThen() 参数的动作结合起来
     */
    static Function<I, O> transform(Function<I, O> in) {
        /* 在调用 in 函数之后调用 andThen()（看源码） */
        return in.andThen(o -> {
            System.out.println(o);
            return o;
        });
    }

    public static void main(String[] args) {
        Function<I, O> f2 = transform(i -> {
            System.out.println(i);
            return new O();
        });

        O o = f2.apply(new I());
    }
}
