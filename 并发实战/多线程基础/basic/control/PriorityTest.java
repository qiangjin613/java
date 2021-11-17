package basic.control;

/**
 * 线程优先级：每个线程都有优先级，优先级高的获得的执行机会高。
 * 特点：每个线程默认的优先级与创建它的父线程的优先级相同。默认情况下，main 线程拥有普通优先级。
 */
public class PriorityTest extends Thread {

    public PriorityTest(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + "，Priority = " + getPriority() + ", i = " + i);
        }
    }

    public static void main(String[] args) {
        Thread.currentThread().setPriority(MAX_PRIORITY); /* 设置主线程的优先级 */
        PriorityTest thread1 = new PriorityTest("优先级低的");
        thread1.setPriority(MIN_PRIORITY);
        thread1.start();
        PriorityTest thread2 = new PriorityTest("优先级高的");
        thread2.setPriority(MAX_PRIORITY);
        thread2.start();
        thread1.setPriority(MAX_PRIORITY); /* 修改运行状态的优先级 */
    }
}
/*
Note:
1. 线程优先级需要操作系统的支持，不同操作系统上的优先级并不相同，且不能与 Java 提供的 10 个等级相对应。
    因此尽量避免直接为线程指定优先级，应使用相关静态常量来设置优先级，保证程序具有更好的移植性。
2. 线程优先级可在线程生命周期的各个阶段设置。
 */
