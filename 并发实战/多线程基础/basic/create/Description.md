#### 线程的 3 种创建方式
- 继承 Thread 类
- 实现 Runnable 接口
- 使用 Callable 和 Future
>Java 使用 Thread 类代表线程，所有的线程对象都必须是 Thread 类或其字类的实例。（对于以上所表述的创建方式，最终都回归到了 Thread）

#### 三种实现方式的对比
| 继承 Thread 类 | 实现 Runnable 接口 | 使用 Callable 和 Future |
| :----: | :----: | :----: |
| 继承父类 | 实现接口 | 实现接口 |
|  run() | run() | call() |
| 多个线程间无法共享线程类的实例变量 | 多个线程间可以共享线程类的实例变量 | 多个线程间可以共享线程类的实例变量 |
| 没有返回值 | 没有用返回值 | 有返回值 |
| 不可以抛出异常 | 不可以抛出异常 | 可以抛出异常 |

总结：
- 使用继承 Thread 类的方式：不能继承其他父类、无法共享线程类的实例变量。但是编写简单，无须使用 Thread.currentThread() 获取当前线程；
- 使用实现 Runnable、Callable 接口的方式：可以继承其他父类、可以实现其他接口、可以共享线程类的实例，但是编写要稍微复杂一些。对于 Callable 使用了 FutureTask 的情况，还可能发生阻塞线程。

基于以上分析，推荐使用实现接口的方式创建线程。

#### 注意
程序只能对新建状态的线程调用 start() 方法。无论是对新建状态调用多次 start()，还是对处于死亡状态的线程调用 start()，
这都引发 IllegalThreadStateException 异常。


## 相关类
### Thread
创建一个新线程非常容易，只需要实例化一个 Thread 实例，然后调用它的 start() 方法。

Thread 提供了以下构造器：
1. 通过继承 Thread 创建一个线程，可以指定该线程的名字，默认名称为 "Thread-" + nextThreadNum()
    - public Thread()
    - public Thread(String name)
2. 通过实现 Runnable 接口创建线程
    - public Thread(Runnable target)
    - public Thread(Runnable target, String name)
3. 创建线程并显式指定线程组
    - public Thread(ThreadGroup group, String name)
    - public Thread(ThreadGroup group, Runnable target)
    - public Thread(ThreadGroup group, Runnable target, String name)
    - public Thread(ThreadGroup group, Runnable target, String name, long stackSize) [^1]

相关属性：
1. 线程名称 name
    - public final String getName()
    - public final synchronized void setName(String name)
2. 线程优先级 priority
    - public final int getPriority()
    - public final void setPriority(int newPriority)
3. 线程组 ThreadGroup
    - public final ThreadGroup getThreadGroup()
4. 后台线程 Daemon
    - public final boolean isDaemon()
    - public final void setDaemon(boolean on)
5. 线程的异常处理器 
    - public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler eh)
    - public static UncaughtExceptionHandler getDefaultUncaughtExceptionHandler()
    - public UncaughtExceptionHandler getUncaughtExceptionHandler()
    - public void setUncaughtExceptionHandler(UncaughtExceptionHandler eh)
6. 线程的上下文类加载器
    - public void setContextClassLoader(ClassLoader cl)
    - public ClassLoader getContextClassLoader()

相关方法：
1. 线程控制
    - public synchronized void start()
    - ~~public void run()~~
    - public final synchronized void join(long millis) throws InterruptedException [^2]
    - public static native void sleep(long millis) throws InterruptedException
    - public static native void yield()
2. 获取线程相关信息
    - public static native Thread currentThread()
    - public final native boolean isAlive()
    - public static int activeCount()
    - public State getState()

内部类：
1. 代表了线程状态的 Thread.State
    ```java
    public enum State {
        NEW, /* 尚未启动的线程处于这种状态 */
        RUNNABLE, /* 在 Java 虚拟机中执行的线程处于这种状态 */
        BLOCKED, /* 正在阻塞等待监视器锁的线程处于这种状态 */
        WAITING, /* 一个线程正在无限期地等待另一个线程执行某个特定操作，该线程处于这种状态 */
        TIMED_WAITING, /* 在指定的等待时间内等待另一个线程执行某个操作的线程处于这种状态 */
        TERMINATED; /* 已经退出（终止）的线程处于这种状态 */
    }
    ```
    一个线程在给定的时间点只能处于一种状态。这些状态是虚拟机状态，不反映任何操作系统线程状态。
2. 用于处理线程中未捕获异常的 Thread.UncaughtExceptionHandler
    ```java
    @FunctionalInterface
    public interface UncaughtExceptionHandler {
        void uncaughtException(Thread t, Throwable e);
    }
    ```


---
[1]: 以上的 7 个构造器，最终都指向了 `private void init(ThreadGroup g, Runnable target, String name, long stackSize)` 方法，如果 stackSize（堆栈大小）不指定，默认为 0。
[2]: 对于 join() 的其他两个重载方法，皆被指向到这里。