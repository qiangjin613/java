/*
嵌套类，指将内部类声明为 static。内部类对象与其外部类对象之间没有联系

想要理解 static 应用于内部类时的含义，
就必须记住，普通的内部类对象隐式地保存了一个引用，指向创建它的外部类对象。
然而，当内部类是 static 的时，就不是这样了。

嵌套类是 static 的，也就意味着：
1. 创建嵌套类的对象时，不需要其外部类的对象。
2. 不能从嵌套类的对象中访问非静态的外部类对象。

嵌套类与普通的内部类还有一个区别：
普通内部类的字段与方法，只能放在类的外部层次上，
所以普通的内部类不能有 static 数据和 static 字段，也不能包含嵌套类。
但是嵌套类可以包含所有这些东西。
 */
class Parcel11 {
    private static class ParcelContents implements Contents {
        /* 嵌套内中不能使用 this 链接到其外部类对象 */
        // Parcel11.this;

        private int i = 11;
        @Override
        public int value() {
            return i;
        }
    }

    protected static final class ParcelDestination implements Destination {
        private String label;

        public ParcelDestination(String label) {
            this.label = label;
        }

        @Override
        public String readLabel() {
            return label;
        }
        /* 嵌套类可以包含其他元素 */
        public static void f() {}
        static int x = 10;
        static class AnotherLevel {
            public static void f() {}
            static int x = 10;
        }
    }

    public static Contents contents() {
        return new ParcelContents();
    }

    public static Destination destination(String s) {
        return new ParcelDestination(s);
    }

    public static void main(String[] args) {
        Contents c = contents();
        Destination d = destination("ss");
    }
}


/*
【接口内部的类】
嵌套类可以作为接口的一部分。
（放到接口中的任何类都自动地是 public 和 static 的，也就是嵌套类）
因为类是 static 的，只是将嵌套类置于接口的命名空间内，这并不违反接口的规则。
（接口的规则是？ ...）

有趣的一个示例：（接口自己的类实现自己）
 */
interface ClassInterface {
    void howdy();
    class Test implements ClassInterface {
        @Override
        public void howdy() {
            System.out.println("接口的内部类的 howdy()");
        }

        public void f() {
            System.out.println("接口的内部类的 f()");
        }

        public static void main(String[] args) {
            Test test = new Test();
            test.f();
        }
    }
}

/*
如果想要创建某些公共代码，
使得它们可以被某个接口的所有不同实现所共用，
那么使用接口内部的嵌套类会显得很方便。
 */

/*
对于在每个类中都编写一个 main() 用于测试的建议，
这样有一个缺点，
 */
class TestBed {
    public void f() {
        System.out.println("f()");
    }
    /*
    这个类在测试的时候并不会被编译到一起，而是生成了一个独立的类 TestBed$Tester。
    可以使用这个类进行测试，但在发布的产品中把这个 TestBed$Tester.class 删除掉后，
    是不应响正常流程的。
     */
    public static class Tester {
        public static void main(String[] args) {
            TestBed t = new TestBed();
            t.f();
        }
    }
}


/*
【从多层嵌套类中访问外部类的成员】
一个内部类被嵌套多少层并不重要，
它能透明地访问所有它所嵌入的外部类的所有成员
 */
class MNA {
    private void f() {}

    /* 内部类 */
    private class B {
        private void u() {}
    }

    /* 内部类 */
    class A {
        private void g() {}
        /* 内部类的内部类 */
        public class B {
            void h() {
                g();
                f();
                /* 也能调用 private B 的 private u() 方法 */
                new MNA.B().u();
            }
        }
    }
}
class MultiNestingAccess {
    public static void main(String[] args) {
        MNA mna = new MNA();
        MNA.A mnaa = mna.new A();
        MNA.A.B mnaab = mnaa.new B();
        mnaab.h();

        /* 无法使用 */
        // mna.new B();
    }
}

/*
【小结】
对于匿名内部类的权限修饰符需要总结一下的，是有规律的！
 */
