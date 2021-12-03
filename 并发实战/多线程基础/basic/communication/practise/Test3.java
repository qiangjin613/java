package communication.practise;

import java.util.LinkedList;
import java.util.Queue;

/*
一个简单代码导致 CPU 占用率 100% 的例子
 */

// DIY 阻塞队列
class TestQueue {
    private Queue<String> queue = new LinkedList<>();

    public synchronized void add(String s) {
        queue.add(s);
    }

    /**
     * 在获取元素的时候，先判断队列是否为空，如果为空则一直循环等待，直到队列中有元素进来
     */
    public synchronized String get() {
        while (queue.isEmpty()) {
            /* 什么也不做 */
        }
        return queue.remove();
    }
}

class AddThead extends Thread {
    private TestQueue queue;

    public AddThead(TestQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            queue.add(i + "");
            System.out.println(Thread.currentThread().getName() + " 添加元素：" + i);
        }
    }
}
class GetThread extends Thread {
    private TestQueue queue;

    public GetThread(TestQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 200; i++) {
            System.out.println(Thread.currentThread().getName() + " 获取元素：" + queue.get());
        }
    }
}

public class Test3 {
    public static void main(String[] args) {
        TestQueue queue = new TestQueue();
        new AddThead(queue).start();
        new GetThread(queue).start();
        System.out.println("end");
    }
}
/*
死循环而导致 CPU 利用率 100%。（Notice：具体的CPU利用率与内核数有关，如果是单核电脑，会卡的死死的）

优化：
在 queue 为空时，让该线程进入等待状态；往 queue 添加元素后，唤醒所有线程。
 */
