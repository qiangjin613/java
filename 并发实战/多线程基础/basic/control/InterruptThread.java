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
