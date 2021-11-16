package basic.control;

/**
 * 方式二：sleep 操作可以让当前线程暂停一段时间，并进入阻塞状态
 */
public class SleepThread extends Thread{

    public SleepThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public static void main(String[] args) {
        SleepThread sleepThread = new SleepThread("将执行 sleep 操作的线程");
        for (int i = 0; i < 100; i++) {
            if (i == 20) {
                sleepThread.start();
            }
            try {
                Thread.sleep(1000); /* 等价于 sleep()、sleepThread.sleep()，其结果：main 线程被阻塞，但不会影响 sleepThread 的执行 */
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        }
    }
}

class SleepThread2 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000); /* 睡眠 1s */
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
Note:
1. 要注意 sleep 操作阻塞的操作是针对于当前哪个线程而言！
2. sleep 操作为 Thread 的静态方法

Method Detail:
public static native void sleep(long millis) throws InterruptedException
public static void sleep(long millis, int nanos) throws InterruptedException 很少使用
 */
