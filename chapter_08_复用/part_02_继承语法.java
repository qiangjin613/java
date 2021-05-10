/**
 * 在继承中，正确初始化基类子对象是重要且必要的一个环节。
 * （编译器强制初始化基类，并要求在构造函数的开头就初始化基类）
 */

class Cleanser {
    // 虽然 s 是 privte 的，但依旧可以通过一些方法在其他类里改变 s
    private String s = "Cleanser";
    public void append(String a) {
        s += a;
    }
    public void apply() {
        append(" apply()");
    }

    @Override
    public String toString() {
        return "Cleanser{" +
                "s='" + s + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Cleanser c = new Cleanser();
        c.apply();
        System.out.println(c);
    }
}
class Detergent extends Cleanser {
    @Override
    public void apply() {
        append(" Detergent.apply()");
        super.apply(); // 使用 super 调用基类版本，防止递归调用
    }
    // 添加新方法：
    public void foam() {
        append(" foam()");
    }

    public static void main(String[] args) {
        Detergent d = new Detergent();
        d.apply();
        d.foam();
        System.out.println(d);
        System.out.println("Testing base class:");
        Cleanser.main(args);
    }
}


/**
 * 【初始化基类】
 * 必须正确初始化基类子对象，而且只有一种方法可以保证这一点：
 *      通过调用基类构造函数在构造函数中执行初始化，
 *      并且该构造函数中具有执行基类初始化所需的所有适当信息和特权。
 *（在构造器中必须显式/隐式地调用其基类的构造器）
 *
 * Java 自动在派生类构造函数中插入对基类构造函数的调用。
 */
class Art {
    Art() {
        System.out.println("Art constructor");
    }
}
class Drawing extends Art {
    Drawing() {
        System.out.println("Drawing constructor");
    }
}
class Cartoon extends Drawing {
    Cartoon() {
        System.out.println("Cartoon constructor");
    }

    public static void main(String[] args) {
        Cartoon x = new Cartoon();
    }
}


/**
 * 【带参数的构造函数 - super】
 * 对于无参构造器，编译器很容易调用这些构造器，因为不需要调用参数
 * 在没有无参构造器，或者必须调用带参数的构造器的情况下，必须使用 super 关键字和适当的参数列表显式调用基类的构造器。
 */
class Game {
    Game(int i) {
        System.out.println("Game constructor: i = " + i);
    }
}
class BoardGame extends Game {
    BoardGame() {
        super(47);
        System.out.println("BoardGame constructor");
    }
    BoardGame(int i) {
        super(i);
        System.out.println("BoardGame constructor: i = " + i);
    }
}
class Chess extends BoardGame {
    Chess() {
        // [1] 使用基类带参的构造器
        super(11);
        System.out.println("Chess constructor");
    }
    public static void main(String[] args) {
        Chess x = new Chess();
    }
}
class Gobang extends BoardGame {
    Gobang(int i) {
        // [2] 使用基类无参构造器（可以省略，默认使用基类的无参构造器）
        super();
        System.out.println("Gobang constructor: i = " + i);
    }
    public static void main(String[] args) {
        Gobang g = new Gobang(47);
    }
}