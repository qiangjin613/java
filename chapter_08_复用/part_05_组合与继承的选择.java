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
 * （“继承”，为向上转型/多态而生）
 * 继承最重要的方面不是为新类提供方法。它是新类与基类的一种关系。
 * 简而言之，这种关系可以表述为“新类是已有类的一种类型”。（是 “已有类型” 的进一步扩展）
 *
 * 如果使用继承做一个通用类，并为了某个特殊需求将其特殊化。
 * 稍微思考，发现：用一个交通工具对象来组成一部车是毫无意义的，车不包含交通工具，它就是交通工具。
 * （这种情况下，相比于组合，使用继承更合理一些）
 *
 *
 * 一个具体的使用组合与继承参考的话语：
 * “是一个”的关系是用继承来表达的，而“有一个“的关系则用组合来表达。
 *
 * 一种判断使用组合还是继承的最清晰的方法是问一问自己是否需要把新类向上转型为基类。
 * 如果必须向上转型，那么继承就是必要的，但如果不需要，则要进一步考虑是否该采用继承。
 * （“多态”一章提出了一个使用向上转型的最有力的理由，但是只要记住问一问“我需要向上转型吗？”，
 * 就能在这两者中作出较好的选择。）
 *
 * （“多态”章节中将进一步进行讨论...）
 */
