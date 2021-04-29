/*
"instanceof 的形式（instanceof和isInstance()）
    V.S.
与Class对象直接比较"
存在重要区别！
 */
class Base2 {}
class Derived2 extends Base2 {}
class FamilyVsExactType {
    static void test(Object x) {
        System.out.println("Testing x of type " + x.getClass());
        System.out.println("x instanceof Base2 " + (x instanceof Base2));
        System.out.println(
                "x instanceof Derived2 " + (x instanceof Derived2));
        System.out.println(
                "Base2.isInstance(x) " + Base2.class.isInstance(x));
        System.out.println(
                "Derived2.isInstance(x) " +
                        Derived2.class.isInstance(x));
        System.out.println(
                "x.getClass() == Base2.class " +
                        (x.getClass() == Base2.class));
        System.out.println(
                "x.getClass() == Derived2.class " +
                        (x.getClass() == Derived2.class));
        System.out.println(
                "x.getClass().equals(Base2.class)) "+
                        (x.getClass().equals(Base2.class)));
        System.out.println(
                "x.getClass().equals(Derived2.class)) " +
                        (x.getClass().equals(Derived2.class)));
    }

    public static void main(String[] args) {
        test(new Base2());
        System.out.println();
        test(new Derived2());
    }
}

/*
测试 Class 对象的相等性：
instanceof 的形式：产生的结果相同
== 和 equals()：产生的结果也相同。

但 instanceof 判断有没有派生关系，
而 == 和 equals() 与继承无关（要么是确定的类型，要么不是）
 */
