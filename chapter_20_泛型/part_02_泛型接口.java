/*
【泛型接口】
泛型也可以应用于接口。例如 生成器，这是一种专门负责创建对象的类。
实际上，这是 工厂方法 设计模式的一种应用。
不过，当使用生成器创建新的对象时，它不需要任何参数，而工厂方法一般需要参数。
生成器无需额外的信息就知道如何创建新对象。

一般而言，一个生成器只定义一个方法，用于创建对象。
例如 java.util.function 类库中的 Supplier 就是一个生成器，调用其 get() 获取对象。
get() 是泛型方法，返回值为类型参数 T。
 */

import java.util.Iterator;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 为了演示 Supplier，我们需要定义几个类。
 * 下面是个咖啡相关的继承体系：
 */
class Coffee {
    private static long counter = 0;
    private final long id = counter++;

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + id;
    }
}
class Latte extends Coffee {}
class Mocha extends Coffee {}
class Cappuccino extends Coffee {}

/**
 * 我们可以编写一个类，实现 Supplier<Coffee> 接口，
 * 它能够随机生成不同类型的 Coffee 对象：
 */
class CoffeeSupplier implements Supplier<Coffee>, Iterable<Coffee> {
    private static Random rand = new Random(47);
    private Class<?>[] types = {Latte.class, Mocha.class, Cappuccino.class};
    private int size = 0;

    public CoffeeSupplier() {}
    public CoffeeSupplier(int sz) {
        size = sz;
    }

    @Override
    public Coffee get() {
        try {
            return (Coffee) types[rand.nextInt(types.length)].newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    class CoffeeIterator implements Iterator<Coffee> {
        int count = size;

        @Override
        public boolean hasNext() {
            return count > 0;
        }

        @Override
        public Coffee next() {
            count--;
            return CoffeeSupplier.this.get();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Coffee> iterator() {
        return new CoffeeIterator();
    }

    public static void main(String[] args) {
        // Supplier 接口：
        Stream.generate(new CoffeeSupplier())
                .limit(5)
                .forEach(System.out::println);

        // Iterable 接口：
        for (Coffee coffee : new CoffeeSupplier(5)) {
            System.out.println(coffee);
        }
    }
}
/*
output:
Cappuccino 0
Cappuccino 1
Mocha 2
Cappuccino 3
Mocha 4
Cappuccino 5
Mocha 6
Cappuccino 7
Latte 8
Mocha 9

 */


/**
 * 另一个实现 Supplier<T> 接口的例子，它负责生成 Fibonacci 数列：
 */
class Fibonacci2 implements Supplier<Integer> {
    private int count = 0;

    @Override
    public Integer get() {
        return fib(count++);
    }

    private int fib(int n) {
        if (n < 2)
            return 1;
        return fib(n - 2) + fib(n - 1);
    }

    public static void main(String[] args) {
        Stream.generate(new Fibonacci2())
                .limit(18)
                .map(n -> n + " ")
                .forEach(System.out::print);
    }
}
/*
虽然我们在 Fibonacci 类的里里外外使用的都是 int 类型，但是其参数类型却是 Integer。
这个例子引出了 Java 泛型的一个局限性：基本类型无法作为类型参数。
不过 Java 5 具备自动装箱和拆箱的功能，可以很方便地在基本类型和相应的包装类之间进行转换。
通过这个例子中 Fibonacci 类对 int 的使用，我们已经看到了这种效果。
 */



/*
如果还想更进一步，编写一个实现了 Iterable 的 Fibnoacci 生成器。
我们的一个选择是重写这个类，令其实现 Iterable 接口。
不过，你并不是总能拥有源代码的控制权，并且，除非必须这么做，否则，我们也不愿意重写一个类。
而且我们还有另一种选择，就是创建一个 适配器 (Adapter) 来实现所需的接口，
我们在前面介绍过这个设计模式。
 */

/**
 * 有多种方法可以实现适配器。
 * 例如，可以通过继承来创建适配器类：
 */
class IterableFibonacci extends Fibonacci2 implements Iterable<Integer> {
    private int n;

    public IterableFibonacci(int count) {
        n = count;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return n > 0;
            }

            @Override
            public Integer next() {
                n--;
                return IterableFibonacci.this.get();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        for (Integer integer : new IterableFibonacci(18)) {
            System.out.print(integer + " ");
        }
    }
}
