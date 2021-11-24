package basic.communication.practise;

/*
使用传统的线程通信实现交替打印
 */
class Print {
    private final char[] letters = "abcdefghigklmnopqrstuvwxyz".toCharArray();
    private final char[] numbers = "0123456789".toCharArray();
    private int p = 0, q = 0;
    private boolean flag = false; /* false 打印字母，true 打印数字 */

    public synchronized void printLetter() throws InterruptedException {
        if (flag) {
            wait();
        }
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + " " + letters[p]);
            p = (p + 1) % letters.length;
        }
        flag = true;
        notifyAll();
    }

    public synchronized void printNum() throws InterruptedException {
        if (!flag) {
            wait();
        }
        System.out.println(Thread.currentThread().getName() + " " + numbers[q]);
        q = (q + 1) % numbers.length;
        flag = false;
        notifyAll();
    }
}

class PrintLetter extends Thread {
    private final Print print;

    public PrintLetter(String threadName, Print print) {
        super(threadName);
        this.print = print;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                print.printLetter();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
class PrintNum extends Thread {
    private final Print print;

    public PrintNum(String threadName, Print print) {
        super(threadName);
        this.print = print;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                print.printNum();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class PrintTest {
    public static void main(String[] args) {
        Print p = new Print();
        new PrintLetter("打印字母", p).start();
        new PrintNum("打印数字", p).start();
    }
}
