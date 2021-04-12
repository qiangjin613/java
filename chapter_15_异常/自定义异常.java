import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * 【自定义异常】
 * 当抛出异常后，有几件事会随之发生:
 *      1. 使用 new 在堆上创建异常对象
 *      2. 异常处理机制接管程序
 *      3. 寻找相关的异常处理程序继续执行程序
 *      4.
 *          1）如果异常向上抛出，出现异常的方法就被终止了，抛出异常的剩余代码不会执行
 *          2）如果异常被处理，则剩余代码继续执行
 */
class SimpleException extends Exception {}
class SimpleExceptionDemo {
    public void f() throws SimpleException {
        System.out.println("Throw SimpleException from f()");
        throw new SimpleException();
    }

    public void f2() throws SimpleException {
        f();
        System.out.println("f2() - 抛出异常的剩余代码");  // f() 过程中抛出异常时，这句将不会执行
    }

    public static void main(String[] args) {
        SimpleExceptionDemo me = new SimpleExceptionDemo();
        try {
            me.f2();
        } catch (SimpleException e) {
            System.out.println("Caught it!");
        }
        System.out.println("main - 抛出异常的剩余代码"); // 即使 me.f2() 抛出了异常，这里也会输出
    }
}

/**
 * 创建一个接收字符串参数的异常类，并且输出信息到标准错误流
 */
class CustomException extends Exception {
    CustomException() {}
    CustomException(String msg) { super(msg);}
}
class FullConstructors {
    public static void f() throws CustomException {
        System.out.println("Throwing CustomException from f()");
        throw new CustomException();
    }
    public static void g() throws CustomException {
        System.out.println("Throwing CustomException from g()");
        throw new CustomException("Originated in g()");
    }

    public static void main(String[] args) {
        try {
            f();
        } catch (CustomException e) {
            e.printStackTrace(System.out); // 信息输出到控制台
            e.printStackTrace(); // 信息输出到标准错误流
        }

        try {
            g();
        } catch (CustomException e) {
            e.printStackTrace(System.out);
        }
    }
}


/**
 * 【异常与记录日志】
 * 记录日志使用 java.util.logging
 * 异常中记录日志的两个位置：
 *      1) 在定义异常类中记录日志
 *      2) 在捕获异常的时候记录日志（推荐！）
 * 但更常见的情形是我们需要捕获和记录其他人编写的异常（这个里面可能没有记录日志）
 */

/**
 * 在异常类中记录日志，好处是不需要客户端程序员的干预就可自动运行
 */
class LoggingException extends Exception {
    // 创建了一个 String 参数相关联的 Logger 对象（通常与错误相关的包名和类名）,这个 Logger 对象会将其输出发送到 System.err
    private static Logger logger = Logger.getLogger("LoggingException");
    LoggingException() {
        StringWriter trace = new StringWriter();
        printStackTrace(new PrintWriter(trace));
        logger.severe(trace.toString()); // (1)在异常类中记录日志
    }
}
class LoggingExceptions {
    public static void main(String[] args) {
        try {
            throw new LoggingException();
        } catch (LoggingException e) {
            System.out.println("Caught " + e);
        }
    }
}

/**
 * 在捕获异常的时候记录日志
 */
class LoggingExceptions2 {
    private static Logger logger = Logger.getLogger("LoggingExceptions2");
    static void logException(Exception e) {
        StringWriter trace = new StringWriter();
        e.printStackTrace(new PrintWriter(trace));
        logger.severe(trace.toString()); // (2)在捕获异常的时候记录日志
    }

    public static void main(String[] args) {
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            logException(e);
        }
    }
}

//===============================================================================
/**
 * 更进一步自定义异常，加入额外的构造器和成员
 */
class MyException2 extends Exception {
    private int x;
    MyException2() {}
    MyException2(String msg) { super(msg); }
    MyException2(String msg, int x) { super(msg); this.x = x; }

    public int val() { return x; }

    @Override
    public String getMessage() {
        // 对于异常类来说，getMessage() 等同于 toString()，该方法获取到的数据即为构造参数中 super(msg) 中传入的 msg
        return "Detail Message: " + x + " " + super.getMessage();
    }
}

class ExtraFeatures {
    public static void f() throws MyException2 {
        System.out.println("Throwing MyException2 from f()");
        throw new MyException2();
    }
    public static void g() throws MyException2 {
        System.out.println("Throwing MyException2 from g()");
        throw new MyException2("Originated in g()");
    }
    public static void h() throws MyException2 {
        System.out.println("Throwing MyException2 from h()");
        throw new MyException2("Originated in h()", 47);
    }

    /**
     * 这里'调试一步一输出'和'运行'的输出结果有细微不同
     */
    public static void main(String[] args) {
        try {
            f();
        } catch (MyException2 e) {
            e.printStackTrace(); // 默认打印到错误流
        }

        try {
            g();
        } catch (MyException2 e) {
            e.printStackTrace(System.out); // 打印到标准输出流
        }

        try {
            h();
        } catch (MyException2 e) {
            e.printStackTrace(System.out);
            System.out.println("e.val() = " + e.val());
        }
    }
}