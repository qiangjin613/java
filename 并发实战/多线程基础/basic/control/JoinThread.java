package basic.control;

/**
 * 方式一：join 操作可让调用线程阻塞，直到使用了 join() 方法的线程执行完毕后，调用线程才可以继续向下执行。
 */
public class JoinThread extends Thread {

    public JoinThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new JoinThread("新线程").start();
        for (int i = 0; i < 1000; i++) {
            if (i == 20) {
                JoinThread joinThread = new JoinThread("将会使用 join() 的线程");

                joinThread.join(); /* 无效操作 */
                joinThread.start();
                joinThread.join(); /* 该操作使得主线程 main 被阻塞，等到 joinThread 执行结束后，main 才能被执行 */
                joinThread.join(); /* 无效操作 */
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }
}
/*
Note:
1. join() 操作会阻塞调用线程，但不会影响其他线程的并发操作（如上例中的 "新线程"）
2. 如果在 start() 之前调用 join() 方法或多次调用 join()，join() 操作将会失效

Use:
join 操作通常是在将大问题划分为若干小问题，每个小问题分配一个线程。当所有的小问题都结局后，再调用主线程来进一步操作（类似于 Map -> Reduce 操作）

Method Detail:
public final synchronized void join(long millis) throws InterruptedException
public final synchronized void join(long millis, int nanos) throws InterruptedException 很少使用
public final void join() throws InterruptedException
 */
