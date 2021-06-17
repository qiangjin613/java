import java.util.Random;
import java.util.function.Supplier;

/*
先来看一个枚举类：
enum Spiciness {
    NOT, MILD, MEDIUM, HOT, FLAMING
}
经编译器编译后：
Compiled from "基本enum特性.java"
final class Spiciness extends java.lang.Enum<Spiciness> {
  public static final Spiciness NOT;
  public static final Spiciness MILD;
  public static final Spiciness MEDIUM;
  public static final Spiciness HOT;
  public static final Spiciness FLAMING;
  private static final Spiciness[] $VALUES;
  public static Spiciness[] values();
  public static Spiciness valueOf(java.lang.String);
  private Spiciness();
  static {};
}

可以看到，enum 都继承自 Java.lang.Enum 类。
由于 Java 不支持多重继承，所以你的 enum 不能再继承其他类，
然而，在创建一个新的 enum 时，可以实现多个接口。
 */
enum CartoonCharacter implements Supplier<CartoonCharacter> {
    SLAPPY, SPANKY, PUNCHY,
    SILLY, BOUNCY, NUTTY, BOB;

    private Random rand = new Random(47);

    @Override
    public CartoonCharacter get() {
        return values()[rand.nextInt(values().length)];
    }
}

class EnumImplementation {
    public static <T> void printNext(Supplier<T> rg) {
        System.out.println(rg.get() + ", ");
    }

    public static void main(String[] args) {
        CartoonCharacter cc = CartoonCharacter.BOB;
        for (int i = 0; i < 10; i++) {
            printNext(cc);
        }
    }
}
