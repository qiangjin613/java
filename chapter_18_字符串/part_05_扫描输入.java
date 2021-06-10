import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.MatchResult;

/**
 * 一般的解决办法：一行一行地读取文本，就像从控制台读入标准输入一样
 */
class SimpleRead {
    // StringReader 将 String 转化为可读的流对象，在用这个对象构造 BufferedReader 对象
    public static BufferedReader input =
            new BufferedReader(new StringReader("Sir Robin of Camelot\n22 1.61803"));

    public static void main(String[] args) {
        try {
            System.out.println("What is your name?");
            String name = input.readLine();
            System.out.println(name);

            System.out.println("How old are you? What is your favorite double?");
            System.out.println("(input: <age> <double>)");
            String numbers = input.readLine();
            System.out.println(numbers);
            String[] numArray = numbers.split(" ");
            int age = Integer.parseInt(numArray[0]);
            double favorite = Double.parseDouble(numArray[1]);
            System.out.format("Hi %s.%n", name);
            System.out.format("In 5 years you will be %d.%n", age + 5);
            System.out.format("My favorite double is %f.", favorite / 2);
        } catch (IOException e) {
            System.out.println("I/O exception");
        }
    }
}

/**
 * （Java SE5 新增 Scanner 类）
 * 使用 Scannr 大大减轻扫描输入的工作负担，有 nextLint()、nextXxxx() 方法
 */
class BetterRead {
    public static void main(String[] args) {
        // Scanner 的构造器可以接收任意类型的输入对象（包括 File、InputStream、String、Readable 等）
        Scanner stdin = new Scanner(SimpleRead.input);
        System.out.println("What is your name?");
        String name = stdin.nextLine();
        System.out.println(name);

        System.out.println("How old are you? What is your favorite double?");
        System.out.println("(input: <age> <double>)");
        int age = stdin.nextInt();
        double favorite = stdin.nextDouble();
        System.out.println(age);
        System.out.println(favorite);
        System.out.format("Hi %s.%n", name);
        System.out.format("In 5 years you will be %d.%n", age + 5);
        System.out.format("My favorite double is %f.", favorite / 2);
    }
}
/*
在上述代码中，没有用 try 捕获 IOException，
因为 Scanner 有一个假设，在输入结束时会抛出 IOException，
所以 Scanner 会把 IOException 吞掉。
 */




/**
 * 【Scanner 分隔符】
 * 默认情况下，使用空白字符对输入进行分词，可以使用正则表达式指定自己所需的分隔符。
 */
class ScannerDelimiter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner("12, 42, 78, 99, 42");
        /* delimiter() 获取当前分隔符 */
        System.out.println("'" + scanner.delimiter() + "'");

        /* useDelimiter 设置 Scanner 的分隔符 */
        scanner.useDelimiter("\\s*,\\s*");
        while (scanner.hasNextInt()) {
            System.out.println(scanner.nextInt());
        }
        System.out.println("当前分隔符使用的 Pattern 对象：" + scanner.delimiter());
    }
}


/**
 * 【用正则表达式扫描】
 * 针对扫描复杂数据时，可以使用自定义的正则表达式进行扫描
 */
class ThreatAnalyzer {
    static String threatData =
            "58.27.82.161@08/10/2015\n" +
            "204.45.234.40@08/11/2015\n" +
            "58.27.82.161@08/11/2015\n" +
            "58.27.82.161@08/12/2015\n" +
            "58.27.82.161@08/12/2015\n" +
            "[Next log section with different data format]";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(threatData);
        String pattern = "(\\d+[.]\\d+[.]\\d+[.]\\d+)@(\\d{2}/\\d{2}/\\d{4})";
        while (scanner.hasNext(pattern)) {
            scanner.next(pattern);
            /* 获取正则表达式的组数据 */
            MatchResult match = scanner.match();
            String ip = match.group(1);
            String date = match.group(2);
            System.out.format("Threat on %s from %s%n", date, ip);
        }
    }
}


/**
 * 【StringTokenizer 类】
 * StringTokenizer 与 正则表达式、Scanner 进行对比
 *
 * 使用正则表达式或 Scanner 对象，我们能够以更加复杂的模式来分割一个字符串，
 * 而这对于 StringTokenizer 来说就很困难了。
 *
 * 基本上，我们可以放心地说，StringTokenizer 已经可以废弃不用了!
 */
class ReplacingStringTokenizer {
    public static void main(String[] args) {
        // [1] 使用 StringTokenizer
        String input = "But I'm not dead yet! I feel happy!";
        StringTokenizer stoke = new StringTokenizer(input);
        while (stoke.hasMoreElements()) {
            System.out.print(stoke.nextToken() + " ");
        }
        System.out.println();

        // [2] 使用正则表达式
        System.out.println(Arrays.toString(input.split(" ")));

        // [3] 使用 Scanner
        Scanner scanner = new Scanner(input);
        while (scanner.hasNext()) {
            System.out.print(scanner.next() + " ");
        }
    }
}