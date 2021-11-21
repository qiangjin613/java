package basic.synchronize;

/*
线程安全的解决方案（三）：同步锁（Lock）
 */

import java.util.concurrent.locks.ReentrantLock;

/**
 * 通用解决方案
 */
class X {
    // 定义锁对象
    private final ReentrantLock lock = new ReentrantLock();

    public void m() {
        // 加锁
        lock.lock();
        try {
            // do sth.
        }
        // 使用 finally 来保证锁的释放
        finally {
            lock.unlock();
        }
    }
}


/**
 * 对账户类的再次改造
 */
class Account4 {
    // 创造锁对象
    private final ReentrantLock lock = new ReentrantLock();
    private Integer id;
    private Double balance;

    public Account4(Integer id, Double balance) {
        this.id = id;
        this.balance = balance;
    }

    public void draw(double drawAmount) {
        /* 加锁 */
        lock.lock();
        try {
            // 取款成功
            if (drawAmount <= balance) {
                System.out.println(Thread.currentThread().getName() + "取款成功，吐出钞票：" + drawAmount);
                try {
                    Thread.sleep(1); /* 强制切换线程 */
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 修改金额
                balance -= drawAmount;
                System.out.println("账户余额：" + balance);
            } else {
                System.out.println(Thread.currentThread().getName() + "取款失败，账户余额不足！");
            }
        } finally {
            /* finally 中释放锁 */
            lock.unlock();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (getId() != null ? !getId().equals(account.getId()) : account.getId() != null) return false;
        return getBalance() != null ? getBalance().equals(account.getBalance()) : account.getBalance() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getBalance() != null ? getBalance().hashCode() : 0);
        return result;
    }
}

/**
 * 模拟多人对同一账户取钱操作
 */
class DrawTest4 extends Thread {

    private Account4 account;

    public DrawTest4(String name, Account4 account) {
        super(name);
        this.account = account;
    }

    @Override
    public void run() {
        account.draw(800.0);
    }

    public static void main(String[] args) {
        Account4 account = new Account4(123, 1000.0);
        new DrawTest4("甲", account).start();
        new DrawTest4("乙", account).start();
    }
}
