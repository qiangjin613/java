/**
 * 【使用 .this】
 * 如果需要使用外部类对象，可以使用 OuterClass.this 来获得外部类对象
 * （类型检查，在编译期就被知晓并受到检查，因此没有任何运行时开销）
 */
class DotThis {
    void f() {
        System.out.println("外部类 DotThis 的 f()");
    }
    class Inner {
        public DotThis outer() {
            return DotThis.this;
            /* 如果使用 return this; 则返回的是内部类 Inner 的对象引用 */
        }
    }
    public Inner inner() {
        return new Inner();
    }

    public static void main(String[] args) {
        DotThis dt = new DotThis();
        DotThis.Inner dti = dt.inner();
        dti.outer().f();
    }
}

/**
 * 【使用 .new】
 */
class DotNew {
    class Inner {}

    public static void main(String[] args) {
        DotNew dn = new DotNew();
        DotNew.Inner inner = dn.new Inner();

        DotThis dt = new DotThis();
        DotThis.Inner dti = dt.new Inner();
        dti.outer().f();
    }
}

/*
要想直接创建内部类的对象，
不能按照你想象的方式，去引用外部类的名字 DotNew，
而是必须使用外部类的对象来创建该内部类对象。

错误的写法：dn.new DotNew.Inner
 */

/**
 * 将 .new 应用于 Parcel 的示例：
 */
class Parcel3 {
    class Contents {
        private int i = 11;
        public int value() {
            return i;
        }
    }
    private class Destination {
        private String label;
        Destination(String whereTo) {
            label = whereTo;
        }
        String readLabel() {
            return label;
        }
    }
    public static void main(String[] args) {
        Parcel3 p = new Parcel3();

        /*
        可以看到，
        在Contents、Destination 类外部，
        也可以使用他们的 private 成员。
        这是为何？
         */
        Parcel3.Contents pc = p.new Contents();
        pc.i++;
        System.out.println(pc.value());

        Parcel3.Destination pd = p.new Destination("Ta");
        pd.label += ".";
        System.out.println(pd.readLabel());
    }
}

class Test {
    public static void main(String[] args) {
        Parcel3 p = new Parcel3();

        /*
        可以看到，
        在Contents、Destination 类外部，
        也可以使用他们的 private 成员。
        这是为何？
         */
        Parcel3.Contents pc = p.new Contents();
        // 在这里并不能像上面的 Parcel3 外部类一样，肆无忌惮地调用其内部类的 private 成员
        // pc.i++;
        System.out.println(pc.value());

        // 不可以在 Test 包中创建 Parcel3 的 private 的内部类 Destination
//        Parcel3.Destination pd = p.new Destination("Ta");
//        pd.label += ".";
//        System.out.println(pd.readLabel());
    }
}

/*
Q: 为什么说在拥有外部类对象之前是不可能创建内部类对象的？
A: 这是因为内部类对象会暗暗地连接到建它的外部类对象上。
    但是，如果你创建的是嵌套类（静态内部类），
    那么它就不需要对外部类对象的引用。
 */
