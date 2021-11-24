package basic.communication;

/*
借助 BlockingQueue 实现线程间的通信
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer extends Thread {
    private BlockingQueue<String> bq;

    public Producer(BlockingQueue<String> bq) {
        this.bq = bq;
    }

    @Override
    public void run() {
        String[] strArr = new String[] {"Java", "Struts", "Spring"};
        for (int i = 0; i < 9999999; i++) {
            System.out.println(getName() + " 生产者准备生产元素 " + i);
            try {
                sleep(200);
                bq.put(strArr[i % 3]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getName() + " 生产完成，bq = " + bq);
        }
    }
}
class Consumer extends Thread {
    private BlockingQueue<String> bq;

    public Consumer(BlockingQueue<String> bq) {
        this.bq = bq;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(getName() + " 消费者准备消费元素 ");
            try {
                sleep(200);
                bq.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(getName() + " 消费完成，bq = " + bq);
        }
    }
}

class Test {
    public static void main(String[] args) {
        BlockingQueue<String> bq = new ArrayBlockingQueue<>(1);
        new Producer(bq).start();
        new Producer(bq).start();
        new Producer(bq).start();
        new Consumer(bq).start();
    }
}
