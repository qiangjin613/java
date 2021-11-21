package basic.synchronize;

class A {
    public synchronized void f(B b) {
        System.out.println(Thread.currentThread().getName() + " 进入了 A 对象的 f()");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " 将要调用 B 对象的 last()");
        b.last();
    }

    public synchronized void last() {
        System.out.println(Thread.currentThread().getName() + " 进入了 a 对象的 last()");
    }
}
class B {
    public synchronized void f(A a) {
        System.out.println(Thread.currentThread().getName() + " 进入了 B 对象的 f()");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " 将要调用 A 对象的 last()");
        a.last();
    }

    public synchronized void last() {
        System.out.println(Thread.currentThread().getName() + " 进入了 b 对象的 last()");
    }
}

public class DeadLock extends Thread {
    private A a = new A();
    private B b = new B();

    @Override
    public void run() {
        setName("线程乙");
        a.f(b);
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("线程甲");
        DeadLock deadLock = new DeadLock();
        deadLock.start();
        deadLock.b.f(deadLock.a);
    }
}
/*
Output:（情况之一）
线程甲 进入了 B 对象的 f()
线程乙 进入了 A 对象的 f()
线程甲 将要调用 A 对象的 last()
线程乙 将要调用 B 对象的 last()
（持续等待... 发生死锁）

对上发生死锁现象的解析：
1. 线程甲先执行 B 对象的 f() --> 对 B 对象进行加锁
2. 线程甲执行 sleep 操作 --> 目的是让甲线程进入阻塞状态（好确保乙线程的 A 对象成功加锁）
3. 线程乙执行 A 对象的 f() --> A 对象加锁
4. 线程乙执行 sleep 操作 --> 目的是让乙线程进入阻塞状态（好确保甲线程的 B 对象成功加锁）
（在 sleep 操作结束后，A、B 对象皆已上锁）
5. 线程甲调用 A 对象的 last() --> 要获得 A 对象的锁，但这时 A 对象已经被锁定，要等待 A 对象的 f() 执行完才会释放锁
6. 线程乙调用 B 对象的 last() --> 要获得 B 对象的锁，但这时 B 对象已经被锁定，要等待 B 对象的 f() 执行完才会释放锁
（互相等待对方释放锁，形成“死锁”）
 */
