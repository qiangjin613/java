/*
【边界】
边界允许我们对泛型使用的参数类型施加约束。
尽管这可以强制执行有关应用了泛型类型的规则，但潜在的更重要的效果是我们可以在绑定的类型中调用方法。

由于擦除会删除类型信息，因此唯一可用于无限制泛型参数的方法是那些 Object 可用的方法。
但是，如果将该参数限制为某类型的子集，则可以调用该子集中的方法。

为了应用约束，Java 泛型使用了 extends 关键字。
注意：当 extends 用于限定泛型类型时，涵义与通常截然相反。
 */

import java.awt.*;

/**
 * 一个使用 extends 的简单例子：（单个边界）
 */
interface HasColor {
    java.awt.Color getColor();
}

class WithColor<T extends HasColor> {
    T item;

    WithColor(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }

    // 因为限定泛型类型，所以可以使用 HasColor 的方法：
    java.awt.Color color() {
        return item.getColor();
    }
}

class Coord {
    public int x, y, z;
}


/*
使用多个边界时，先是类，再是接口
 */
// 错误的使用方式：
// class WithColorCoord<T extends HasColor & Coord> {}

class WithColorCoord<T extends Coord & HasColor> {
    T item;

    WithColorCoord(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }

    java.awt.Color color() {
        return item.getColor();
    }

    int getX() {
        return item.x;
    }

    int getY() {
        return item.y;
    }

    int getZ() {
        return item.z;
    }
}



/*
与继承一样：只能有一个类边界，可以有多个接口边界。
 */
interface Weight {
    int weight();
}

class Solid<T extends Coord & HasColor & Weight> {
    T item;

    Solid(T item) {
        this.item = item;
    }

    java.awt.Color color() {
        return item.getColor();
    }

    int getX() {
        return item.x;
    }

    int getY() {
        return item.y;
    }

    int getZ() {
        return item.z;
    }

    int weight() {
        return item.weight();
    }
}

/**
 * 使用上面的边界类
 */
class Bounded extends Coord implements HasColor, Weight {

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public int weight() {
        return 0;
    }
}
class BasicBounds {
    public static void main(String[] args) {
        Solid<Bounded> solid = new Solid<>(new Bounded());
        solid.color();
        solid.getX();
        solid.weight();
    }
}

/*
---------------------分界线---------------------------
相同的知识点，不过下面使用了继承，优化了上面冗余的代码
 */

class HoldItem<T> {
    T item;

    HoldItem(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }
}

class WithColor2<T extends HasColor> extends HoldItem<T> {
    WithColor2(T item) {
        super(item);
    }

    java.awt.Color color() {
        return item.getColor();
    }
}

class WithColorCoord2<T extends Coord & HasColor> extends WithColor2<T> {
    WithColorCoord2(T item) {
        super(item);
    }

    int getX() {
        return item.x;
    }

    int getY() {
        return item.y;
    }

    int getZ() {
        return item.z;
    }
}

class Solid2<T extends Coord & HasColor & Weight> extends WithColorCoord2<T> {
    Solid2(T item) {
        super(item);
    }

    int weight() {
        return item.weight();
    }
}

class InheritBounds {
    public static void main(String[] args) {
        Solid2<Bounded> solid = new Solid2<>(new Bounded());
        solid.color();
        solid.getX();
        solid.weight();
    }
}
