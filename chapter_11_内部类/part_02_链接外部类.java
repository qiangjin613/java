/*
当生成一个内部类的对象时，
此对象与制造它的外部对象（enclosing object）之间就有了一种联系，
所以它能访问其外部对象的所有成员，而不需要任何特殊条件。
此外，内部类还拥有其外围类的所有元素的访问权。
 */

/**
 * 测试”外部类“权限的例子：
 * （也是“迭代器”设计模式的一个例子）
 */
interface Selector {
    boolean end();
    Object current();
    void next();
}
class Sequence {
    private Object[] items;
    private int next = 0;
    public Sequence(int size) {
        items = new Object[size];
    }
    public void add(Object x) {
        if (next < items.length) {
            items[next++] = x;
        }
    }
    private class SequenceSelector implements Selector {
        private int i = 0;

        @Override
        public boolean end() {
            return i == items.length;
        }

        @Override
        public Object current() {
            return items[i];
        }

        @Override
        public void next() {
            if (i < items.length) {
                i++;
            }
        }
    }
    public Selector selector() {
        return new SequenceSelector();
    }

    public static void main(String[] args) {
        Sequence sequence = new Sequence(10);
        for (int i = 0; i < 10; i++) {
            sequence.add(i + "");
        }
        Selector selector = sequence.selector();
        while (!selector.end()) {
            System.out.print(selector.current() + " ");
            selector.next();
        }
    }
}
class TestDemo {
    public static void main(String[] args) {
        Sequence sequence = new Sequence(10);
        for (int i = 0; i < 10; i++) {
            sequence.add(i + "");
        }
        /*
        虽然 selector 的运行时类型是 private 的 Sequence.SequenceSelector 类，
        但这并不妨碍在其他类中使用，因为_权限修饰符_只是在编译器进行限制
         */
        Selector selector = sequence.selector();
        while (!selector.end()) {
            System.out.print(selector.current() + " ");
            selector.next();
        }
    }
}


/*
Q: 内部类自动拥有对其外部类所有成员的访问权。这是如何做到的呢？
A: 当某个外部类的对象创建了一个内部类对象时，
此内部类对象必定会秘密地捕获一个指向那个外部类对象的引用。
然后，在访问此外部类的成员时，就是用那个引用来选择外部类的成员。
 */

/**
 * 现在可以看到的是，
 * 内部类的对象在与其外部类的对象相关联的情况下被创建
 * （就像你应该看到的，内部类是非 static 类时）。
 * 如果，
 * 内部类与外部类对象无关（是 static 的）情况下，能被创建吗？
 *
 * 如下：是可以的。
 * （这个叫嵌套类（静态内部类））
 */
class TestInnerClass {
    private static class A {}
    public static void main(String[] args) {
        A a = new A();
    }
}
