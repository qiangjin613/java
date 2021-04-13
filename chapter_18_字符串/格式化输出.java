import java.io.PrintStream;
import java.text.Format;
import java.util.Formatter;

/**
 * 【printf()、System.out.format()】
 */
class SimpleFormat {
    public static void main(String[] args) {
        int x = 5;
        double y = 5.322342;
        // [1] The old way:
        System.out.println("Row 1: [" + x + " " + y + "]");
        // [2] The new wy:
        System.out.format("Row 1: [%d %f]%n", x, y);
        // [3] or
        System.out.printf("Row 1: [%d %f]%n", x, y);
        // [4] or
        System.out.println(String.format("Row 1: [%d %f]%n", x, y));
    }
    // [1]、[2]、[3] 是等价的，或者使用 String.format() 格式化字符串
}


/**
 * 【Formatter 类】
 * 在 Java 中，所有的格式化功能都是由 java.util.Formatter 类处理
 */
class Turtle {
    private String name;
    private Formatter f;
    public Turtle(String name, Formatter f) {
        this.name = name;
        this.f = f;
    }
    public void move(int x, int y) {
        // Formatter 可以看作一个翻译器，将格式化字符串与数据翻译成需要的结果
        f.format("%s The Turtle is at (%d, %d)%n", name, x, y);
    }

    public static void main(String[] args) {
        PrintStream outAlias = System.out;
        // 当创建一个 Formatter 对象时，需要向构造器传递一些信息，
        // 告诉它最终的结果将向哪里输出（控制台、文件、OutputStream 等）
        Turtle tommy = new Turtle("Tommy", new Formatter(System.out));
        Turtle terry = new Turtle("Terry", new Formatter(outAlias));
        tommy.move(0, 0);
        terry.move(4, 8);
    }
}


/**
 * 【格式化修饰符】
 * 通用语法：%[argument_index$][flags][width][.precision]conversion
 * 购物收据案例
 */
class ReceiptBuilder {
    private double total = 0;
    // 将“翻译”结果输出到 StringBuilder 中
    private Formatter f = new Formatter(new StringBuilder());
    public ReceiptBuilder() {
        f.format("%-15s %5s %10s%n", "Item", "Qty", "Price");
        f.format("%-15s %5s %10s%n", "----", "---", "-----");
    }
    public void add(String name, int qty, double price) {
        f.format("%-15.15s %5d %10.2f%n", name, qty, price);
        total += price * qty;
    }
    public String build() {
        f.format("%-15s %5s %10.2f%n", "Tax", "", total * 0.06);
        f.format("%-15s %5s %10s%n", "", "", "-----");
        f.format("%-15s %5s %10.2f%n", "Total", "", total * 1.06);
        return f.toString();
    }

    public static void main(String[] args) {
        ReceiptBuilder rb = new ReceiptBuilder();
        rb.add("Jack's Magic Beans", 4, 4.25);
        rb.add("Princess Peas", 3, 5.1);
        rb.add("Three Bears Porridge", 1, 14.29);
        System.out.println(rb.build());
    }
}

/**
 * 【Formatter 转换】
 *      %d  整型（十进制）
 *      %x  整形（十六进制）
 *
 *      %f  浮点数（十进制）
 *      %e  浮点数（科学计数）
 *
 *      %c  Unicode字符
 *      %b  Boolean值
 *      %s  字符串
 *
 *      %n  换行符（与 \n 效果相同）
 *      %h  散列码（十六进制）
 */
class Conversion {
    public static void main(String[] args) {
        Formatter f = new Formatter(System.out);

        char u = 'a';
        System.out.println("u = 'a'");
        f.format("s: %s%n" ,u);
        f.format("c: %c%n" ,u);
        // [1] 针对无效的转换，会触发 IllegalFormatConversionException 异常
        // f.format("d: %d%n" ,u);
        // [2] 对不是 boolean 类型的参数，只要该参数不为 null，其转换结果永远都是 true
        // 这一点在其他语言中（包括 C），往往转换为 false
        f.format("b: %b%n", u);
        f.format("h: %h%n", u);

        double d = 180.99999;
        System.out.println("d = 180.99999");
        f.format("f: %f%n", d);
        f.format("e: %e%n", d);
        f.format("h: %h%n", d);
        // f.format("x: %x%n", d);

        int i = 16;
        System.out.println("i = 16");
        f.format("x: %x%n", i);
        f.format("h: %h%n", i);
    }
}


/**
 * 【String。format()】
 * 接受与 Formatter.format() 方法一样的参数，但返回一个 String 对象。
 * 当只需使用一次 format() 方法的时候，String.format() 用起来很方便。
 */
class DatabaseException extends Exception {
    public DatabaseException(int transactionID, int queryID, String message) {
        super(String.format("(t%d, q%d) %s", transactionID, queryID, message));
        // 在 String.format() 内部，也是创建了一个 Formatter 对象
    }

    public static void main(String[] args) {
        try {
            throw new DatabaseException(3, 7, "Write failed");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}