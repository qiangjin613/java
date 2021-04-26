import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import java.nio.channels.ClosedSelectorException;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 【类加载机制】检查类加载器工作方式
 */
class Cookie {
    static {
        System.out.println("Loading Cookie");
    }
}
class Gum {
    static {
        System.out.println("Loading Gum");
    }
}
class Candy {
    static {
        System.out.println("Loading Candy");
    }
}
class SweetShop {
    public static void main(String[] args) {
        System.out.println("inside main");
        new Candy();
        System.out.println("After creating Candy");
        try {
            Class.forName("Gum");
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't find Gum");
        }
        System.out.println("After Class.forName(\"Gum\")");
        new Cookie();
        System.out.println("After creating Cookie");
    }
}
/*
Java 程序在它开始之前并没有被完全加载，很多部分在需要的时候才会加载
 */


/**
 * Class 类的获取、使用
 */
interface HasBatteries {}
interface Waterproof {}
interface Shoots {}

class Toy {
    // 如果注释下面的无参数构造器会引起 NoSuchMethodError 错误
    Toy() {}
    Toy(int i) {}
}
class FancyToy extends Toy implements HasBatteries, Waterproof, Shoots {
    FancyToy() {
        super(1);
    }
}

class ToyTest {
    static void printInfo(Class cc) {
        System.out.println("Class name: " + cc.getName()
                + " is interface?[" + cc.isInterface() + "]");
        System.out.println("Simple name: " + cc.getSimpleName());
        System.out.println("Canonical name: " + cc.getCanonicalName());
    }

    public static void main(String[] args) {
        Class c = null;
        try {
            // 传递给 forName() 的字符串必须使用类的全限定名（包含包名）
            c = Class.forName("FancyToy");
        } catch (ClassNotFoundException e) {
            System.out.println("Can't find FancyToy");
            System.exit(1);
        }

        printInfo(c);
        for (Class face : c.getInterfaces()) {
            printInfo(face);
        }

        Class up = c.getSuperclass();
        Object obj = null;

        try {
            // 使用 newInstance() 创建类，必须带有无参数的构造器。
            // （使用 Java 反射 API 可用任意构造器来动态地创建类的对象）
            obj = up.newInstance();
        } catch (IllegalAccessException e) {
            System.out.println("Cannot access");
            System.exit(1);
        } catch (InstantiationException e) {
            System.out.println("Cannot instantiate");
        }

        printInfo(obj.getClass());
    }

}


/**
 * 【类字面常量】
 */
class Initable {
    static final int STATIC_FINAL = 47;
    static final int STATIC_FINAL2 = ClassInitialization.rand.nextInt(1000);
    static {
        System.out.println("Initializing Initable");
    }
}
class Initable2 {
    static int staticNonFinal = 147;
    static {
        System.out.println("Initializing Initable2");
    }
}
class Initable3 {
    static final int staticNonFinal = 74;
    static {
        System.out.println("Initializing Initable3");
    }
}

class ClassInitialization {
    public static Random rand = new Random(47);

    public static void main(String[] args) throws ClassNotFoundException {
        // 使用类字面常量获得 Class 对象
        // 不触发初始化:
        Class initable = Initable.class;
        System.out.println("After creating Initable ref");
        // 不触发初始化:
        System.out.println(Initable.STATIC_FINAL);
        // 触发初始化:
        System.out.println(Initable.STATIC_FINAL2);
        // 触发初始化:
        System.out.println(Initable2.staticNonFinal);
        // 触发初始化:
        Class initable3 = Class.forName("Initable3");
        System.out.println("After creating Initable3 ref");
        System.out.println(Initable3.staticNonFinal);
    }
}


/**
 * 【泛化的 Class 引用】
 * 就是 Class 与 泛型 结合
 */

/**
 * 使用泛型
 */
class GenericClassReferences {
    public static void main(String[] args) {
        Class intClass = int.class;
        intClass = double.class;

        Class<Integer> genericIntClass = int.class;
        // int.class 与 Integer.class 是同一个东西
        genericIntClass = Integer.class;
        // 非法
        //genericIntClass = double.class;
    }
}

/**
 * 使用通配符
 */
class WildcardClassReferences {
    public static void main(String[] args) {
        // 使用 Class<?> 要比 Class 好
        // 因为这是在告诉编译器，是故意使用了一个非具体的类引用，不会有编译器警告信息
        Class<?> intClass = int.class;
        intClass = double.class;
    }
}

/**
 * 使用泛型和newInstance()
 */
class CountedInteger {
    private static long counter;
    private final long id = counter++;

    @Override
    public String toString() {
        return "id=" + id;
    }
}
class DynamicSupplier<T> implements Supplier<T> {
    private Class<T> type;
    public DynamicSupplier(Class<T> type) {
        this.type = type;
    }
    @Override
    public T get() {
        try {
            // 使用 newInstance() 创建对象
            return type.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Stream.generate(new DynamicSupplier<>(CountedInteger.class))
                .skip(10)
                .limit(5)
                .forEach(System.out::println);
    }
}
/*
将泛型语法用于 Class 对象时，
newInstance() 将返回该对象的确切类型，
而不仅仅只是在 ToyTest.java 中看到的基类 Object。
 */

/**
 * 泛型语法用于 Class 对象的限制
 */
class GenericToyTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Class<FancyToy> ftClass = FancyToy.class;
        // 产生精确的类型:
        FancyToy fancyToy = ftClass.newInstance();

        Class<? super FancyToy> up = ftClass.getSuperclass();
        // 不会通过编译：
        //Class<Toy> up2 = ftClass.getSuperclass();
        // 只会产生 Object 对象：
        Object obj = up.newInstance();
    }
}
/*
如果你手头的是超类，那编译器将只允许你声明超类引用为“某个类，它是 FancyToy 的超类”，
就像在表达式 Class<? super FancyToy> 中所看到的那样。而不会接收 Class<Toy> 这样的声明。

这看上去显得有些怪，
因为 getSuperClass() 方法返回的是基类（不是接口），并且编译器在编译期就知道它是什么类型了（Toy.class）,
而不仅仅只是某个“类”。
不管怎样，正是由于这种含糊性，up.newInstance 的返回值不是精确类型，而只是 Object。
 */


/**
 * 【cast() 方法】
 * 用于 Class 引用的转型语法。
 * cast() 在无法使用普通类型转换的情况下会显得非常有用，
 * 但事实却是这种情况非常少见
 */
class Building {}
class House extends Building {}
class ClassCasts {
    public static void main(String[] args) {
        // 向上转型：
        Building b = new House();
        Class<House> houseType = House.class;
        // 向下转型：
        House h = houseType.cast(b);
        // 或者：
        h = (House) b;
    }
}





