/**
 * 【结合组合与继承】
 * 经常同时使用组合和继承。
 */
class Plate {
    Plate(int i) {
        System.out.println("Plate constructor: i = " + i);
    }
}

class Utensil {
    Utensil() {
        System.out.println("Utensil constructor");
    }
    Utensil(int i) {
        System.out.println("Utensil constructor: i = " + i);
    }
}
class Spoon extends Utensil {
    Spoon(int i) {
        super(i);
        System.out.println("Spoon constructor: i = " + i);
    }
}
class Fork extends Utensil {
    Fork(int i) {
        super(i);
        System.out.println("Fork constructor: i = " + i);
    }
}

class Custom {
    Custom(int i) {
        System.out.println("Custom constructor: i = " + i);
    }
}
// [1] 使用继承
class PlaceSetting extends Custom {
    // [2] 组合
    private Spoon sp;
    private Fork frk;
    private Plate p;
    public PlaceSetting(int i ) {
        super(i + 1);
        sp = new Spoon(i + 2);
        frk = new Fork(i + 3);
        p = new Plate(i + 4);
        System.out.println("PlaceSetting constructor");
    }

    public static void main(String[] args) {
        PlaceSetting x = new PlaceSetting(9);
    }
}


/**
 * 【保证适当的清理】
 * 与“异常”章节中描述的一样
 *
 * 在屏幕上绘制图片的例子
 */
class Shape {
    Shape(int i) {
        System.out.println("Shape constructor");
    }
    void dispose() {
        System.out.println("Shape dispose");
    }
}
class Circle extends Shape {
    Circle(int i) {
        super(i);
        System.out.println("Drawing Circle");
    }
    @Override
    void dispose() {
        System.out.println("Erasing Circle");
        super.dispose();
    }
}
class Triangle extends Shape {
    Triangle(int i) {
        super(i);
        System.out.println("Drawing Triangle");
    }
    @Override
    void dispose() {
        System.out.println("Erasing Triangle");
        super.dispose();
    }
}
class CADSystem extends Shape {
    private Circle c;
    private Triangle t;
    private CADSystem(int i) {
        super(i + 1);
        c = new Circle(1);
        t = new Triangle(1);
        System.out.println("Combined constructor");
    }
    @Override
    void dispose() {
        System.out.println("CADSystem.dispose()");
        // 按与创建的相反顺序执行特定于类的所有清理工作。(一般来说，这要求基类元素仍然是可访问的。)
        // 防止一个子对象依赖另一个子对象
        t.dispose();
        c.dispose();
        super.dispose();
    }
    public static void main(String[] args) {
        CADSystem x = new CADSystem(47);
        try {
            // do sth.
        } finally {
            x.dispose();
        }
    }
}


/**
 * 【名称隐藏】
 * 重载机制不论是在基类还是派生类中，都会起作用。
 */
class Homer {
    char doh(char c) {
        System.out.println("doh(char)");
        return 'd';
    }
    float doh(float f) {
        System.out.println("doh(float)");
        return 1.0f;
    }
}

class Milhouse  {}

class Bart extends Homer {
    // 重载基类的 doh()：
    void doh(Milhouse m) {
        System.out.println("doh(Milhouse)");
    }
}

class Hide {
    public static void main(String[] args) {
        Bart b = new Bart();
        b.doh(1);
        b.doh('x');
        b.doh(1.0f);
        b.doh(new Milhouse());
    }
}

/**
 * Java 5 添加了 @Override 注解。
 * 当打算重写一个方法时，可以选择添加这个注解，
 * 如果不小心用了重载而不是重写，编译器会产生一个错误消息。
 */


/**
 * 【组合与继承的选择】
 * 1) 当想在新类中包含一个已有类的功能时，使用组合（or 委托）。新类的使用者看到的是新类的接口，而非嵌入对象的接口
 *
 * 有时让类的用户直接访问到新类中的组合成分是有意义的。（只需将成员对象声明为 public 即可，可以看作“半委托”）
 * 成员对象隐藏了具体实现，所以是安全的。
 * 当用户知道这是在组装以组部件时，会使接口更加容易理解。
 * 声明成员为 public 有助于客户端程序员理解如何使用类，且降低了类创建者面临的代码复杂度。
 * （但是，这这只是一个特例。通常来说，属性还是应该声明为 private）
 */

/**
 * Car 的例子
 */
class Engine {
    public void start() {}
    public void rev() {}
    public void stop() {}
}
class Wheel {
    public void inflate(int psi) {}
}
class Window {
    public void rollup() {}
    public void rolldown() {}
}
class Door {
    // 组合
    public Window window = new Window();

    public void open() {}
    public void close() {}
}
class Car {
    // 1个引擎、4个轮子、2扇门
    public Engine engine = new Engine();
    public Wheel[] wheels = new Wheel[4];
    public Door left = new Door(), right = new Door();

    public Car() {
        for (int i = 0; i < 4; i++) {
            wheels[i] = new Wheel();
        }
    }

    public static void main(String[] args) {
        Car car = new Car();
        car.left.window.rollup();
        car.wheels[0].inflate(72);
    }
}


/**
 * 【再论组合和继承】
 * 尽管在教授 OOP 的过程中我们多次强调继承，但这并不意味着要尽可能使用它。
 * 恰恰相反，尽量少使用它，除非确实使用继承是有帮助的！
 *
 * 一种判断使用组合还是继承的最清晰的方法是问一问自己是否需要把新类向上转型为基类。
 * 如果必须向上转型，那么继承就是必要的，但如果不需要，则要进一步考虑是否该采用继承。
 *
 * （“多态”章节中将进一步进行讨论...）
 */