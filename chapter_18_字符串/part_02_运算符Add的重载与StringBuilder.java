import java.util.Random;
import java.util.stream.Collectors;

/**
 * 不可变性会带来一定的效率问题（重载的 +、+= 用于拼接字符串就是一个例子）
 *
 * String 中的 + 与 += 是 Java 中仅有的两个重载过的操作符，
 * Java 不允许程序员重载任何其他的操作符。
 */
class Concatenation {
    public static void main(String[] args) {
        String mango = "mango";
        // [1]
        /* 如果按照这样子拼接，编译器不进行优化的前提下，需要新建 3 个对象 */
        String s = "abc" + mango + "def" + 47;
        System.out.println(s);
    }
}
/*
【编译器的自动性能优化】
上述代码中：编译器创建了一个 StringBuilder 对象，用于构建最终的 String，
并对每个字符串调用了一次 append() 方法，
最后调用 toString() 生成结果，并存为 s。
即：
new StringBuilder()
         .append("abc")
         .append("mango")
         .append("def")
         .append("47")
         .toString();
 */



/*
Q：既然编译器会自动进行性能优化，那是不是意味着可以随意的使用 String 对象？
A：看看下面的例子再说叭...
 */
class WitherStringBuilder {
    // [2] 编译器的优化不是完美的
    /*
    StringBuilder 是在循环内构造的，
    这意味着每进行一次循环，会创建一个新的 StringBuilder 对象
    */
    public String implicit(String[] fields) {
        String result = "";
        for (String field : fields) {
            result += field;
        }
        return result;
    }
    // [3] 使用 StringBuilder 的情况
    /*
    编译器只生成一个 StringBuilder 对象，
    显式创建 StringBuilder 对象时，还允许预先为 StringBuilder 指定大小（可以避免频繁地重新分配缓冲）
    */
    public String explicit(String[] fields) {
        StringBuilder result = new StringBuilder();
        for (String field : fields) {
            result.append(field);
        }
        return result.toString();
    }
}



/*
Q：如何合理地利用编译器的自动优化？
A：如果字符串操作比较简单，那就可以信赖编译器，它会合理地构造最终的字符串结果。
   但是，如果要在循环中使用拼接字符传，而且可能有性能问题，
   那么最好显示创建一个 StringBuilder 对象，用它来构建最终结构。
 */



/**
 * 一个比较好的例子：
 */
class UsingStringBuilder {
    public static String string1() {
        Random rand = new Random(47);
        // [4] 循环中拼接字符串，使用 StringBuilder
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < 15; i++) {
            result.append(rand.nextInt(100));
            result.append(", ");

            /*
            这样的“捷径”会让编译器调入陷阱，
            在 rand.nextInt(100) + ", " 处，
            会创建另一个 StringBuilder 处理括号内的字符串操作
             */
            // result.append(rand.nextInt(100) + ", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append("]");
        return result.toString();
    }
    public static String string2() {
        // [5] 使用了 Stream，这样代码更加简洁美观。
        // 可以证明，Collectors.joining() 内部也是使用的 StringBuilder，这种写法不会影响性能！
        return new Random(47)
                .ints(15, 0, 100)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
    }

    public static void main(String[] args) {
        System.out.println(string1());
        System.out.println(string2());
    }
}
