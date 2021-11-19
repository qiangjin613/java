package basic.synchronize;

/*
线程安全的解决方案（二）：同步方法
 */

class Account3 {
    private Integer id;
    private Double balance;

    public Account3(Integer id, Double balance) {
        this.id = id;
        this.balance = balance;
    }

    public synchronized void draw(double drawAmount) {
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
    }

    // 使用同步代码块改造上面的同步方法
    public void draw2(double drawAmount) {
        synchronized (this) {
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
        }
    }

    /*
    下面这个取钱线程也是安全的
     */
    Object o = new Object();
    public void draw3(double drawAmount) {
        synchronized (o) {
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
class DrawTest3 extends Thread {

    private Account3 account;

    public DrawTest3(String name, Account3 account) {
        super(name);
        this.account = account;
    }

    @Override
    public void run() {
        account.draw2(800.0);
    }

    public static void main(String[] args) {
        Account3 account = new Account3(123, 1000.0);
        new DrawTest3("甲", account).start();
        new DrawTest3("乙", account).start();
    }
}
