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
            Class<?> gum = Class.forName("Gum");
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
 * 【Class 类的获取、使用】
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
            Toy t = (Toy) obj;
            printInfo(t.getClass());
        } catch (IllegalAccessException e) {
            System.out.println("Cannot access");
            System.exit(1);
        } catch (InstantiationException e) {
            System.out.println("Cannot instantiate");
        }

        printInfo(obj.getClass());
    }
}
/*
小结
-------
    Method                      class                       InnerClass
String getName()            packageName.className       packageName.className$innerClassName
String getSimpleName()      className                   innerClassName
String getCanonicalName()   packageName.className       packageName.className.innerClassName
注：getCanonicalName() 除内部类和数组外，对大部分类产生的结果与 getName() 相同
-----------
    Method
native Class<? super T> getSuperclass()

native boolean isInterface()
Class<?>[] getInterfaces()

T newInstance() throws InstantiationException, IllegalAccessException       调用无参数的构造器，没有无参构造器就会抛出异常！
 */



/*
【类字面常量】
Java 还提供了另一种方法来生成类对象的引用：类字面常量。如：Initable.class

类字面常量不仅可以应用于普通类，也可以应用于接口、数组以及基本数据类型。
另外，对于基本数据类型的包装类，还有一个标准字段 TYPE。
TYPE 字段是一个引用，指向对应的基本数据类型的 Class 对象，如下所示：
boolean.class   等价于     Boolean.TYPE
char.class	    等价于     Character.TYPE
byte.class	    等价于     Byte.TYPE
short.class	    等价于     Short.TYPE
int.class	    等价于     Integer.TYPE
long.class	    等价于     Long.TYPE
float.class	    等价于     Float.TYPE
double.class	等价于     Double.TYPE
void.class	    等价于     Void.TYPE

建议是使用 .class 的形式，以保持与普通类的一致性。


------------------------------------
ClassName.class vs Class.forName()  |
------------------------------------
这样做不仅更简单，而且更安全，因为它在编译时就会受到检查（因此不必放在 try 语句块中）。
并且它根除了对 forName() 方法的调用，所以效率更高。


注意，有一点很有趣：
当使用 .class 来创建对 Class 对象的引用时，不会自动地初始化该 Class 对象。
为了使用类而做的准备工作实际包含三个步骤：
    1.加载
    这是由类加载器执行的。该步骤将查找字节码（通常在 classpath 所指定的路径中查找，但这并非是必须的），
    并从这些字节码中创建一个 Class 对象。
    2.链接
    在链接阶段将验证类中的字节码，为 static 字段分配存储空间，
    并且如果需要的话，将解析这个类创建的对其他类的所有引用。
    3.初始化
    如果该类具有超类，则先初始化超类，执行 static 初始化器和 static 初始化块。

直到第一次引用一个 static 方法（构造器隐式地是 static）
或者非常量的 static 字段，才会进行类初始化。
 */
class Initable {
    static final Integer STATIC_FINAL = 47;
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
        System.out.println(Initable.STATIC_FINAL);

        // 触发初始化:
        System.out.println(Initable.STATIC_FINAL2);
        System.out.println(Initable2.staticNonFinal);
        Class initable3 = Class.forName("Initable3");
        System.out.println("After creating Initable3 ref");
        System.out.println(Initable3.staticNonFinal);
    }
}
/*
初始化有效地实现了尽可能的“惰性”，从对 initable 引用的创建中可以看到，
仅使用 .class 语法来获得对类对象的引用不会引发初始化。
但与此相反，使用 Class.forName() 来产生 Class 引用会立即就进行初始化，如 initable3。

如果一个 static final 值是“编译期常量”（如 Initable.staticFinal），
那么这个值不需要对 Initable 类进行初始化就可以被读取。
但是，如果只是将一个字段设置成为 static 和 final，还不足以确保这种行为。
例如，对 Initable.staticFinal2 的访问将强制进行类的初始化，因为它不是一个编译期常量。

如果一个 static 字段不是 final 的，那么在对它访问时，
总是要求在它被读取之前，要先进行链接（为这个字段分配存储空间）和初始化（初始化该存储空间），
就像在对 Initable2.staticNonFinal 的访问中所看到的那样。
 */




/*
【泛化的 Class 引用】
Class 引用总是指向某个 Class 对象，
而 Class 对象可以用于产生类的实例，并且包含可作用于这些实例的所有方法代码。
它还包含该类的 static 成员，因此 Class 引用表明了它所指向对象的确切类型，
而该对象便是 Class 类的一个对象。

但是，Java 设计者看准机会，将它的类型变得更具体了一些。
Java 引入泛型语法之后，我们可以使用泛型对 Class 引用所指向的 Class 对象的类型进行限定。
 */

/**
 * 使用泛型
 */
class GenericClassReferences {
    public static void main(String[] args) {
        /*
        普通 Class 类的引用可以重新赋值指向任何其他的 Class 对象，
        但是使用泛型限定的类引用只能指向其声明的类型。
        通过使用泛型语法，我们可以让编译器强制执行额外的类型检查。
         */

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
 * 使用泛型和 newInstance()
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
        // 向下转型：将 Building 的 b 对象，向下转型为 House 类型的 h 对象
        House h = houseType.cast(b);
        // 或者：
        h = (House) b;
    }
}
