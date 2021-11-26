package basic.线程组;

class ExHandler1 implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(t.getName() + " 在 ExHandler1 的 uncaughtException 中捕获了线程：" + e);
    }
}

class Demo {
    public static void main(String[] args) {
        /* 设置 main 线程组的未处理异常处理器 */
        Thread.currentThread().setUncaughtExceptionHandler(new ExHandler1());
        int a = 5 / 0;
        System.out.println("main 线程正常结束");
    }
}

class ExHandler2 implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(t.getName() + " 在 ExHandler2 的 uncaughtException 中捕获了线程：" + e);
    }
}

class Demo2 {
    public static void main(String[] args) {
        Thread.currentThread().setUncaughtExceptionHandler(new ExHandler1());
        Thread.setDefaultUncaughtExceptionHandler(new ExHandler2());
        //System.out.println(Thread.currentThread().getUncaughtExceptionHandler());
        int a = 5 / 0;
        System.out.println("main 线程正常结束");
    }
}
/*
结果：
在以上两种处理过程中，发现程序都不会打印最后一句 “main 线程正常结束”。
说明异常处理器与通过 catch 捕获异常对象是不同的：
当使用 catch 捕获异常时，异常不会向上传播给上一级调用者；
但使用异常处理器对异常进行处理后，异常依然会传播给上一级调用者，也就是说会影响当前线程的执行。
 */
