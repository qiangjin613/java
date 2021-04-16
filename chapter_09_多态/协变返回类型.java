/**
 * Java 5 中引入了协变返回类型，
 * 这表示派生类的被重写方法可以返回基类方法返回类型的派生类型（如：WheatMill 类的 process() 方法）
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
