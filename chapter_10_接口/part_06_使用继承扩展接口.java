/*
简单来说，就是接口的 ”继承“ 行为

如下所示：
 */

interface Monster {
    void menace();
}
interface DangerousMonster extends Monster {
    void destroy();
}
interface Lethal {
    void kill();
}
class DragonZilla implements DangerousMonster {
    @Override
    public void menace() {}

    @Override
    public void destroy() {}
}

interface Vampire extends DangerousMonster, Lethal {
    void drinkBlood();
}
class VeryBadVampire implements Vampire {

    @Override
    public void menace() {}

    @Override
    public void destroy() {}

    @Override
    public void kill() {}

    @Override
    public void drinkBlood() {}
}
class HorrorShow {
    static void u(Monster m) {
        m.menace();
    }

    static void v(DangerousMonster d) {
        d.menace();
        d.destroy();
    }

    static void w(Lethal l) {
        l.kill();
    }

    public static void main(String[] args) {
        DangerousMonster b = new DragonZilla();
        u(b);
        v(b);
        Vampire v = new VeryBadVampire();
        u(v);
        v(v);
        w(v);
    }
}


/*
【结合接口时的命名冲突】
当实现多个接口时可能会存在一个小陷阱：方法冲突（这一点在 default 方法中提到过）
 */
interface I1 {
    void f();
}

interface I2 {
    int f(int i);
}

interface I3 {
    int f();
}

class C {
    public int f() {
        return 1;
    }
}

class C2 implements I1, I2{

    @Override
    public void f() {}

    @Override
    public int f(int i) {
        return 1;
    }
}

class C3 extends C implements I2 {
    @Override
    public int f(int i) {
        return 1;
    }
}

class C4 extends C implements I3 {
    @Override
    public int f() {
        return 1;
    }
}

/**
 * 因方法的返回值不同，但签名相同，造成的错误，编译器报错
 * 【这就是”多继承“的BUG】
 */
//class C5 extends C implements I1 {}
//class C6 extends C implements I1, I3 {}


/*
覆写、实现和重载令人不快地搅和在一起带来了困难。
同时，重载方法仅根据返回类型是区分不了的。

当打算组合接口时，
在不同的接口中使用相同的方法名通常会造成代码可读性的混乱，
尽量避免这种情况。
 */
