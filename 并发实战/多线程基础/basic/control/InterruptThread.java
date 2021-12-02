package control;

/*
中断线程的方法：1）使用 interrupt()；2）设置标志位。
 */

/**
 * 1）使用 interrupt() 中断线程
 */
class T extends Thread {
    @Override
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");
        }
    }
}
class Test {
    public static void main(String[] args) throws InterruptedException {
        T thread = new T();
        thread.start();
        Thread.sleep(1); /* 让 thread 得到执行 */
        thread.interrupt();
        thread.join(); /* thread 执行完毕后，再执行 main 线程 */
        System.out.println("end");
    }
}
/*
Notice:
interrupt() 操作仅向线程发出了“中断请求”，至于该线程是否能立刻响应，要看具体代码。
 */

/**
 * 让中断线程立即响应的代码
 */
class T2 extends Thread {
    @Override
    public void run() {
        Thread hello = new HelloThread();
        hello.start();
        try {
            hello.join();
        } catch (InterruptedException e) {
            System.out.println( "Interrupted!");
        }
        hello.interrupt(); // 如果去掉这个代码，将会导致 hello 线程仍然会继续运行，且 JVM 不会退出
    }
}
class HelloThread extends Thread {
    @Override
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}


class Test2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new T2();
        t.start();
        Thread.sleep(1000);
        t.interrupt();
        t.join();
        System.out.println("end");
    }
}
/*
main 线程通过调用 t.interrupt() 从而通知 t 线程中断，而此时t线程正位于 hello.join() 的等待中，
此方法会立刻结束等待并抛出 InterruptedException。
由于我们在 t 线程中捕获了 InterruptedException，因此，就可以准备结束该线程。
在 t 线程结束前，对 hello 线程也进行了 interrupt() 调用通知其中断。
如果去掉这一行代码，可以发现 hello 线程仍然会继续运行，且 JVM 不会退出。

Notice：
目标线程检测到 isInterrupted() 为 true 或者捕获了 InterruptedException 都应该立刻结束自身线程。
 */


/**
 * 2）设置标志位中断线程
 */
class T3 extends Thread {
    /* 设置一个线程间共享的变量，使用 volatile 关键字标记，确保每个线程都能读取到更新后的变量值 */
    public volatile boolean running = true;

    @Override
    public void run() {
        int n = 0;
        while (running) {
            n++;
            System.out.println(n + " hello!");
        }
        System.out.println("end");
    }
}
class Test3 {
    public static void main(String[] args) throws InterruptedException {
        T3 t = new T3();
        t.start();
        Thread.sleep(1);
        t.running = false;
    }
}
/*
通过 volatile 关键字解决了共享变量在线程间的可见性问题
 */
