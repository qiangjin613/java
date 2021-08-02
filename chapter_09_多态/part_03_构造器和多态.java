/**
 * 在这之前，可以先看看 “chapter_08_复用\part_09_类初始化和加载.java”
 * 【构造器初始化顺序】（前言：先要分配内存，才能进行初始化呀！）
 * 0. 在所有事发生之前，分配给对象的存储空间会被初始化为二进制 0（或某些特殊类型与 0 等价的值）
 *    【这个是指在按生命初始化成员之前，在进栈之后吗？ 或者说，什么时候给对象变量分配内存呢？】
 * 1. 从 main 方法的类开始，看当前类有没有基类，如果有，依次进栈
 *    如：Sandwich、PortableLunch、Lunch、Meal 依次进栈
 * 2. 从栈顶弹出，然后按文本顺序初始化成员（成员变量、初始化块）后，再初始化构造器
 */
class Meal {
    Meal() {
        System.out.println("Meal()");
    }
}
class Bread {
    Bread() {
        System.out.println("Bread()");
    }
}
class Cheese {
    Cheese() {
        System.out.println("Cheese()");
    }
}
class Lettuce {
    Lettuce() {
        System.out.println("Lettuce()");
    }
    private Cheese c = new Cheese();
    {
        System.out.println("111111");
    }
}
class Lunch extends Meal {
    Lunch() {
        // 隐式调用了基类 Meal 不带参数的构造器
        System.out.println("Lunch()");
    }
}
class PortableLunch extends Lunch {
    PortableLunch() {
        System.out.println("PortableLunch()");
    }
}
class Sandwich extends PortableLunch {
    private Bread b = new Bread();
    private Cheese c = new Cheese();
    private Lettuce l = new Lettuce();
    public Sandwich() {
        System.out.println("Sandwich()");
    }

    public static void main(String[] args) {
        new Sandwich();
    }
}
/*
不幸的是，这不能处理所有情况，在下一节会看到：
在这种初始化体系下，还是会有漏网之鱼...
（当“多态”在构造器中发生）
 */


/**
 * 【继承和清理】
 * 在使用组合和继承创建新类时，大部分时候你无需关心清理。子对象通常会留给垃圾收集器处理。
 * 如果你存在清理问题，那么必须用心地为新类创建一个 dispose() 方法，
 * 由于继承，如果有其他特殊的清理工作的话，就必须在派生类中重写 dispose() 方法。
 * 当重写 dispose() 方法时，记得调用基类的 dispose() 方法，否则基类的清理工作不会发生
 *
 * 销毁的顺序应该与初始化的顺序相反，以防一个对象依赖另一个对象。
 * 销毁顺序：
 * 1）对于属性
 *    销毁顺序与初始化顺序相反（因为属性是按照声明顺序初始化的）。防止一个对象依赖另一个对象。
 * 2）对于基类
 *    （遵循 C++ 析构函数的形式）先销毁派生类，再销毁基类。防止派生类调用了基类的一些方法。
 */
class Characteristic {
    private String s;
    Characteristic(String s) {
        this.s = s;
        System.out.println("Creating Characteristic " + s);
    }
    protected void dispose() {
        System.out.println("disposing Characteristic " + s);
    }
}
class Description {
    private String s;
    Description(String s) {
        this.s = s;
        System.out.println("Creating Description " + s);
    }
    protected void dispose() {
        System.out.println("disposing Description " + s);
    }
}
class LivingCreature {
    private Characteristic p = new Characteristic("is alive");
    private Description t = new Description("Basic Living Creature");
    LivingCreature() {
        System.out.println("LivingCreature()");
    }
    // [1] 组合类中编写 dispose() 方法
    protected void dispose() {
        // 先銷毀本身
        System.out.println("LivingCreature dispose");
        // 再与创建顺序相反的顺序销毁对象
        t.dispose();
        p.dispose();
    }
}
class Animal extends LivingCreature {
    private Characteristic p = new Characteristic("has heart");
    private Description t = new Description("Animal not Vegetable");
    Animal() {
        System.out.println("Animal");
    }
    // [2] 继承中重写 dispose() 方法
    @Override
    protected void dispose() {
        // 先销毁自身
        System.out.println("Animal dispose");
        // 再按照创建的反序销毁组合
        t.dispose();
        p.dispose();
        // 最后销毁基类
        super.dispose();
    }
}
class Frog extends Animal {
    private Characteristic p = new Characteristic("Croaks");
    private Description t = new Description("Eats Bugs");
    public Frog() {
        System.out.println("Frog()");
    }

    @Override
    protected void dispose() {
        System.out.println("Frog dispose");
        t.dispose();
        p.dispose();
        super.dispose();
    }

    public static void main(String[] args) {
        Frog frog = new Frog();
        System.out.println("Bye!");
        // 最后销毁当前对象
        frog.dispose();
    }
}

/*
在上述案例中，Frog 对象拥有自己的成员对象，它创建了这些成员，并且知道它们能存活多久，
所以它知道如何调用 dispose() 方法。然而
当某个成员对象被其他一个或多个对象共享时，就不能只是简单地调用 dispose()。
 *
这是一个稍微复杂的情况：
也许就必须使用<strong>引用计数</strong>来跟踪仍然访问着共享对象的对象数量。
 */
class Shared {
    // 记录该对象被共享的次数
    private int refcount = 0;
    private static long counter = 0;
    private final long id = counter++;

    Shared() {
        System.out.println("Creating " + this);
    }

    public void addRef() {
        refcount++;
    }

    protected void dispose() {
        // 当该对象没有被其他类共享时，销毁该对象
        if (--refcount == 0) {
            System.out.println("Disposing " + this);
        }
    }

    @Override
    public String toString() {
        return "Shared{" + "refcount=" + refcount + ", id=" + id + '}';
    }
}
class Composing {
    private Shared shared;
    private static long counter = 0;
    private final long id = counter++;

    Composing(Shared shared) {
        this.shared = shared;
        // 该对象的引用 +1
        this.shared.addRef();
        System.out.println("Creating " + this);
    }

    protected void dispose() {
        System.out.println("disposing " + this);
        shared.dispose();
    }

    @Override
    public String toString() {
        return "Composing{" + "shared=" + shared + ", id=" + id + '}';
    }
}

class ReferenceCounting {
    public static void main(String[] args) {
        Shared shared = new Shared();
        Composing[] composings = {
                new Composing(shared),
                new Composing(shared),
                new Composing(shared),
                new Composing(shared),
                new Composing(shared),
                new Composing(shared)
        };
        for (Composing c : composings) {
            c.dispose();
        }
    }
}
/*
上述案例的注意事项：
在将一个 shared 对象附着在类上时，必须记住调用 addRef()，
而 dispose() 方法会跟踪引用数，以确定在何时真正地执行清理工作。
【使用这种技巧需要加倍细心，但是如果需要清理正在共享的对象，你没有太多选择】
 */


/**
 * 【构造器内部多态方法的行为】
 * 在构造器中调用了正在构造的对象的动态绑定方法
 */
class Glyph {
    void draw() {
        System.out.println("父类 Glyph.draw()");
    }
    Glyph() {
        System.out.println("父类 Glyph() before draw()");
        // 在本例中，这里的方法调用，使用的是动态绑定的方法，
        // 但动态绑定的 draw() 使用了未初始化的 radius 变量
        draw();
        System.out.println("父类 Glyph() after draw()");
    }
}
class RoundGlyph extends Glyph {
    private int radius = 1;
    RoundGlyph(int r) {
        radius = r;
        System.out.println("子类 RoundGlyph.RoundGlyph(), radius = " + radius);
    }

    @Override
    void draw() {
        System.out.println("子类 RoundGlyph.draw(), radius = " + radius);
    }
}
class PolyConstructors {
    public static void main(String[] args) {
        new RoundGlyph(5);
    }
}
/*
这里说明，至少在初始化基类 Glyph 之前，就将其派生类 RoundGlyph 的成员变量默认初始化了（系统进行的初始化）
【这个问题其实是对象变量什么时候分配内存的问题】
 */

/**
 * Q：radius 的值并未初始化，为什么为 0？
 * A：在初始化的之前，分配给对象的存储空间会被初始化为二进制 0。
 *    这么做有个优点：
 *    首先，防止了引用未被初始化而造成的异常错误；
 *    其次，所有事物至少初始化为 0（或某些特殊数据类型与 0 等价的值），而不是仅仅留作垃圾。
 *    这包括了通过组合嵌入类中的对象引用，被赋予 null。如果忘记初始化该引用，就会在运行时出现异常。
 *
 * 因此，编写构造器有一条良好规范：
 *    做尽量少的事让对象进入良好状态。
 *    如果有可能的话，尽量不要调用类中的任何方法。
 *    在基类的构造器中能安全调用的只有基类的 final 方法（这也适用于可被看作是 final 的 private 方法）。
 *    这些方法不能被重写，因此不会产生意想不到的结果。
 *    你可能无法永远遵循这条规范，但应该朝着它努力。
 */
