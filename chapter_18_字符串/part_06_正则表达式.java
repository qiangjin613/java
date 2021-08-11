/*
【基础】
一般来说，正则表达式就是以某种方式来描述字符串，
因此你可以说：“如果一个字符串含有这些东西，那么它就是我正在找的东西。”
 */
class IntegerMatch {
    public static void main(String[] args) {
        String simpleRegex = "-?\\d+";
        System.out.println("-123".matches(simpleRegex)); // true
        System.out.println("+123.32".matches(simpleRegex)); // false
        System.out.println("-123.32".matches(simpleRegex)); // false
        System.out.println("123.32".matches(simpleRegex)); // false

        String intRegex = "[-+]?\\d+"; /* 等价于 (-|\\+)?\\d+ */
        System.out.println("-123".matches(intRegex)); // true
        System.out.println("+123".matches(intRegex)); // true
        System.out.println("-123.32".matches(intRegex)); // false
        System.out.println("123.32".matches(intRegex)); // false
    }
}

class Splitting {
    public static String knights = "Then, when you have found the shrubbery, " +
            "you must cut down the mightiest tree in the " +
            "forest...with... a herring!";

    public static void split(String regex) {
        for (String s : knights.split(regex)) {
            System.out.print(s + "_");
        }
        System.out.println();
    }
    public static void split(String regex, int limit) {
        for (String s : knights.split(regex, limit)) {
            System.out.print(s + "_");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        split(" ");
        split("\\W+");
        split("n\\W+");

        System.out.println();

        /* limit 代表将字符串分割为多少个字串 */
        split(" ", 1); /* 表示不分割字串 */
        split("\\W+", 2);
        split("n\\W+", 3);
    }
}

class Replacing {
    static String s = Splitting.knights;
    public static void main(String[] args) {
        System.out.println(s);
        System.out.println(s.replaceFirst("f\\w+", "---"));
        System.out.println(s.replaceFirst("shrubbery|tree|forest|herring", "***"));

        System.out.println(s.replaceAll("f\\w+", "---"));
        System.out.println(s.replaceAll("shrubbery|tree|forest|herring", "***"));
    }
}
/*
与正则表达式相关的 String 方法有：
boolean matches(String regex)

String[] split(String regex)
String[] split(String regex, int limit)

String replaceFirst(String regex, String replacement)
String replaceAll(String regex, String replacement)
 */

/*
通常，比起功能有限的 String 类，我们更愿意构造功能强大的正则表达式对象。
这些对象在 java.util.regex 包中。
 */
