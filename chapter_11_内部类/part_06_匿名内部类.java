/*
一个简单例子：（使用类的无参构造器或接口）
 */
class Parcel7 {
    public Contents contents() {
        return new Contents() {
            private int i = 11;
            @Override
            public int value() {
                return i;
            }
        };
    }

    public static void main(String[] args) {
        Parcel7 p = new Parcel7();
        Contents c = p.contents();
        System.out.println(c.value());
    }
}

/*
上述匿名内部类的语法是下述形式的简化形式：
 */
class Parcel7b {
    class MyContents implements Contents {
        private int i = 11;
        @Override
        public int value() {
            return i;
        }
    }

    public Contents contents() {
        return new MyContents();
    }

    public static void main(String[] args) {
        Parcel7b p = new Parcel7b();
        Contents c = p.contents();
    }
}

/**
 * 使用含参构造器
 */
class Wrapping {
    private int i;
    public Wrapping(int x) {
        i = x;
    }
    public int value() {
        return i;
    }
}
/*
尽管 Wrapping 只是一个具有具体实现的普通类，但它还是被导出类当作公共“接口”来使用。
 */
class Parcel8 {
    public Wrapping wrapping(int x) {
        /*
        这个就像是一个匿名类 extend Wrapping 的感觉
         */
        return new Wrapping(x) {
            @Override
            public int value() {
                return super.value() * 47;
            }
        };
    }

    public static void main(String[] args) {
        Parcel8 p = new Parcel8();
        Wrapping w = p.wrapping(11);
        System.out.println(w.value());
    }
}


/*
在匿名类中定义字段时，还能够对其执行初始化操作：
 */
class Parcel9 {
    public Destination destination(final String dest) {
        return new Destination() {
            /* 匿名内部类中初始化字段 */
            private String label = dest;
            @Override
            public String readLabel() {
                return dest;
            }
        };
    }

    public static void main(String[] args) {
        Parcel9 p = new Parcel9();
        Destination d = p.destination("tt");
    }
}


/*
注意到上例有一个不一样的地方：
如果在定义一个匿名内部类时，
它要使用一个外部环境（在本匿名内部类之外定义）对象，
那么编译器会要求其（该对象）参数引用是 final 或者是 “effectively final”
（也就是说，该参数在初始化后不能被重新赋值，所以可以当作 final 的）

在上例中，dest 如果被内部类使用了，
在没有被 final 修饰时，不允许再改变 dest 的值（这个时候是 effectively final）
（即使不加 final，Java 8 的编译器也会为我们自动加上 final）
或者 dest 直接被 final 修饰
 */



/*
Q：如果为内部类添加一些类似构造器的行为，该怎么办呢？
A：虽然匿名内部类中不可能有构造器（因为它根本没名字）。但别忘了，还有初始化块。
 */
abstract class Base {
    Base(int i) {
        System.out.println("Base constructor, i = " + i);
    }
    public abstract void f();
}
class AnonymousConstructor {
    public static Base getBase(int i) {
        i++;
        return new Base(i) {
            {
                System.out.println("匿名内部类的初始化块");
            }
            @Override
            public void f() {
                System.out.println("匿名内部类的 f()");
            }
        };
    }

    public static void main(String[] args) {
        Base b = getBase(47);
        b.f();
    }
}

/*
值得注意的是，上例中的 i 不要求是 final 的。
因为 i 被传递给匿名类的基类的构造器，它并不会在匿名类内部被直接使用。
 */


/*
又一个例子
 */
class Parcel10 {
    public Destination destination(final String dest, final float price) {
        return new Destination() {
            private String label = dest;
            private int cost;
            // 实例初始化：
            {
                cost = Math.round(price);
                if (cost > 100) {
                    System.out.println("太大了，超额了");
                }
            }
            @Override
            public String readLabel() {
                return label;
            }
        };
    }

    public static void main(String[] args) {
        Parcel10 p = new Parcel10();
        Destination d = p.destination("ss", 1000.3F);
    }
}

/*
对于匿名类而言，实例初始化的实际效果就是构造器。
（当然它受到了限制-你不能重载实例初始化方法，所以你仅有一个这样的构造器）
 *
匿名内部类与正规的继承相比有些受限，
因为匿名内部类要么继承类，要么实现接口，但是不能两者兼备。
而且如果是实现接口，也只能实现一个接口。
 */
