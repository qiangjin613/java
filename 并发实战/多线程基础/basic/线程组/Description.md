## 线程组
Java 使用 ThreadGroup 来表示线程组，可以使用线程组对一批线程进行分类管理，Java 中对线程组的控制相当于同时控制这批线程。在未指定线程组的情况下（默认情况下），子线程和创建它的父线程属于同一个线程组。

每一个线程组都会有一个名字，且这个名字不允许改变。

*一旦某个线程加入了指定线程组后，此线程将一直属于该线程组，直到该线程死亡。* 同时，因为中途不可改变线程所属的线程组，所以 Thread 类没有提供 setThreadGroup() 方法来改变线程的所属组。

### 线程组内线程的未处理异常
Java 5 开始，加强了线程的异常处理，如果线程执行过程中抛出了一个未处理异常，JVM 在结束该线程之前会自动查找是否有对应的 Thread.UncaughtExceptionHandler 对象，如果找到该处理器对象，则会调用该对象的 uncaughtException(Thread t, Throwable e) 方法来处理该异常。
> Thread.UncaughtExceptionHandler 是 Thread 类的内部 public 接口，该接口内只有一个方法：void uncaughtException(Thread t, Throwable e);

ThreadGroup 类实现了 Thread.UncaughtExceptionHandler 接口，所以每个线程所属的线程组将会作为默认的异常处理器。

线程组处理**未捕获异常**的默认流程如下：（先判断 1，再判断 2，最后判断 3。如果找到一个处理方案，则不继续向下进行）
1. 如果该线程有线程组，则调用线程组的 uncaughtException() 来处理该异常；
2. 如果该线程实例所属的线程类有默认的异常处理器，那么就调用该异常处理器来处理该异常；
3. 如果该异常对象是 ThreadDeath 对象，则不做任何处理；否则，将异常跟踪栈的信息打印到 System.err 错误输出流，并结束该线程。

### 异常处理器处理异常与 catch 捕获异常的不同
当使用 catch 捕获异常时，异常不会向上传播给上一级调用者；
但使用异常处理器对异常进行处理后，异常依然会传播给上一级调用者，也就是说会影响当前线程的执行。