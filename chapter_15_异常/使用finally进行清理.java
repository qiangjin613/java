/**
 * 有一些代码片段，可能希望无论 try 块中的异常是否抛出，它们都能得到执行。
 * 为了达到这个效果，可以在异常处理程序后面加上 finally 子句。
 */
class ThreeException extends Exception {}
class FinallyWorks {
    static int count = 0;

    public static void main(String[] args) {
        while (true) {
            try {
                if (count++ == 0) {
                    throw new ThreeException();
                }
                System.out.println("No exception");
            } catch (ThreeException e) {
                System.out.println("ThreeException");
            } finally {
                System.out.println("In finally clause");
                if (count == 2) {
                    break; // 退出 while 循环
                }
            }

        }
    }
}


/**
 * 【finally 用来做什么？】
 * 对于没有垃圾回收和析构函数自动调用机制的语言来说，finally 非常重要。
 * 能保证：无论 try 块里发生了什么，内存总能得到释放。
 * 当要把除内存之外的资源恢复到它们的初始状态时，就要用到 finally 子句。包括：打开的文件或网络连接等（内存由垃圾回收机制释放）
 */
class Switch {
    private boolean state = false;
    public boolean read() {
        return state;
    }
    public void on() {
        state = true;
        System.out.println(this);
    }
    public void off() {
        state = false;
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "Switch{" +
                "state=" + state +
                '}';
    }
}
class OnOffException1 extends Exception {}
class OnOffException2 extends Exception {}

/**
 * 如果不用 finally，使用完要关闭资源
 */
class OnOffSwitch {
    private static Switch sw = new Switch();
    public static void f() throws OnOffException1, OnOffException2 {}

    public static void main(String[] args) {
        try {
            sw.on();
            f();
            sw.off();
        } catch (OnOffException1 e) {
            System.out.println("OnOffException1");
            sw.off();
        } catch (OnOffException2 e) {
            System.out.println("OnOffException2");
            sw.off();
        }
    }
}

/**
 * 当异常被抛出时，如果没有被处理程序捕获，上述代码中的 sw.off() 就没有调用
 * 现改为使用 finally 处理资源关闭
 */
class WithFinally {
    private static Switch sw = new Switch();

    public static void main(String[] args) {
        try {
            sw.on();
            OnOffSwitch.f();
        } catch (OnOffException1 e) {
            System.out.println("OnOffException1");
        } catch (OnOffException2 e) {
            System.out.println("OnOffException2");
        } finally {
            sw.off();
        }
    }
}

/**
 * 在异常没有被当前的程序捕获的情况下，
 * 异常处理机制也会在跳到更高一层的异常处理程序之前执行 finally 子句
 */
class FourException extends Exception {}
class AlwaysFinally {
    public static void main(String[] args) {
        System.out.println("Entering first try block");
        try {
            System.out.println("Entering second try block");
            try {
                throw new FourException();
            } finally {
                System.out.println("finally in 2nd try block");
            }
        } catch (FourException e) {
            System.out.println("Caught FourException in 1st try block");
        } finally {
            System.out.println("finally in 1st try block");
        }
    }
}
/**
 * finally 每次都会执行，但异常只会被捕获一次
 */
class AlwaysFinally2 {
    public static void main(String[] args) {
        System.out.println("Entering first try block");
        try {
            System.out.println("Entering second try block");
            try {
                throw new FourException();
            } catch (FourException e) {
                System.out.println("Caught FourException in 1st try block");
            }  finally {
                System.out.println("finally in 2nd try block");
            }
        } catch (Exception e) { // 因在第 2 个 try 块中被捕获了，所以在这里将不会被二次捕获！
            System.out.println("Caught FourException in 1st try block");
        } finally {
            System.out.println("finally in 1st try block");
        }
    }
}


/**
 * 【在 return 中使用 finally】
 * 只要 try 块被执行，finally 一定会被执行
 */
class MultipleReturns {
    public static void f(int i) {
        System.out.println("Initialization that requires cleanup");
        try {
            System.out.println("Point 1");
            if (i == 1) return;
            System.out.println("Point 2");
            if (i == 2) return;
            System.out.println("Point 3");
            if (i == 3) return;
            System.out.println("End");
            return;
        } finally {
            System.out.println("Performing cleanup"); // 每次都会执行
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 4; i++) {
            f(i);
        }
    }
}


/**
 * 【缺憾：异常丢失】
 * 遗憾的是，Java 的异常也有瑕疵。
 */
class VeryImportantException extends Exception {
    @Override
    public String toString() {
        return "A very important exception!";
    }
}
class HoHumException extends Exception {
    @Override
    public String toString() {
        return "A trivial exception";
    }
}
class LostMessage {
    void f() throws VeryImportantException {
        throw new VeryImportantException();
    }
    void dispost() throws HoHumException {
        throw new HoHumException();
    }

    public static void main(String[] args) {
        try {
            LostMessage lm = new LostMessage();
            try {
                lm.f();
            } finally {
                lm.dispost();
            }
        } catch (VeryImportantException | HoHumException e) {
            System.out.println(e); // lm.f() 抛出的 VeryImportantException 异常丢失了
            /**
             * 一种简单的方式看代这种丢失：在 lm.f() 抛出(返回)了异常 A，在 finally 中又抛出(返回)了异常 B，这时异常 B 替换了异常 A。
             * 异常处理机制，处理一种异常，也许在以后的版本中会修正这个问题。
             * 对于抛出多个异常（类似于一个异常数组/列表等），可以使用【异常链】来实现
             */
        }
    }
}

/**
 * 另一种更简单的异常丢失：从 finally 中返回
 */
class ExceptionSilencer {
    public static void main(String[] args) {
        try {
            throw new RuntimeException();
        } finally {
            return; // 抛出也是另一种 "return"，这里的返回替换了抛出的异常。即使方法里抛出了异常，它也不会产生任何输出。
        }
    }
}