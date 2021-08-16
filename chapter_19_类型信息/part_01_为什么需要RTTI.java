import java.util.stream.Stream;

abstract class Shape3 {
    void draw() {
        System.out.println(this + ".draw()");
    }

    /**
     * 重写父类（Object）的 toString() 方法，变为抽象方法，提醒实现类要重写该方法
     * @return
     */
    @Override
    public abstract String toString();
}

class Circle3 extends Shape3 {
    @Override
    public String toString() {
        return "Circle3";
    }
}

class Square3 extends Shape3 {
    @Override
    public String toString() {
        return "Square3";
    }
}

class Shapes {
    public static void main(String[] args) {
        Stream.of(new Circle3(), new Square3())
                .forEach(Shape3::draw);
    }
}
/*
上述例子中，把 Shape3 对象放入 Stream<Shape3> 中就会向上转型（隐式），
但在向上转型的时候也丢失了这些对象的具体类型。对 Stream 而言，它们只是 Shape3 对象。

严格来说，Stream<Shape3> 实际上是把放入其中的所有对象都当作 Object 对象来持有，
只是取元素时会自动将其类型转为 Shape3。这也是 RTTI 最基本的使用形式，
因为在 Java 中，所有类型转换的正确性检查都是在运行时进行的！
这也是 RTTI 的含义所在：运行时，识别一个对象的类型。
（为什么这么大胆向下转型？这是因为泛型保证了在编译时确保存入的都是 Shape3 对象）

另外在这个例子中，类型转换并不彻底：Object 被转型为 Shape，而不是 Circle、Square 或者 Triangle。
这是因为目前我们只能确保这个 Stream<Shape> 保存的都是 Shape：
    1.编译期，stream 和 _Java 泛型系统_确保放入 stream 的都是 Shape 对象
    （Shape 子类的对象也可视为 Shape 的对象），否则编译器会报错；
    2.运行时，_自动类型转换_确保了从 stream 中取出的对象都是 Shape 类型。

接下来就是多态机制的事了，Shape 对象实际执行什么样的代码，
是由引用所指向的具体对象（Circle、Square 或者 Triangle）决定的。

这也符合我们编写代码的一般需求，
通常，我们希望大部分代码尽可能少了解对象的具体类型，
而是只与对象家族中的一个通用表示打交道（本例中即为 Shape）。
这样，代码会更容易写，更易读和维护；设计也更容易实现，更易于理解和修改。
所以多态是面向对象的基本目标。
 */
