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
            try { }
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