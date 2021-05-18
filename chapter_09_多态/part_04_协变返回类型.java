/**
 * Java 5 中引入了协变返回类型，
 * 这表示派生类的被重写方法可以返回基类方法返回类型的派生类型（如：WheatMill 类的 process() 方法）（返回的类型更具体了）
 *
 * 不仅是对于返回类型，对于访问修饰符，也是做出了限制，被重写方法的方法权限只可以扩大，不可缩小
 * 直接点，就是 Wheat 的 toString() 方法的权限修饰符，只能大于等于其父类的相关方法的权限修饰符
 * （原因之一：在多态中，其父类在使用子类的方法时，要有相关的权限）
 *
 * 4 种修饰符中，只有 private 权限的不允许被重写，也是因为调用的时候没有权限问题
 */
class Grain {
    @Override
    public String toString() {
        return "Grain";
    }
}
class Wheat extends Grain {
    @Override
    public String toString() {
        return "Wheat";
    }
}

class Mill {
    Grain process() {
        return new Grain();
    }
}
class WheatMill extends Mill {
    // 协变返回类型允许返回更具体的 Wheat 类型
    @Override
    Wheat process() {
        return new Wheat();
    }
}
class CovariantReturn {
    public static void main(String[] args) {
        Mill m = new Mill();
        Grain g = m.process();
        System.out.println(g);

        m = new WheatMill();
        // g2 实际上是 Wheat 类型
        Grain g2 = m.process();
        System.out.println(g2);
    }
}
