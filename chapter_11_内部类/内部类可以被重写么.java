/*
覆盖 == 重写
重写 != 重写

Q：如果创建了一个内部类，然后继承其外部类并重新定义此内部类时，会发生什么呢？
    也就是说，内部类可以被重写吗？
A：“重写”内部类就好像它是外部类的一个方法，其实并不起什么作用。
 */
class Egg {
    private Yolk y;
    protected class Yolk {
        public Yolk() {
            System.out.println("Egg 的内部类 Yolk 初始化");
        }
    }
    Egg() {
        System.out.println("Egg 初始化");
        y = new Yolk();
    }
}
class BigEgg extends Egg {
    /* @Override 不适用于 class */
    public class Yolk {
        public Yolk() {
            System.out.println("BigEgg 的内部类 Yolk 初始化");
        }
    }

    public static void main(String[] args) {
        new BigEgg();
    }
}
/*
上述例子说明，当继承了某个外部类的时候，内部类并没有发生什么特别神奇的变化。
这两个内部类是完全独立的两个实体，各自在自己的命名空间内。
 */

/**
 * 可以通过一定的技巧和明确继承某个内部类来达成“重写”的效果
 */
class Egg2 {
    protected class Yolk {
        public Yolk() {
            System.out.println("Egg2 的内部类 Yolk");
        }
        public void f() {
            System.out.println("Egg2 的内部类 Yolk 的 f()");
        }
    }

    private Yolk y;

    Egg2() {
        System.out.println("Egg2 初始化");
    }

    public void insertYolk(Yolk yy) {
        y = yy;
    }
    public void g() {
        y.f();
    }
}
class SubEgg2 extends Egg2 {
    public class Yolk extends Egg2.Yolk {
        public Yolk() {
            System.out.println("SubEgg2 的内部类 Yolk 初始化");
        }
        @Override
        public void f() {
            System.out.println("SubEgg2 的内部类 Yolk 覆写的 f() 执行");
        }
    }
    public SubEgg2() {
        /* 在这里使用了 Egg2 的 Yolk 内部类，所以会初始化 Egg2 的 Yolk 内部类 */
        insertYolk(new Yolk());
    }

    public static void main(String[] args) {
        Egg2 e2 = new SubEgg2();
        e2.g();
    }
}
