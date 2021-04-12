import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * 在考虑所有可能失败的方法时，找出放置所有 try-catch-finally 块的位置变得令人生畏。
 * 确保没有任何故障路径，使系统远离不稳定状态，这非常具有挑战性。
 */

/**
 * InputFile.java 一个更好的实现方式：
 * getLines() 全权负责打开文件并创建 Stream
 */
class InputFile2 {
    private String fname;
    public InputFile2(String fname) {
        this.fname = fname;
    }

    public Stream<String> getLines() throws IOException {
        return Files.lines(Paths.get(fname));
    }

    public static void main(String[] args) throws IOException {
        new InputFile2("TryWithResources用法.java").getLines()
                .skip(15)
                .limit(1)
                .forEach(System.out::println);
    }
}

/**
 * 一个棘手的样板代码：
 */
class MessyException {
    public static void main(String[] args) {
        InputStream in = null;
        try {
            in = new FileInputStream(new File("MessyException.java"));
            int contents = in.read();
            // do sth.
        } catch (IOException e) {
            // 处理异常
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // 处理异常
                }
            }
        }
    }
}


/**
 * 当 finally 子句有自己的 try 块时，感觉事情变得过于复杂。
 * 幸运的是，Java 7 引入了 try-with-resources 语法，它可以非常清楚地简化上面的代码
 */
class TryWithResources {
    public static void main(String[] args) {
        try(
                InputStream in = new FileInputStream(new File("MessyException.java"))
        ) {
            int contents = in.read();
            // do sth.
        } catch (IOException e) {
            // 处理错误
        }
    }
}

/**
 * try-with-resources 语法的其他特性
 */
class StreamsAreAutoCloseable {
    public static void main(String[] args) throws IOException {
        // try-with-resources 里面的 try 语句块可以不包含 catch 或者 finally 语句而独立存在
        try(
                // 资源规范头中可以包含多个定义，并且通过分号进行分割（最后一个分号是可选的）
                // 规范头中定义的每个对象都会在 try 语句块运行结束之后调用 close() 方法
                Stream<String> in = Files.lines(Paths.get("StreamsAreAutoCloseable.java"));
                PrintWriter outfile = new PrintWriter("Results.txt");
        ) {
            in.skip(5)
                    .limit(1)
                    .map(String::toLowerCase)
                    .forEachOrdered(outfile::println);
        }
    }
}


/**
 * 【揭示细节】
 * 创建自己的 AutoCloseable 类
 *  try-with-resources 定义子句中创建的对象（在括号内）必须实现 java.lang.AutoCloseable 接口，这个接口只有一个方法：close()
 */
class Reporter implements AutoCloseable {
    String name = getClass().getSimpleName();
    Reporter () {
        System.out.println("Creating " + name);
    }
    @Override
    public void close() throws Exception {
        System.out.println("Closing " + name);
    }
}
class First extends Reporter {}
class Second extends Reporter {}
class AutoCloseableDetails {
    public static void main(String[] args) {
        try(
                First f = new First();
                Second s = new Second()
        ) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * 如果不是 AutoCloseable 的对象，就无法通过编译
 */
//class Anything {}
//class TryAnything {
//    public static void main(String[] args) {
//        try(
//                Anything a = new Anything() // 编译错误：Anything无法转换为java.lang.AutoCloseable
//        ) {
//
//        }
//    }
//}

/**
 * 构造函数抛出异常的情况
 */
class CE extends Exception {}
class SecondException extends Reporter {
    SecondException() throws CE {
        super();
        throw new CE();
    }
}
class ConstructorException {
    public static void main(String[] args) {
        try(
                First f = new First();
                SecondException s = new SecondException();
                Second s2 = new Second()
        ) {
            System.out.println("In body");
        } catch (CE e) {
            System.out.println("Caught CE: " + e);
        } catch (Exception e) {
            System.out.println("Caught Exception: " + e);
        }
    }
}