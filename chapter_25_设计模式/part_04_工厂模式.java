/*
【引言】
当你发现必须将新类型添加到系统中时，合理的第一步是使用多态性为这些新类型创建一个通用接口。
这会将你系统中的其余代码与要添加的特定类型的信息分开，
使得可以在不改变现有代码的情况下添加新类型……或者看起来如此。
 *
起初，在这种设计中，似乎你必须更改代码的唯一地方就是你继承新类型的地方，但这并不是完全正确的。
你仍然必须创建新类型的对象，并且在创建时必须指定要使用的确切构造器。
 *
因此，如果创建对象的代码分布在整个应用程序中，
那么在添加新类型时，你将遇到相同的问题——你仍然必须追查你代码中新类型碍事的所有地方。
恰好是类型的创建碍事，而不是类型的使用（通过多态处理），
但是效果是一样的：添加新类型可能会引起问题。
 *
解决方案是强制对象的创建都通过通用工厂进行，而不是允许创建代码在整个系统中传播。
如果你程序中的所有代码都必须执行通过该工厂创建你的一个对象，那么在添加新类时只需要修改工厂即可。
 *
由于每个面向对象的程序都会创建对象，并且很可能会通过添加新类型来扩展程序，
因此工厂是最通用的设计模式之一。
 */

import com.sun.org.apache.bcel.internal.generic.GETFIELD;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 举例来说，让我们重新看一下Shape系统。
 * 首先，我们需要一个用于所有示例的基本框架。
 * 如果无法创建Shape对象，则需要抛出一个合适的异常：
 */
class BadShapeCreation extends RuntimeException {
    public BadShapeCreation(String msg) {
        super(msg);
    }
}

/**
 * 接下来，是一个Shape基类：
 */
class Shape4 {
    private static int counter = 0;
    private int id = counter++;

    public void draw() {
        System.out.println(this + " draw()");
    }

    public void erase() {
        System.out.println(this + " erase()");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + "]";
    }
}
/*
该类自动为每一个 Shape 对象创建唯一的 id，并使用运行时信息来发现特定的 Shape 子类名称。
 */

/**
 * 创建一些具体的图形类（Shape 类的子类）
 */
class Circle4 extends Shape4 {}
class Square4 extends Shape4 {}
class Triangle4 extends Shape4 {}

/**
 * 创建一个工厂：
 * （具有能够创建对象的方法的类）
 */
interface FactoryMethod4 {
    Shape4 create(String type);
}
/*
create()接收一个参数，这个参数使其决定要创建哪一种Shape对象，这里是String，
但是它其实可以是任何数据集合。对象的初始化数据（这里是字符串）可能来自系统外部。
 */

class ShapeFactory4 implements FactoryMethod4 {
    public Shape4 create(String type) {
        switch (type) {
            case "Circle4": return new Circle4();
            case "Square4": return new Square4();
            case "Triangle4": return new Triangle4();
            default: throw new BadShapeCreation(type);
        }
    }
}

/**
 * 测试工厂
 */
class FactoryTest {
    public static void test(FactoryMethod4 factory) {
        Stream.of("Circle4", "Square4", "Triangle4",
                "Square4", "Circle4", "Circle4", "Triangle4")
                .map(factory::create)
                .peek(Shape4::draw)
                .peek(Shape4::erase)
                .count();
    }

    public static void main(String[] args) {
        FactoryTest.test(new ShapeFactory4());
    }
}

/*
现在，create()是添加新类型的Shape时系统中唯一需要更改的其他代码。
 */


/*
【动态工厂】
前面例子中的静态create()方法强制所有创建操作都集中在一个位置，
因此这是添加新类型的Shape时唯一必须更改代码的地方。
这当然是一个合理的解决方案，因为它把创建对象的过程限制在一个框内。
但是，如果你在添加新类时无需修改任何内容，那就太好了。
 */

/**
 * 以下版本使用反射在首次需要时将 Shape 的构造器动态加载到工厂列表中：
 */
class ShapeFactory5 implements FactoryMethod4 {
    Map<String, Constructor> factories = new HashMap<>();

    static Constructor load(String type) {
        System.out.println("loading " + type);
        try {
            return Class.forName(type).getConstructor();
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new BadShapeCreation(type);
        }
    }

    @Override
    public Shape4 create(String type) {
        try {
            return (Shape4) factories
                    .computeIfAbsent(type, ShapeFactory5::load)
                    .newInstance();
        } catch (IllegalAccessException |
                InstantiationException |
                InvocationTargetException e) {
            throw new BadShapeCreation(type);
        }
    }

    public static void main(String[] args) {
        FactoryTest.test(new ShapeFactory5());
    }
}
/*
这里是通过 Class.forName() 扫描特定的包，获取类来实现动态加载。
 *
create()方法和之前一样，通过 type 获取 Shape 对象。
 *
然而，当你开始运行程序时，工厂的map为空。
create()使用map的computeIfAbsent()方法来查找构造器（如果该构造器已存在于map中）。
如果不存在则使用load()计算出该构造器，并将其插入到map中。
 *
从输出中可以看到，
每种特定类型的Shape都是在第一次请求时才加载的，（map的computeIfAbsent()方法）
然后只需要从map中检索它。
 */


/**
 * 【多态工厂】
 * 采用“工厂方法”模式的原因是可以从基本工厂中继承出不同类型的工厂。
 * 这里再次修改示例，实现“多态工厂”：
 */
interface PolymorphicFactory {
    Shape4 create();
}

class RandomShapes implements Supplier<Shape4> {
    private final PolymorphicFactory[] factories;
    private Random rand = new Random(42);

    RandomShapes(PolymorphicFactory... factories) {
        this.factories = factories;
    }

    @Override
    public Shape4 get() {
        return factories[rand.nextInt(factories.length)].create();
    }
}

class ShapeFactory3 {
    public static void main(String[] args) {
        RandomShapes rs = new RandomShapes(
                Circle4::new,
                Square4::new,
                Triangle4::new);
        Stream.generate(rs)
                .limit(6)
                .peek(Shape4::draw)
                .peek(Shape4::erase)
                .count();
    }
}
/*
一点疑惑：
Circle4::new 是指？为什么不在一个继承体系中的类可以进行赋值操作？（排除向上转型的可能性）
 */


/*
【抽象工厂】
抽象工厂模式看起来像我们之前所见的工厂对象，
但拥有不是一个工厂方法而是几个工厂方法，每个工厂方法都会创建不同种类的对象。
这个想法是在创建工厂对象时，你决定如何使用该工厂创建的所有对象。
 */

/*
作为另一个示例，
假设你正在创建一个通用游戏环境来支持不同类型的游戏。
使用抽象工厂看起来就像下文那样：
 */

/**
 * Obstacle、Player 是游戏中的游戏模式与角色：
 */
interface Obstacle {
    void action();
}

interface Player {
    void interactWith(Obstacle o);
}

class Kitty implements Player {
    @Override
    public void interactWith(Obstacle o) {
        System.out.print("Kitty has encountered a ");
        o.action();
    }
}
class KungFuGuy implements Player {
    @Override
    public void interactWith(Obstacle o) {
        System.out.print("KungFuGuy has encountered a ");
        o.action();
    }
}

class Puzzle implements Obstacle {
    @Override
    public void action() {
        System.out.println("Puzzle");
    }
}
class NastyWeapon implements Obstacle {
    @Override
    public void action() {
        System.out.println("NastyWeapon");
    }
}

/**
 * 抽象工厂：
 */
class GameElementFactory {
    Supplier<Player> player;
    Supplier<Obstacle> obstacle;
}

/**
 * 具体的工厂类：
 */
class KittiesAndPuzzles extends GameElementFactory {
    KittiesAndPuzzles() {
        player = Kitty::new;
        obstacle = Puzzle::new;
    }
}
class KillAndDismember extends GameElementFactory {
    KillAndDismember() {
        player = KungFuGuy::new;
        obstacle = NastyWeapon::new;
    }
}

/**
 * 游戏环境：
 */
class GameEnvironment {
    private Player p;
    private Obstacle o;

    public GameEnvironment(GameElementFactory factory) {
        p = factory.player.get();
        o = factory.obstacle.get();
    }

    public void play() {
        p.interactWith(o);
    }

    public static void main(String[] args) {
        GameElementFactory kp = new KittiesAndPuzzles(),
                kd = new KillAndDismember();
        GameEnvironment g1 = new GameEnvironment(kp),
                g2 = new GameEnvironment(kd);
        g1.play();
        g2.play();
    }
}
/*
在这种环境中，Player对象与Obstacle对象进行交互，
但是根据你所玩游戏的类型，存在不同类型的玩家和障碍物。

你可以通过选择特定的GameElementFactory来确定游戏的类型，
然后GameEnvironment控制游戏的设置和玩法。

在此示例中，设置和玩法非常简单，
但是这些活动（初始条件和状态变化）可以决定游戏的大部分结果。

这里，GameEnvironment不是为继承而设计的，尽管这样做很有意义。
它还包含“双重调度”和“工厂方法”的示例，稍后将对这两个示例进行说明。
 */
