import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 【特例：RuntimeException】
 * 代码中只有 RuntimeException（及其子类）类型的异常可以被忽略，因为编译器强制要求处理所有受检查类型的异常
 */
class NeverCaught {
    static void f() {
        throw new RuntimeException("From f()");
    }
    static void g() {
        f();
    }

    public static void main(String[] args) {
        g();
    }
}

/**
 * 【被忽略的 finally】
 */
class IgnoredFinally  {
    public static void f() throws NullPointerException, ArrayIndexOutOfBoundsException {
        if (1 == 2) {
            try {

            }
            // try 块没有执行，所以 finally 也就没有执行
            finally {
                System.out.println("11111111111111111");
            }
        }
    }

    public static void main(String[] args) {
        f();
    }
}

/**
 * 【异常匹配】
 * “最近”匹配、匹配派生类
 */
class Annoyance extends Exception {}
class Sneeze extends Annoyance {}
class Human {
    public static void main(String[] args) {
        // [1] 最近匹配
        try {
            throw new Sneeze();
        } catch (Sneeze s) {
            System.out.println("Caught Sneeze");
        } catch (Annoyance a) {
            System.out.println("Caught Annoyance");
        }

        // [2] 匹配派生类
        try {
            throw new Sneeze();
        } catch (Annoyance a) {
            System.out.println("Caught Annoyance");
        }

        // [3] 被“屏蔽”的派生类，会产生编译错误
//        try {
//            throw new Sneeze();
//        } catch (Annoyance a) {
//            System.out.println("Caught Annoyance");
//        } catch (Sneeze s) {
//            System.out.println("Caught Sneeze");
//        }
    }
}

/**
 * 【把异常传递给控制台】
 */
class MainException {
    public static void main(String[] args) throws IOException {
        throw new IOException();
    }
}

/**
 * 【把“被检查的异常”转换为“不检查的异常”】
 */
class WrapCheckedException {
    void throwRuntimeException(int type) {
        try {
            switch (type) {
                case 0: throw new FileNotFoundException();
                case 1: throw new IOException();
                case 2: throw new RuntimeException("Where am I?");
                default: return;
            }
        } catch (IOException | RuntimeException e) {
            // 转换为“不检查的异常”，同时使用异常链保存异常信息
            throw new RuntimeException(e);
        }
    }
}
class SomeOtherException extends Exception {}
class TurnOffChecking {
    public static void main(String[] args) {
        WrapCheckedException wce = new WrapCheckedException();
        wce.throwRuntimeException(3);
        for (int i = 0; i < 4; i++) {
            try {
                if (i < 3) {
                    wce.throwRuntimeException(i);
                } else {
                    throw new SomeOtherException();
                }
            } catch (SomeOtherException e) {
                System.out.println("SomeOtherException: " + e);
            } catch (RuntimeException re) {
                // 把原先的那个异常给提取出来，然后就可以用它们自己的 catch 子句进行处理
                try {
                    throw re.getCause();
                } catch (FileNotFoundException e) {
                    System.out.println("FileNotFoundException: " + e);
                } catch (IOException e) {
                    System.out.println("IOException: " + e);
                } catch (Throwable e) {
                    System.out.println("Throwable: " + e);
                }
            }
        }
    }
}