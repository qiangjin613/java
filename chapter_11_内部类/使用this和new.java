/**
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
 * .new 使用的例子：
 */
class DotNew {
    class Inner {}

    public static void main(String[] args) {
        DotNew dn = new DotNew();
        DotNew.Inner inner = dn.new Inner();
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
    class Destination {
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
        Parcel3.Contents c = p.new Contents();
        Parcel3.Destination d = p.new Destination("Ta");
    }
}

/*
Q: 为什么说在拥有外部类对象之前是不可能创建内部类对象的？
A: 这是因为内部类对象会暗暗地连接到建它的外部类对象上。
    但是，如果你创建的是嵌套类（静态内部类），
    那么它就不需要对外部类对象的引用。
 */
