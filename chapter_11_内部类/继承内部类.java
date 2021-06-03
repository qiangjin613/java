/*
因为内部类的构造器必须连接到指向其外部类对象的引用，
所以在继承内部类的时候，事情会变得有点复杂。

问题在于，
那个指向外部类对象的“秘密的”引用必须被初始化，
而在派生类中不再存在可连接的默认对象。
 */
class WithInner {
    class Inner {}
}
class InheritInner extends WithInner.Inner {
    InheritInner(WithInner wi) {
        wi.super();
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

提供了必要的引用，然后程序才能编译通过。
 */
