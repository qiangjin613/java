package basic.create;

/**
 * 方式 1：继承 Thread
 */
public class FirstThread extends Thread {
    private int i;

    @Override
    public void run() {
        for (; i < 100; i++) {
            System.out.println(getName() + " " + i); /* 等价于 this.getName() */
            /* 运行过程中修改线程名 */
            if (i == 30) {
                setName("小兵");
            }
        }
    }
}

class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i); /* 使用 Thread.currentThread() 调用当前线程 */

            if (i == 20) {
                new FirstThread().start();
                new FirstThread().start();
            }
        }
    }
}
