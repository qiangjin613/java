## 线程池
系统启动一个新线程的成本是比较高的，因为它涉及与操作系统的交互。在这种情况下，使用线程池可以很好地提高性能，尤其是当程序中需要大量创建生存期很短的线程时，更应该考虑使用线程池。

与数据库连接池类似的是，线程池在系统启动时即创建大量空闲的线程，程序将 Runnable 或 Callable 对象传给线程池，线程池就会启动一个空闲的线程来执行他们的 run() 或 call() 方法，当 run() 或 call() 方法执行结束后，该线程不会死亡，而是再次返回线程池中，变为空闲状态，等待下一个 Runnable 或 Callable 对象。

除此之外，使用线程池可以有效地控制系统中并发线程的数量，当系统中包含大量并发线程时，会导致系统性能剧烈下降，甚至导致 JVM 崩溃，而线程池的最大线程参数可以控制系统中并发线程数不会超过此数。

### 使用线程池管理线程
Java 5 以前，开发者必须手动实现自己的线程池；从 Java 5 开始，Java 内建支持线程池。Java 5 新增了一个 Executors 工厂类来产生线程池：
- static ExecutorService newCachedThreadPool()
- static ExecutorService newSingleThreadExecutor()
- static ExecutorService newFixedThreadPool(int nThreads)
- static ScheduledExecutorService newSingleThreadScheduledExecutor()
- static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
- static ExecutorService newWorkStealingPool()
> newWorkStealingPool() 是 Java 8 新增的方法，用于生成 work stealing 池。这个线程池充分利用多 CPU 的并行能力，可以更好发挥底层硬件的性能。除此之外，work stealing 池相当于后台线程池，如果前台线程都死亡了，池中的线程会自动死亡。

ExecutorService 代表尽快执行线程的线程池（只要线程池中有空闲线程就执行线程任务），ScheduledExecutorService 代表可在指定延迟后或周期性地执行线程任务的线程池。

在 ExecutorService 中，用完一个线程池后，应当使用 shutdown() 或 shutdownNow() 来关闭线程池。二者区别如下：
- void shutdown()：启动有序关闭，在此过程中执行先前提交的任务，但不接受新的任务。
- List<Runnable> shutdownNow()：尝试停止所有正在执行的任务，停止对正在等待的任务的处理，并返回正在等待执行的任务的列表。

总结以下使用线程池来执行线程任务的步骤：
1. 调用 Executors 工厂类来产生 ExecutorService 或 ScheduledExecutorService 对象代表一个线程池；
2. 创建 Runnable 或 Callable 实现类的实例，作为线程执行任务；
3. 调用线程池对象的 submit() 或 schedule() 方法来提交线程任务；
4. 当不想提交任何任务时，调用 shutdown() 或 shutdownNow() 方法来关闭线程池（如果是 ExecutorService 的话）。

### 使用 ForkJoinPool 利用多 CPU
为了利用多 CPU、多核 CPU 的性能优势，计算机软件系统应该可以充分“挖掘”每个 CPU 的计算能力，绝不能让某个 CPU 处于“空闲”状态。为了充分利用多 CPU、多核 CPU 的优势，可以考虑把一个任务拆分成多个“小任务”，把多个“小任务”放到多个处理器核心上并行执行，当多个“小任务”执行完成之后，再将这些执行结果合并起来即可。

Java 7 提供了 ForkJoinPool 来支持一个任务拆分成多个“小任务”并行计算。ForkJoinPool 是 ExecutorService 的实现类，因此是一种特殊的线程池。
Java 8 进一步扩展了 ForkJoinPool 的功能，Java 8 为其增加了通用池功能。通用池的运行状态不受 shutdown() 或 shutdownNow() 的影响。但有一种情况，如果程序执行 System.exit(0) 操作来终止虚拟机，通用池以及通用池中正在执行的任务都会被自动终止。
> 使用 ForkJoinPool 的 public static ForkJoinPool commonPool() 方法获得通用池

创建了 ForkJoinPool 实例后，就可以调用 ForkJoinPool 的 `public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task)` 或 `public <T> T invoke(ForkJoinTask<T> task)` 方法来执行指定任务了。其中 ForkJoinTask 代表一个可以并行、合并的任务。
ForkJoinTask 是一个抽象类，它还有两个子抽象类：RecursiveAction 代表没有返回值的任务，RecursiveTask 代表有返回值的任务。