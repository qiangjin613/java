import java.util.Random;

/**
 * 对于忘记类型的多态机制，编译器是如何知道引用指向的是哪个类型呢？
 * ”绑定“机制解释了答案。
 */


/**
 * 【方法调用绑定】
 * 将一个方法调用和一个方法主体关联起来称作绑定。
 *
 * 根据”绑定“发生的时期，可分为：前期绑定、后期绑定。
 * 1）前期绑定
 *    绑定发生在程序运行前（如果有的话，由编译器和链接器实现）。
 * 2）后期绑定
 *    在运行时根据对象的类型进行绑定，也称为动态绑定或运行时绑定。
 *    当一种语言实现了后期绑定，就必须具有某种机制在运行时能判断对象的类型，从而调用恰当的方法。
 *    也就是说，编译器仍然不知道对象的类型（忘记了对象类型），但是方法调用机制能找到正确的方法体并调用。
 *    每种语言的后期绑定机制都不同，但是可以想到，对象中一定存在某种类型信息。
 *
 * （在 C 语言中只有这一种方法调用）
 * Java 中除了 static 和 final 方法（private 方法也是隐式的 final）外，其他所有方法都是后期绑定。
 * 这意味着通常情况下，我们不需要判断后期绑定是否会发生——它自动发生。
 *
 * 再次解释”为什么将一个对象指明为 final ？“
 * 因为有效地关闭了动态绑定，或者说告诉编译器不需要对其进行动态绑定。
 * 然而，现在使用 final 最好是为了设计使用，而不是为了提升性能。
 */


/**
 * 【产生正确的行为】
 * 形状的例子：
 *      Shape2 形状
 *      RandomShapes 工厂
 */
class Shape2 {
    public void draw() {}
    public void erase() {}
}
class Circle2 extends Shape2 {
    @Override
    public void draw() {
        System.out.println("Circle2.draw()");
    }
    @Override
    public void erase() {
        System.out.println("Circle2.erase()");
    }
}
class Square2 extends Shape2 {
    @Override
    public void draw() {
        System.out.println("Square2.draw()");
    }
    @Override
    public void erase() {
        System.out.println("Square2.erase()");
    }
}
class Triangle2 extends Shape2 {
    @Override
    public void draw() {
        System.out.println("Triangle2.draw()");
    }
    @Override
    public void erase() {
        System.out.println("Triangle2.erase()");
    }
}

class RandomShapes2 {
    private Random rand = new Random(47);
    public Shape2 get() {
        switch (rand.nextInt(3)) {
            default:
            case 0: return new Circle2();
            case 1: return new Square2();
            case 2: return new Triangle2();
        }
    }
    public Shape2[] array(int sz) {
        Shape2[] shapes = new Shape2[sz];
        // 填充图形数组：
        for (int i = 0; i < shapes.length; i++) {
            shapes[i] = get();
        }
        return shapes;
    }
}

class Shpes2 {
    public static void main(String[] args) {
        RandomShapes2 gen = new RandomShapes2();
        for (Shape2 shape2 : gen.array(9)) {
            // 所有的方法都是通过动态绑定运行的
            shape2.draw();
        }
    }
}


/**
 * 【陷阱：“重写”私有方法】
 * 基类的 private 方法对于派生类是隐蔽的。
 * 基类同名的 f() 方法是一个全新的方法，不算是重写方法！
 *
 * 结论：
 * 只有非 private 方法才能被重写，建议为重写的方法添加 @Override 注解（就能通过编译器检测出来）。
 */
class PrivateOverride {
    private void f() {
        System.out.println("private f()");
    }

    public static void main(String[] args) {
        PrivateOverride po = new Derived();
        po.f(); // output: private f()
    }
}
class Derived extends PrivateOverride {
    // @Override
    public void f() {
        System.out.println("public f()");
    }
}


/**
 * 【陷阱：属性与静态方法】
 * 只有普通的方法调用可以是多态的。（普通方法的动态绑定）
 */
class Super {
    public int field = 0;

    public int getField() {
        return field;
    }
}
class Sub extends Super {
    public int field = 1;

    @Override
    public int getField() {
        return field;
    }

    public int getSuperField() {
        return super.field;
    }
}
class FieldAccess {
    public static void main(String[] args) {
        Super sup = new Sub();
        // sup 字段是自己的，方法获取的字段值是派生类（动态绑定）的
        System.out.println("sup.field = " + sup.field +
                ", sup.getField() = " + sup.getField());

        Sub sub = new Sub();
        System.out.println("sub.field = " + sub.field +
                ", sub.getField() = " + sub.getField()
                + ", sub.getSuperField() = " + sub.getSuperField());
    }
}

/**
 *
 */
class StaticSuper {
    public static String staticGet() {
        return "StaticSuper.staticGet()";
    }
    public String dynamicGet() {
        return "StaticSuper.dynamicGet()";
    }
}
class StaticSub extends StaticSuper {
    // @Override，编译出错：“重写方法不在基类中”
    public static String staticGet() {
        return "StaticSub.staticGet()";
    }
    @Override
    public String dynamicGet() {
        return super.dynamicGet();
    }
}
class StaticPolymorphism {
    public static void main(String[] args) {
        StaticSuper sup = new StaticSuper();
        System.out.println(StaticSuper.staticGet());
        System.out.println(sup.dynamicGet());

        StaticSuper sub = new StaticSub();
        System.out.println(StaticSuper.staticGet());
        System.out.println(sub.dynamicGet());

        /**
         * output:
         * StaticSuper.staticGet()
         * StaticSuper.dynamicGet()
         * StaticSuper.staticGet()
         * StaticSuper.dynamicGet()
         *
         * 结论：静态的方法只与类关联，与单个的对象无关。
         */
    }
}
