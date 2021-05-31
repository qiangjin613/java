/**
 * 一个内部类的入门实例
 */
class Parcel1 {
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

    public void ship(String dest) {
        Contents c = new Contents();
        Destination d = new Destination(dest);
        System.out.println(d.readLabel());
    }

    public static void main(String[] args) {
        Parcel1 p = new Parcel1();
        p.ship("Tasmania");
    }
}

/*
在 ship() 方法里面使用内部类的时候，与使用普通类没什么不同。
代码上来看，最明显的区别是内部类的位置在普通类里面定义的。
 */

/**
 * 更典型的情况是：
 * 外部类将有一个方法 to()、contents()，
 * 该方法返回一个指向内部类的引用 Contents、Destination。
 */
class Parcel2 {
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

    public Contents contents() {
        return new Contents();
    }

    public Destination to(String s) {
        return new Destination(s);
    }

    public void ship(String dest) {
        Contents c = new Contents();
        Destination d = new Destination(dest);
        System.out.println(d.readLabel());
    }

    public static void main(String[] args) {
        Parcel2 p = new Parcel2();
        p.ship("Tasmania");

        Parcel2 q = new Parcel2();
        // 使用内部类的引用创建内部类：
        Parcel2.Contents contents = q.contents();
        Parcel2.Destination bob = q.to("Bob");
        /* 或者 */
        Contents contents2 = q.contents();
        Destination bob2 = q.to("Bob");
    }
}

/*
说明：
在外部类的静态方法中：
    可以具体地指明这个对象的类型 OuterClassName.InnerClassName
    也可以直接指明类型 InnerClassName

但是，在其他类中创建内部类：
    需要具体地指明这个对象的类型 OuterClassName.InnerClassName
 */
class TestParcel2 {
    public static void main(String[] args) {
        Parcel2 q = new Parcel2();
        // 使用内部类的引用创建内部类：
        Parcel2.Contents contents = q.contents();
        Parcel2.Destination bob = q.to("Bob");
        /* 不可以省略外部类的类型 */
        // Contents contents2 = q.contents();
        // Destination bob2 = q.to("Bob");
    }
}
