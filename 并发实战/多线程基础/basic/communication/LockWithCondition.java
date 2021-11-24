package basic.communication;

/*
Java 1.5 新添的通信方法：使用 Condition 控制线程通信
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account2 {
    // 显式定义 Lock 对象和对应的 Condition 对象
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private int id;
    private double balance;
    private boolean flag = false;

    public Account2(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    /**
     * 取钱方法
     * @param drawAmount 取款金额
     */
    public void draw(double drawAmount) {
        lock.lock();
        try {
            if (!flag) {
                condition.await();
            } else {
                System.out.println(Thread.currentThread().getName() + " 取钱：" + drawAmount);
                balance -= drawAmount;
                System.out.println("账户余额：" + balance);
                flag = false;
                condition.signalAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 存钱方法
     * @param depositAmount 存款金额
     */
    public void deposit(double depositAmount) {
        lock.lock();
        try {
            if (flag) {
                condition.await();
            } else {
                System.out.println(Thread.currentThread().getName() + " 存钱：" + depositAmount);
                balance += depositAmount;
                System.out.println("账户余额：" + balance);
                flag = true;
                condition.signalAll();
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
class DrawTest2 {
    public static void main(String[] args) {
        Account2 account = new Account2(123, 0.0);
        new DepositThread2("存钱甲", account, 800).start();
        new DepositThread2("存钱乙", account, 800).start();
        new DrawThread2("取钱丙", account, 800).start();
    }
}

class DrawThread2 extends Thread {
    private Account2 account;
    private double drawAmount;

    public DrawThread2(String name, Account2 account, double drawAmount) {
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
class DepositThread2 extends Thread {
    private Account2 account;
    private double drawAmount;

    public DepositThread2(String name, Account2 account, double drawAmount) {
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
