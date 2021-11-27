package basic.线程池;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个简单使用线程池的例子
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        /* 创建一个具有固定线程数的线程池 */
        ExecutorService pool = Executors.newFixedThreadPool(6);
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + " 的 i = " + i);
            }
        };
        /* 向线程池中提交两个线程 */
        pool.submit(task);
        pool.submit(task);
        /* 关闭线程池 */
        pool.shutdown();
    }
}
