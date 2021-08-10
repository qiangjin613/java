/*
因为内部类的构造器必须连接到指向其外部类对象的引用，
所以在继承内部类的时候，事情会变得有点复杂。

问题在于，
那个指向外部类对象的“秘密的”引用必须被初始化，
而在派生类中不再存在可连接的默认对象。
 */
class WithInner {
    WithInner() {
        System.out.println("WithInner init");
    }
    WithInner(int i) {
        System.out.println("WithInner init: " + i);
    }


    class Inner {
        Inner() {
            System.out.println("Inner init");
        }
        Inner(int i) {

            /*
            Q：如何在指定默认创建的外部类的构造器？
            A：无法指定。
                因为创建外部类的时候，首先就是创建外部类对象，再创建内部类的对象。
                无法直接创建内部类的对象（无法直接让内部类自己创建外部类对象的引用）。
             */

            System.out.println("Inner init: " + i);
        }
    }
}
class InheritInner extends WithInner.Inner {
    InheritInner(WithInner wi) {
        // 在这里要明确指定使用内部类的构造器。
        // 这里调用的是 WithInner.Inner 的 Inner(int i) 构造器。
        // 猜想：super 是根据 extends 后面的类来走的，wi 是提供了一个
        wi.super(1);
    }

    public static void main(String[] args) {
        WithInner wi = new WithInner();
        InheritInner ii = new InheritInner(wi);
    }
}
/*
在上述例子中，InheritInner 继承自内部类 WithInner.Inner，
当生成一个构造器时，默认的构造器指向的是外部类 WithInner 的引用，
因此必须在构造器中使用 enclosingClassReference.super(); 明确指定使用内部类的构造器。

enclosingClassReference 就是外部类的引用。

提供了必要的引用，然后程序才能编译通过。
 */
