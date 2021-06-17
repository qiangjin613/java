import java.util.Random;

/*
这里提供一个随机选择的工具类：
利用泛型，从而使得这个工作更一般化
从 enum 实例中进行随机选择。
 */
class Enums {
    private static Random rand = new Random(47);

    /* <T extends Enum<T>> 表示 T 是一个 enum 实例 */
    public static <T extends Enum<T>> T random(Class<T> c) {
        return random(c.getEnumConstants());
    }

    public static <T> T random(T[] values) {
        return values[rand.nextInt(values.length)];
    }
}

enum Activity {
    SITTING, LYING, STANDING, HOPPING,
    RUNNING, DODGING, JUMPING, FALLING, FLYING
}
class RandomTest {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(Enums.random(Activity.class) + " ");
        }
    }
}
