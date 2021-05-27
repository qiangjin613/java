interface CanFight{
    void fight();
}
interface CanFight2{
    void fight();
}
interface CanSwim{
    void swim();
}
interface Canfly{
    void fly();
}
class ActionCharacter {
    public void fight() {}
}

class Hero extends ActionCharacter implements CanFight, CanFight2, CanSwim, Canfly {

    /*
    因为在 ActionCharacter 中覆写了 fight()
    所以在这里的 CanFight, CanFight2 的 fight() 冲突也就解决了。
     */

    @Override
    public void swim() {}

    @Override
    public void fly() {}
}

class Adventure {
    public static void t(CanFight x) {
        x.fight();
    }
    public static void u(CanSwim x) {
        x.swim();
    }
    public static void v(Canfly x) {
        x.fly();
    }
    public static void w(ActionCharacter x) {
        x.fight();
    }

    public static void main(String[] args) {
        Hero h = new Hero();
        t(h);
        u(h);
        v(h);
        w(h);
    }
}

/*
【记住！】
使用接口的核心原因之一：
为了能够向上转型为多个基类型（以及由此带来的灵活性）。
第二个原因是：
与使用抽象基类相同：防止客户端程序员创建这个类的对象，确保这仅仅只是一个接口。

那么，如何选择接口还是抽象类呢？
如果创建不带任何方法定义或成员变量的基类，就选择接口而不是抽象类。
事实上，如果知道某事物是一个基类，可以考虑用接口实现它
 */
