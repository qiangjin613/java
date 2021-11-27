package basic.线程池;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/*
实例一：使用 ForkJoinPool 执行没有返回值的任务，“大任务”拆分成多个“小任务“。
 */
class PrintTask extends RecursiveAction {
    private static final int THRESHOLD = 50;
    private int start;
    private int end;

    public PrintTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        /* 当 end 与 start 之间的差小于 THRESHOLD 时，打印 [start, end) */
        if (end - start < THRESHOLD) {
            for (int i = start; i < end; i++) {
                System.out.println(Thread.currentThread().getName() + " 的 i = " + i);
            }
        } else {
            /* 否则，将”大任务“拆解为”小任务“ */
            int middle = (start + end) / 2;
            PrintTask left = new PrintTask(start, middle);
            PrintTask right = new PrintTask(middle, end);
            /* 并行执行两个”小任务“ */
            left.fork();
            right.fork();
        }
    }
}
class Test1 {
    public static void main(String[] args) throws InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(new PrintTask(0, 500));
        pool.awaitTermination(2, TimeUnit.SECONDS);
        pool.shutdown();
    }
}





/*
实例二：使用 ForkJoinPool 执行有返回值的求和任务。
 */