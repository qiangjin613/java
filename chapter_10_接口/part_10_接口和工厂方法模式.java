/*
接口是多实现的途径，生成符合某个接口的对象的典型方式是工厂方法设计模式。

对于工厂方法设计模式而言，只需调用工厂对象中的创建方法就能生成对象的实现，
理论上，通过这种方式可以将接口与实现的代码完全分离，
使得可以透明地将某个实现替换为另一个实现。


Q：“接口 + 工厂方法”的组合是否能通过 RTTI 反射的考验呢？
A：不能，还是可以通过 RTTI 顺藤摸瓜找到具体的运行时信息。
 */

interface Service {
    void method1();
    void method2();
}
interface ServiceFactory {
    Service getService();
}

class Service1 implements Service {
    public Service1() {}

    @Override
    public void method1() {
        System.out.println("Service1 的 method1()");
    }

    @Override
    public void method2() {
        System.out.println("Service1 的 method2()");
    }
}
class Service1Factory implements ServiceFactory {
    @Override
    public Service getService() {
        return new Service1();
    }
}

class Service2 implements Service {
    public Service2() {}

    @Override
    public void method1() {
        System.out.println("Service2 的 method1()");
    }

    @Override
    public void method2() {
        System.out.println("Service2 的 method2()");
    }
}
class Service2Factory implements ServiceFactory {
    @Override
    public Service getService() {
        return new Service2();
    }
}

class Factories {
    public static void serviceConsumer(ServiceFactory fact) {
        Service s = fact.getService();
        s.method1();
        s.method2();
        System.out.println("ServiceFactory 产生的具体对象信息："
                + fact.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        serviceConsumer(new Service1Factory());
        serviceConsumer(new Service2Factory());
    }
}


/*
Q: 为什么要添加额外的间接层呢？
A: 一个常见的原因是创建框架。

如下，一个游戏系统（在相同的棋盘下国际象棋和西洋跳棋）：
 */
interface Game2 {
    boolean move();
}
interface Game2Factory {
    Game2 getGame2();
}

// 西洋跳棋：
class Checkers2 implements Game2 {
    private int moves = 0;
    private static final int MOVES = 3;

    @Override
    public boolean move() {
        System.out.println("Checkers2 move " + moves);
        return ++moves != MOVES;
    }
}
class Checkers2Factory implements Game2Factory {
    @Override
    public Game2 getGame2() {
        return new Checkers2();
    }
}

// 国际象棋：
class Chess2 implements Game2 {
    private int moves = 0;
    private static final int MOVES = 4;

    @Override
    public boolean move() {
        System.out.println("Chess2 move " + moves);
        return ++moves != MOVES;
    }
}
class Chess2Factory implements Game2Factory {
    @Override
    public Game2 getGame2() {
        return new Chess2();
    }
}

// 开始游戏：
class Games {
    public static void playGme(Game2Factory factory) {
        Game2 s = factory.getGame2();
        System.out.println("play: " + s.getClass().getName());
        while (s.move()){}
    }

    public static void main(String[] args) {
        playGme(new Checkers2Factory());
        playGme(new Chess2Factory());
    }
}

/*
接口的工厂模式就是这样了，在内部类中，将会使用更加优雅的方式实现工厂模式。
 */
