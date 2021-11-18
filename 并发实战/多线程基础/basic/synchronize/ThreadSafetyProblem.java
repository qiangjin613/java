package basic.synchronize;

/*
关于线程安全问题，举一个经典的 银行取钱 问题。
其流程简化为：1）输入取款金额；2）判断账户余额是否大于取款金额；3）输出结果（取款成功 or 取款失败）
 */

/**
 * 账户类
 */
class Account {
    private Integer id;
    private Double balance;

    public Account(Integer id, Double balance) {
        this.id = id;
        this.balance = balance;
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
 * 取钱操作
 */
class DrawThread extends Thread {
    /* 账户和取款金额 */
    private Account account;
    private Double drawAmount;

    public DrawThread(String name, Account account, Double drawAmount) {
        super(name);
        this.account = account;
        this.drawAmount = drawAmount;
    }

    @Override
    public void run() {
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

/**
 * 模拟多人对同一账户取钱操作
 */
class DrawTest {
    public static void main(String[] args) {
        Account account = new Account(123, 1000.0);
        new DrawThread("张三", account, 800.0).start();
        new DrawThread("李四", account, 800.0).start();
    }
}
/*
Output：（一种情况）
李四取款成功，吐出钞票：800.0
张三取款成功，吐出钞票：800.0
账户余额：200.0
账户余额：200.0

Question：
账户金额为 1000，但两个人成功取款后，账户金额还剩 200，违背了数据一致性原则。

Note：
虽然上述情况是使用 sleep 操作来人为地强制切换线程，但难免在自然情况下发生类似的问题。
（在 10000000 次操作只要有一次出现了错误，那就是编程错误引起的）
 */
