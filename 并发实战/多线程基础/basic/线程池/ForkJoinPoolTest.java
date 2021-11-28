package basic.线程池;

import java.util.concurrent.*;

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
class SumTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 20;
    private int arr[];
    private int start;
    private int end;

    public SumTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        if (end - start < THRESHOLD) {
            for (int i = start; i < end; i++) {
                sum += arr[i];
                System.out.println(Thread.currentThread().getName() + " 在计算中，sum = " + sum);
            }
            return sum;
        } else {
            int middle = (start + end) / 2;
            SumTask left = new SumTask(arr, start, middle);
            SumTask right = new SumTask(arr, middle, end);
            /* 并行执行两个”小任务“ */
            left.fork();
            right.fork();
            /* 把两个“小任务”累加的结果合并起来 */
            return left.join() + right.join();
        }
    }
}
class Test2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] arr = new int[100];
        for (int i = 0; i < arr.length; i++) {
            arr[i] += i + 1;
        }
        ForkJoinPool pool = new ForkJoinPool();
        Future<Integer> future = pool.submit(new SumTask(arr, 0, arr.length));
        System.out.println(future.get());
        pool.shutdown();
    }
}
