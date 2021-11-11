package basic.create;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 方式 3：使用 Callable 和 Future
 */
public class ThirdThread implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int i = 0;
        for (; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
        return i;
    }
}

class Test3 {
    public static void main(String[] args) {
        /* 创建线程 */
        ThirdThread tt = new ThirdThread();
        FutureTask<Integer> ft = new FutureTask<>(tt);
        // ft.get(); /* 该操作将导致主线程阻塞。在当前流程中，导致一直等待... */
        new Thread(ft).start();

        try {
            /* 通过 FutureTask 对象获取该线程返回值。如果没有返回值将导致逐渐成被阻塞，直到 call() 方法结束并返回为止 */
            System.out.println(ft.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
