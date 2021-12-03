package basic.synchronize;

/*
线程安全的解决方案（一）：同步代码块
    synchronized (同步监视器) {
        // 同步代码块
    }
含义：线程在执行同步代码块之前，必须先获得对同步监视器的锁定。
 */

/*
案例一：模拟多人对同一账户取钱操作
 */
class DrawThread2 extends Thread {
    // 这里线程安全问题的 账户类
    private basic.synchronize.Account account;
    private Double drawAmount;

    public DrawThread2(String name, basic.synchronize.Account account, Double drawAmount) {
        super(name);
        this.account = account;
        this.drawAmount = drawAmount;
    }

    @Override
    public void run() {
        /*
        为了加深理解，来一个骚操作：
            Object o = new Object();
            synchronized (o) {
        如果是这样的话，这个同步代码块是没用的，因为锁住的是当前对象的 o，而对于有资源竞争的 account 则没有任何锁定
         */
        synchronized (account) {
            // 取款成功
            if (drawAmount <= account.getBalance()) {
                System.out.println(getName() + "取款成功，吐出钞票：" + drawAmount);
                try {
                    sleep(1); /* 强制切换线程 */
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 修改金额
                account.setBalance(account.getBalance() - drawAmount);
                System.out.println("账户余额：" + account.getBalance());
            } else {
                System.out.println(getName() + "取款失败，账户余额不足！");
            }
        }
    }
}

class DrawTest2 {
    public static void main(String[] args) {
        basic.synchronize.Account account = new basic.synchronize.Account(123, 1000.0);
        new DrawThread2("张三", account, 800.0).start();
        new DrawThread2("李四", account, 800.0).start();
    }
}



/*
案例二：两个线程同时对一个int变量进行操作，一个加10000次，一个减10000次
 */
class Counter {
    public static final Object lock = new Object();
    public static int count = 0;
}

class AddThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Counter.lock) { /* 获取锁 */
                Counter.count += 1; /* 一行语句对应了 3 条指令：ILOAD、IADD、ISTORE */
            } /* 释放锁 */
        }
    }
}
class DecThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Counter.lock) {
                Counter.count -= 1;
            }
        }
    }
}

class CounterTest {
    public static void main(String[] args) throws InterruptedException {
        AddThread addThread = new AddThread();
        DecThread decThread = new DecThread();
        addThread.start();
        decThread.start();
        addThread.join();
        decThread.join();
        System.out.println(Counter.count);
    }
}
/*
步骤：
1. 找出修改共享变量的线程代码块；
2. 选择一个共享实例作为锁；
3. 使用 synchronized (lockObject) { ... }。
 */


/*
案例三：对同一个对象使用两个锁进行控制
 */
class Counter2 {
    public static final Object stuLock = new Object();
    public static final Object teacherLock = new Object();
    public static int studentCount = 0;
    public static int teacherCount = 0;
}

class AddStudentThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Counter2.stuLock) {
                Counter2.studentCount += 1;
            }
        }
    }
}
class DecStudentThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Counter2.stuLock) {
                Counter2.studentCount -= 1;
            }
        }
    }
}
class AddTeacherThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Counter2.teacherLock) {
                Counter2.teacherCount += 1;
            }
        }
    }
}
class DecTeacherThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Counter2.teacherLock) {
                Counter2.teacherCount -= 1;
            }
        }
    }
}

class CounterTest2 {
    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[] {
                new AddStudentThread(), new DecStudentThread(),
                new AddTeacherThread(), new DecTeacherThread()
        };
        for (Thread t : ts) {
            t.start();
            t.join();
        }
        System.out.println(Counter2.studentCount + " " + Counter2.teacherCount);
    }
}
/*
如果上述 4 个线程变量分别对 2 个共享变量分别进行读写，使用的是一个锁。
这就造成了原本可以并发执行的 Counter.studentCount += 1 和 Counter.teacherCount += 1，现在无法并发执行了，执行效率大大降低。

在上述示例中，对需要同步的线程分成两组：Teacher 和 Student 组，组之间不存在竞争。因此，应该使用两个不同的锁：teacherLock 和 stuLock。
这样才能最大化地提高执行效率。
 */


/*
案例四：对 Java 的 synchronized 线程锁的可重入性进行测试
 */
class Counter3 {
    private int count = 0;

    public synchronized void add(int n) {
        if (count > 0) {
            dec(-n);
        } else {
            count += n;
        }
    }
    public synchronized void dec(int n) {
        count += n;
    }
}
class CounterTest3 {
    public static void main(String[] args) {
        Counter3 c = new Counter3();
        for (int i = 0; i < 100; i++) {
            c.add(i);
        }
    }
}
/*
上述示例中，如果进入到 dec() 内部，说明了，在获取到 this 锁的情况下再次进行加锁。
 */
