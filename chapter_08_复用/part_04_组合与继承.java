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
 * 换句话说，重载不会因为在基类（派生类）或者父类（基类）中的位置不同而失效！
 */
class Homer {
    char doh(char c) {
        System.out.println("doh(char)");
        return 'd';
    }
    // 重载上面的 doh 方法。重载是指编写同名的方法
    float doh(float f) {
        System.out.println("doh(float)");
        return 1.0f;
    }
    // 重载 doh()
    double doh(int i) {
        return 1.0f;
    }
    // 需要注意的是：重载是根据 方法签名（方法名 + 参数列表） 来判断的，返回值的不同不算重载。
//    int doh(char c) {
//        return 1;
//    }
}

class Milhouse  {}

class Bart extends Homer {
    // 重载基类的 doh():
    void doh(Milhouse m) {
        System.out.println("doh(Milhouse)");
    }

    // 覆盖基类的 doh():
    @Override
    double doh(int i) {
        return 1;
    }
}

class Hide {
    public static void main(String[] args) {
        Bart b = new Bart();
        // 在调用时，如果有就调用准确匹配的方法，如果没有就进行向上转型找有没有相关方法
        // 如：char --> int --> float --> double，还有 short、byte 等类型如同数值计算时的自动转换
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
