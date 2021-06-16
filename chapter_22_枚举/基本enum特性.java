/*
枚举，Java 5 添加了一个看似很小的 enum 关键字。

以前，需要创建一个常量集（常量类），
但是这些值并不会将自身限制在这个常量集的范围内，
因此使用它们更有风险，而且更难使用。
Java 程序员必须了解许多细节并格外仔细地去达成 enum 的效果。

现在，使用群组 & 使用枚举类型时使用，可以将这些值限制在一个范围内。
并且它的功能比 C/C++ 中的完备得多。
 */


/*
简单的例子：
 */
enum Spiciness {
    /* 枚举类型的实例是常量，因此按照命名惯例，它们都用 大写字母+下划线 表示 */
    NOT, MILD, MEDIUM, HOT, FLAMING
}
class SimpleEnumUse {
    public static void main(String[] args) {
        Spiciness howHot = Spiciness.MEDIUM;
        System.out.println(howHot);  // out: MEDIUM
    }
}

/*
在创建 enum 时，编译器会自动添加一些有用的特性：
1. toString()  -->  name
2. ordinal()   --> ordinal
3. static values()
 */
class EnumOrder {
    public static void main(String[] args) {
        /* .values() 方法返回 enum 实例的数组 */
        for (Spiciness s : Spiciness.values()) {
            System.out.println(s + ", ordinal " + s.ordinal());
        }
    }
}

/*
尽管 enum 看起来像是一种新的数据类型，
但是这个关键字只是在生成 enum 的类时，产生了某些编译器行为，
因此在很大程度上你可以将 enum 当作其他任何类。
（事实上，enum 确实是类，并且具有自己的方法）
 */

/*
【switch 语句中的 enum】
enum 还有一个很实用的特性，就是在 switch 中使用：
 */
class Burrito {
    Spiciness degree;

    public Burrito(Spiciness degree) {
        this.degree = degree;
    }

    public void describe() {
        System.out.println("This burrito is ");
        switch (degree) {
            case NOT:
                System.out.println("NOT");
                break;
            case MILD:
                System.out.println("MILD");
                break;
            case HOT:
            case FLAMING:
            case MEDIUM:
            default:
                System.out.println("maybe too hot");
        }
    }

    public static void main(String[] args) {
        Burrito plain = new Burrito(Spiciness.MILD),
                greenChile = new Burrito(Spiciness.MEDIUM),
                jalapeno = new Burrito(Spiciness.HOT);
        plain.describe();
        greenChile.describe();
        jalapeno.describe();
    }
}
/*
由于 switch 是在有限的可能值集合中选择，
因此它与 enum 是绝佳的组合。
 */



/*
创建 enum 时，
编译器会为你生成一个相关的类，
这个类继承自 Java.lang.Enum。

再次演示了 Enum 类提供的一些功能：
（Enum 类实现了 Comparable 接口，所以它具有 compareTo() 方法。
同时，它还实现了 Serializable 接口。）
 */
enum Shrubbery {
    GROUND, CRAWLING, HANGING
}
class EnumClass {
    public static void main(String[] args) {
        for (Shrubbery s : Shrubbery.values()) {
            System.out.println(s + ", ordinal: " + s.ordinal());
            System.out.print(s.compareTo(Shrubbery.GROUND) + " ");
            System.out.print(s.equals(Shrubbery.GROUND) + " ");
            System.out.println(s == Shrubbery.GROUND);
            System.out.println(s.getDeclaringClass());
            System.out.println(s.name());
            System.out.println("*********************");
        }
        for (String s : "GROUND CRAWLING HANGING".split(" ")) {
            Shrubbery shrub = Enum.valueOf(Shrubbery.class, s);
            System.out.println(shrub);
        }
    }
}
