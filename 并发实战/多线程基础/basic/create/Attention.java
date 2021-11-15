package basic.create;

/**
 * 误区一：关于 Thread 的 run()
 */
class Demo1 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " " + this.getName() + " " + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("直接调用线程对象的 run() 方法" +
                "系统会把线程对象当成普通对象，把 run() 方法当成普通方法" +
                "所以下面将一次执行两个 run() 方法");
        new Demo1().run();
        Demo1 t = new Demo1();
        t.run();
        // 在调用完线程对象的 run() 后，调用 start() 尝试启动线程
        t.start(); /* 依旧可以启动这个线程 */
        t.run();
        // 再次启动线程
        //t.start(); /* java.lang.IllegalThreadStateException */
    }
}
/*
Notice：
1. 上述代码中只有一个 main 主线程，两个 run() 将被顺序执行。
    也就是说：如果直接调用线程的 run() 方法，该方法将作为一个普通方法，而不是线程执行体。
2. 手动启动启动 run() 后，依旧可以启动线程对象。但是，不可将同一个线程对象启动多次！
3. 永远不要调用线程对象的 run()。
 */

/**
 * 误区二：妄图将线程“复活”
 */
class Demo2 extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "Hello");
    }

    public static void main(String[] args) throws InterruptedException {
        Demo2 t = new Demo2();
        for (int i = 0; i < 10000; i++) {
            if (i == 20) {
                t.start();
            }

            // 重新唤醒已经死亡的线程
            if (!t.isAlive() && i > 20) { /* 当 i > 20 时，该线程已经被启动过。如果在这里线程死亡，那么，就是死亡状态了 */
                t.start(); /* java.lang.IllegalThreadStateException */
            }
        }
    }
}
/*
Notice：
1. 不要对处于死亡的线程调用 start()，否则引发 IllegalThreadStateException 异常。
 */

/*
小结：
程序只能对新建状态的线程调用 start() 方法。无论是对新建状态调用多次 start()，还是对处于死亡状态的线程调用 start()，
这都引发 IllegalThreadStateException 异常。
 */
