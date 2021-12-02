package basic.control;

/**
 * 后台线程（Daemon Thread），又称为 守护线程 或 精灵线程。任务是为其他的线程提供服务。如：JVM 的 GC 线程。
 * 特点：1）前台线程死亡，后台线程自动死亡；2）前台线程创建的子线程默认是前台线程，后台线程创建的子线程默认是后台线程。
 */
public class DaemonThread extends Thread {
    public DaemonThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 1_000; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }

    public static void main(String[] args) {
        DaemonThread daemonThread = new DaemonThread("后台线程");
        daemonThread.setDaemon(true);
        daemonThread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
        /* 通过观察，当 main 线程死亡时，后台线程也随之死亡 */
    }
}
/*
Note:
1. 如果要将某个线程设置为后台线程，必须要在该线程启动之前设置（即，setDaemon 在 start 之前），否则引发 IllegalThreadStateException 异常
2. 前台线程死亡后，JVM 通知后台线程死亡，在这个之间，从接收指令到做出相应需要一定时间
3. 在守护线程中，不能持有任何需要关闭的资源.例如打开文件等，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。
 */
