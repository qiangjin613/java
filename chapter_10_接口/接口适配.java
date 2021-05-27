import java.nio.CharBuffer;
import java.util.Random;
import java.util.Scanner;

/*
接口最吸引人的原因之一是相同的接口可以有多个实现。

接口的一种常见用法是前面提到的策略设计模式。
可以说：“只要对象遵循接口，就可以调用方法”
 */

/**
 * 一个例子：
 * 类 Scanner 的构造器接受的是一个 Readable 接口，
 * Readable 没有用作 Java 标准库中其他任何方法的参数——它是单独为 Scanner 创建的，
 * 因此 Scanner 没有将其参数限制为某个特定类。
 * 通过这种方式，Scanner 可以与更多的类型协作。
 * 就是说，如果你创建了一个新类并想让 Scanner 作用于它，就让它实现 Readable 接口。
 */
class RandomString implements Readable {
    private static Random random = new Random(47);
    private static final char[] CAPITALS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LOWERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] VOWELS = "aeiou".toCharArray();
    private int count;

    public RandomString(int count) {
        this.count = count;
    }

    @Override
    public int read(CharBuffer cb){
        if (count-- == 0) {
            return -1;
        }
        cb.append(CAPITALS[random.nextInt(CAPITALS.length)]);
        for (int i = 0; i < 4; i++) {
            cb.append(VOWELS[random.nextInt(VOWELS.length)]);
            cb.append(LOWERS[random.nextInt(LOWERS.length)]);
        }
        cb.append(" ");
        return 10;
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(new RandomString(10));
        while (s.hasNext()) {
            System.out.println(s.next());
        }
    }
}

/*
考虑：假设有一个类没有实现 Readable 接口，怎样才能让 Scanner 作用于它呢？
可是这个也没有使用到 Scanner 呀！
 */
interface RandomDoubles {
    Random rand = new Random(47);
    default double next() {
        return rand.nextDouble();
    }
    static void main(String[] args) {
        RandomDoubles rd = new RandomDoubles() {};
        for (int i = 0; i < 7; i++) {
            System.out.println(rd.next() + "");
        }
    }
}

/*
使用接口实现 适配器模式
使用 一个既是 RandomDoubles，又是 Readable 的类
 */
class AdaptedRandomDoubles implements RandomDoubles, Readable {
    private int count;

    public AdaptedRandomDoubles(int count) {
        this.count = count;
    }

    @Override
    public int read(CharBuffer cb) {
        if (count-- == 0) {
            return -1;
        }
        String result = Double.toString(next()) + " ";
        cb.append(result);
        return result.length();
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(new AdaptedRandomDoubles(7));
        while (s.hasNextDouble()) {
            System.out.println(s.nextDouble() + "");
        }
    }
}
