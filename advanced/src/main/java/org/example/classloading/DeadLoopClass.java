package org.example.classloading;

/**
 * 演示类加载过程导致的线程阻塞
 */
public class DeadLoopClass {

    static {
        if (true) {
            System.out.println(Thread.currentThread() + " 初始化 DeadLoopClass");
            while (true) {}
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable run = () -> {
            System.out.println(Thread.currentThread() + " start");
            DeadLoopClass dlc = new DeadLoopClass();
            System.out.println(Thread.currentThread() + " run over");
        };

        Thread thread = new Thread(run);
        Thread thread1 = new Thread(run);
        thread.start();
        thread1.start();
    }
}
