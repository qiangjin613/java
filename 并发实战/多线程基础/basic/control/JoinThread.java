package basic.control;

/**
 * 方式一：join 线程可让调用线程阻塞，直到使用了 join() 方法的线程执行完毕后，调用线程才可以继续向下执行。
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
        for (int i = 0; i < 100; i++) {
            if (i == 20) {
                JoinThread joinThread = new JoinThread("将会使用 join() 的线程");
                joinThread.start();
                joinThread.join(); /* 该操作使得主线程 main 被阻塞，等到 joinThread 执行结束后，main 才能被执行 */
            }
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }
}
