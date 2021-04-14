/**
 * 每个类的编译代码都存在与自己独立的文件中。
 * 该文件只有在使用程序代码时才会被加载。
 *
 * 一般可以说“类的代码在首次使用时加载“。
 * 通常是指创建类的第一个对象，或者是访问了类的 static 属性或方法。（构造器也是一个 static 方法尽管它的 static 关键字是隐式的）
 * 因此，准确地说，一个类当它任意一个 static 成员被访问时，就会被加载。
 *
 * 首次使用时就是 static 初始化发生时。
 * 所有的 static 对象和 static 代码块在加载时按照文本顺序依次初始化。
 * static 变量只被初始化一次。
 *
 * 初始化顺序的总结：
 *      ... ...
 */
class Insect {
    private int i = 9;
    protected int j;

    Insect() {
        System.out.println("i = " + i + ", j = " + j);
        j = 39;
    }

    private static int x1 = printInit("static Insect.x1 initialized");

    static int printInit(String s) {
        System.out.println(s);
        return 47;
    }
}

class Beetle extends Insect {
    private int k = printInit("Beetle.k.initialized");

    public Beetle() {
        System.out.println("k = " + k);
        System.out.println("j = " + j);
    }

    private static int x2 = printInit("static Beetle.x2 initialized");

    public static void main(String[] args) {
        System.out.println("Beetle constructor");
        Beetle b = new Beetle();
    }
}