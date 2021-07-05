import java.util.*;

/*
for-in 语法主要用于数组，但它也适用于任何 Collection 对象。
先看一个通用的证明：
 */
class ForInCollections {
    public static void main(String[] args) {
        Collection<String> cs = new LinkedList<>();
        Collections.addAll(cs, "Take the long way home".split(" "));
        for (String s : cs) {
            System.out.print("'" + s + "' ");
        }
    }
}
/*
由于 cs 是一个 Collection，
因此，该代码展示了使用 for-in 是所有 Collection 对象的特征。
 */

/*
这样做的原因是 Java 5 引入了一个名为 Iterable 的接口，
该接口包含一个能够生成 Iterator 的 iterator() 方法。
for-in 使用此 Iterable 接口来遍历序列。
因此，如果创建了任何实现了 Iterable 的类，
都可以将它用于 for-in 语句中：
 */
class IterableClass implements Iterable<String> {
    protected String[] words = ("And that is how " +
            "we know the Earth to be banana-shaped."
    ).split(" ");

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < words.length;
            }

            @Override
            public String next() {
                return words[index++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        for (String s : new IterableClass()) {
            System.out.print("'" + s + "' ");
        }
    }
}

/*
在 Java 5 中，许多类都是 Iterable，
主要包括所有的 Collection 类（但不包括各种 Maps ）。
例如，下面的代码可以显示所有的操作系统环境变量：
 */
class EnvironmentVariables {
    public static void main(String[] args) {
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

/*
for-in 语句适用于数组或其它任何 Iterable，
但这并不意味着数组肯定也是个 Iterable，
也不会发生任何自动装箱（这里指，不会自动将数组装箱变为 Iterable）：
 */
class ArrayIsNotIterable {
    static <T> void test(Iterable<T> ib) {
        for (T t : ib) {
            System.out.print(t + " ");
        }
    }

    public static void main(String[] args) {
        test(Arrays.asList(1, 2, 3));
        String[] strings = { "A", "B", "C" };
        // 数组在for-in中工作，但它不是Iterable:
        // test(strings);
        // 必须显式地将它转换为一个Iterable:
        test(Arrays.asList(strings));
    }
}
/*
尝试将数组作为一个 Iterable 参数传递会导致失败。
这说明不存在任何从数组到 Iterable 的自动转换;
必须手工执行这种转换。
 */



/*
【适配器方法惯用法】
如果现在有一个 Iterable 类，想要添加一种或多种在 for-in 语句中使用这个类的方法，应该怎么做呢？

例如，
你希望可以选择正向还是反向遍历一个单词列表。
如果直接继承这个类，并覆盖 iterator() 方法，则只能替换现有的方法，而不能实现遍历顺序的选择。

一种解决方案是所谓适配器方法（Adapter Method）的惯用法。
“适配器”部分来自于设计模式，因为必须要提供特定的接口来满足 for-in 语句。
如果已经有一个接口并且需要另一个接口时，则编写适配器就可以解决这个问题。

在这里，若希望在默认的正向迭代器的基础上，添加产生反向迭代器的能力，
因此不能使用覆盖，
相反，
而是添加了一个能够生成 Iterable 对象的方法，
该对象可以用于 for-in 语句。
这使得我们可以提供多种使用 for-in 语句的方式：
 */
class ReversibleArrayList<T> extends ArrayList<T> {
    ReversibleArrayList(Collection<T> c) {
        super(c);
    }
    public Iterable<T> reversed() {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    int current = size() - 1;
                    @Override
                    public boolean hasNext() {
                        return current > -1;
                    }

                    @Override
                    public T next() {
                        return get(current--);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
class AdapterMethodIdiom {
    public static void main(String[] args) {
        ReversibleArrayList<String> ral = new ReversibleArrayList<>(
                Arrays.asList("To be or not to be".split(" ")));
        /* 直接将 ral 对象放在 for-in 语句中，则会得到（默认的）正向迭代器 */
        for (String s : ral) {
            System.out.print(s + " ");
        }
        System.out.println();

        /* reversed() 方法，它会产生不同的行为 */
        for (String s : ral.reversed()) {
            System.out.print(s + " ");
        }
    }
}

/*
通过使用这种方式，可以在下面的示例中添加两种适配器方法：
 */
class MultiIterableClass extends IterableClass {
    /* 反向迭代器 */
    public Iterable<String> reversed() {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    int current = words.length - 1;
                    @Override
                    public boolean hasNext() {
                        return current > -1;
                    }

                    @Override
                    public String next() {
                        return words[current--];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
    /* 随机迭代器 */
    public Iterable<String> randomized() {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                List<String> shuffled = new ArrayList<>(Arrays.asList(words));
                Collections.shuffle(shuffled, new Random(47));
                return shuffled.iterator();
            }
        };
    }

    public static void main(String[] args) {
        MultiIterableClass mic = new MultiIterableClass();
        /* 使用默认的 Iterable */
        for (String s : mic) {
            System.out.print(s + " ");
        }
        System.out.println();

        /* 使用反向的 Iterable */
        for (String s : mic.reversed()) {
            System.out.print(s + " ");
        }
        System.out.println();

        /* 使用随机的 Iterable */
        for (String s : mic.randomized()) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
/*
从输出中可以看到，
Collections.shuffle() 方法不会影响到原始数组，而只是打乱了 shuffled 中的引用。
之所以这样，
是因为 randomized() 方法用一个 ArrayList 将 Arrays.asList() 的结果包装了起来。
如果这个由 Arrays.asList() 生成的 List 被直接打乱，
那么它将修改底层数组，如下所示：
 */
class ModifyingArraysAsList {
    public static void main(String[] args) {
        Random rand = new Random(47);
        Integer[] ia = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        /* 用一个 ArrayList 将 Arrays.asList() 的结果进行包装 */
        List<Integer> list1 = new ArrayList<>(Arrays.asList(ia));
        System.out.println("Before shuffling: " + list1);
        Collections.shuffle(list1, rand);
        System.out.println("After shuffling: " + list1);
        /* ia 并没有被影响 */
        System.out.println("array: " + Arrays.toString(ia));


        /* 直接使用 Arrays.asList(ia) 的结果 */
        List<Integer> list2 = Arrays.asList(ia);
        System.out.println("Before shuffling: " + list2);
        Collections.shuffle(list2, rand);
        System.out.println("After shuffling: " + list2);
        /* ia 顺序已然被打乱 */
        System.out.println("array: " + Arrays.toString(ia));
    }
}
/*
要注意：
Arrays.asList() 生成一个 List 对象，
该对象使用底层数组作为其物理实现。
如果执行的操作会修改这个 List，
并且不希望修改原始数组，那么就应该在另一个集合中创建一个副本。
 */
