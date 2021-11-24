package basic.communication.practise;

/*
在这组测试中，使用 Lock 对象充当同步监视器，使用 Lock 对象的 wait、notifyAll 操作来控制线程，实现成功（同步监视器可使用任何对象充当）。

但是，不可以将“使用 lock 的方式上锁”、“使用 lock 的 wait、notifyAll 的方式控制线程通信”混用！
因为在 Lock - Condition 的方式中是没有同步监视器一说的。
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
    private Integer id;
    private Double balance;
    /* 是否有存款的标志：true 代表有钱，false 代表没钱 */
    private Boolean flag = false;
    private final Lock lock = new ReentrantLock();

    public Account(Integer id, Double balance) {
        this.id = id;
        this.balance = balance;
    }

    /**
     * 取款操作
     * @param drawAmount 取款金额
     */
    public void draw(double drawAmount) {
        lock.lock();
            try {
                /* 如果账户没钱，让取钱线程等待，进入阻塞状态 */
                if (!flag) {
                    lock.wait();
                } else {
                    // 执行取钱操作
                    System.out.println(Thread.currentThread().getName() + " 取款：" + drawAmount);
                    balance -= drawAmount;
                    System.out.println("账户余额：" + balance);
                    // 更新标志字段，并唤醒其他线程
                    flag = false;
                    lock.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

    }

    /**
     * 存款操作
     * @param depositAmount 存款金额
     */
    public  void deposit(double depositAmount) {
        lock.lock();
            try {
                if (flag) {
                    /* 如果有钱，让存钱线程等待，进入阻塞状态 */
                    lock.wait();
                } else {
                    // 执行存钱操作
                    System.out.println(Thread.currentThread().getName() + " 存款：" + depositAmount);
                    balance += depositAmount;
                    System.out.println("账户余额：" + balance);
                    flag = true;
                    lock.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
    }
}

/**
 * 开启三个线程进行测试：两个存款、一个取款
 */
class DrawTest {
    public static void main(String[] args) {
        Account account = new Account(123, 0.0);
        new DepositThread("存钱甲", account, 800).start();
        new DepositThread("存钱乙", account, 800).start();
        new DrawThread("取钱丙", account, 800).start();
    }
}

class DrawThread extends Thread {
    private Account account;
    private double drawAmount;

    public DrawThread(String name, Account account, double drawAmount) {
        super(name);
        this.account = account;
        this.drawAmount = drawAmount;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " 取钱线程执行，i = " + i);
            account.draw(drawAmount);
        }
    }
}
class DepositThread extends Thread {
    private Account account;
    private double drawAmount;

    public DepositThread(String name, Account account, double drawAmount) {
        super(name);
        this.account = account;
        this.drawAmount = drawAmount;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " 存钱线程执行，i = " + i);
            account.deposit(drawAmount);
        }
    }
}
