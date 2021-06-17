import java.text.DateFormat;
import java.util.Date;
import java.util.EnumSet;

/*
Java 的 enum 有一个非常有趣的特性，
即它允许程序员为 enum 实例编写方法，
从而为每个 enum 实例赋予各自不同的行为。

如果要实现常量相关的方法，
需要为 enum 定义一个或多个 abstract 方法，
然后为每个 enum 实例实现该抽象方法。

如下，
 */
enum ConstantSpecificMethod {
    DATE_TIME {
        @Override
        String getInfo() {
            return DateFormat.getDateInstance().format(new Date());
        }
    },
    CLASSPATH {
        @Override
        String getInfo() {
            return System.getenv("CLASSPATH");
        }
    },
    VERSION {
        @Override
        String getInfo() {
            return System.getProperty("java.version");
        }
    };

    abstract String getInfo();

    public static void main(String[] args) {
        for (ConstantSpecificMethod csm : values()) {
            System.out.println(csm.getInfo());
        }
    }
}
/*
通过相应的 enum 实例，可以调用上述的抽象方法。
这通常也称为 表驱动的代码（table-driven code）[可以与命令模式关联对比]

在面向对象的程序设计中，不同的行为与不同的类关联。
而通过常量相关的方法，
----
每个 enum 实例可以具备自己独特的行为，
这似乎说明每个 enum 实例就像一个独特的类。
----
在上面的例子中，
enum 实例似乎被当作其“超类”ConstantSpecificMethod 来使用，
在调用 getInfo() 方法时，体现出多态的行为。
 */

/**
 * 然而，enum 实例与类的相似之处也仅限于此了。
 * 并不能真的将 enum 实例作为一个类型来使用：
 */
enum LikeClasses {
    WINKEN {
        @Override
        void behavior() {
            System.out.println("WINKEN.behavior1");
        }
    },
    BLINKEN {
        @Override
        void behavior() {
            System.out.println("BLINKEN.behavior2");
        }
    };

    abstract void behavior();
}
class NotClass {
    // void f1(LikeClasses.WINKEN instance) {}
}
/*
因为每个 enum 元素都是一个 LikeClasses 类型的 static final 实例，
所以编译器不允许我们将一个 enum 实例当作 class 类型。

同时，由于 enum 元素是 static 实例，所以无法访问外部类的非 static 元素或方法。
 */

/**
 * 在看一个洗车的例子：
 * 每个顾客在洗车时，都有一个选择菜单，每个选择对应一个不同的动作。
 * 可以将一个常量相关的方法关联到一个选择上，
 * 再使用一个 EnumSet 来保存客户的选择：
 */
class CarWash {
    public enum Cycle {
        UNDERBODY {
            @Override
            void action() {
                System.out.println("Spraying the underbody");
            }
        },
        WHEELWASH {
            @Override
            void action() {
                System.out.println("Washing the wheels");
            }
        },
        PREWASH {
            @Override
            void action() {
                System.out.println("Loosening the dirt");
            }
        };

        abstract void action();
    }

    EnumSet<Cycle> cycles = EnumSet.of(Cycle.PREWASH);

    public void add(Cycle cycle) {
        cycles.add(cycle);
    }
    public void washCar() {
        for (Cycle cycle : cycles) {
            cycle.action();
        }
    }

    @Override
    public String toString() {
        return "CarWash{" +
                "cycles=" + cycles +
                '}';
    }

    public static void main(String[] args) {
        CarWash wash = new CarWash();
        System.out.println(wash);
        wash.washCar();

        wash.add(Cycle.PREWASH);
        wash.add(Cycle.PREWASH);
        wash.add(Cycle.PREWASH);
        wash.add(Cycle.UNDERBODY);
        wash.add(Cycle.WHEELWASH);
        System.out.println(wash);
        wash.washCar();
    }
}
/*
与使用匿名内部类相比较，定义常量相关方法的语法更高效、简洁。

上述的例子也展示了 EnumSet 的一些特性：
因为它是一个集合，
所以对于同一个元素而言，只能出现一次，
因此对同一个参数重复地调用 add() 方法会被忽略掉
（这是正确的行为，因为一个 bit 位开关只能“打开”一次）

同样地，
向 EnumSet 添加 enum 实例的顺序并不重要，
因为其输出的次序决定于 enum 实例定义时的次序。
 */

/*
Q：除了实现 abstract 方法以外，程序员是否可以覆盖常量相关的方法呢？
A：答案是肯定的，如下：
 */
enum OverrideConstantSpecific {
    NUT, BOLT,
    WASHER {
        @Override
        void f() {
            System.out.println("Overridden method");
        }
    };

    void f() {
        System.out.println("default behavior");
    }

    public static void main(String[] args) {
        for (OverrideConstantSpecific ocs : values()) {
            System.out.print(ocs + ": ");
            ocs.f();
        }
    }
}
/*
虽然 enum 有某些限制，但是一般而言，我们还是可以将其看作是类。
 */
