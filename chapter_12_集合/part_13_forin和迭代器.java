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

 */
















