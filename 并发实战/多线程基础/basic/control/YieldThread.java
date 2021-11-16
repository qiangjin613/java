package basic.control;

/**
 * 方式三：yield 操作可以让当前线程暂停，转入就绪状态
 */
public class YieldThread extends Thread {

    public YieldThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            Thread.yield();
        }
    }

    public static void main(String[] args) {
        YieldThread thread1 = new YieldThread("线程1");
        thread1.start();
        YieldThread thread2 = new YieldThread("线程2");
        thread2.start();
        /* 两个线程基本上会交替执行 */
    }
}
/*
Note:
1. yield 操作是让当前线程放弃对 CPU 的持有权，让系统调度器重新调度一次，所以完全会出现的可能是，当某个线程调用了 yield 操作后该线程又被调用执行了

Method Detail:
public static native void yield();
 */
