package basic.communication;

/*
传统的通信方法：使用 Object 的 wait、notify、notifyAll，实现账户取钱、存款操作。
要求：取钱、存款交替进行，既不能同时取款两次，也不能同时存款两次。
 */
class Account {
    private Integer id;
    private Double balance;
    /* 是否有存款的标志：true 代表有钱，false 代表没钱 */
    private Boolean flag = false;

    public Account(Integer id, Double balance) {
        this.id = id;
        this.balance = balance;
    }

    /**
     * 取款操作
     * @param drawAmount 取款金额
     */
    public synchronized void draw(double drawAmount) {
        try {
            /* 如果账户没钱，让取钱线程等待，进入阻塞状态 */
            if (!flag) {
                wait();
            } else {
                // 执行取钱操作
                System.out.println(Thread.currentThread().getName() + " 取款：" + drawAmount);
                balance -= drawAmount;
                System.out.println("账户余额：" + balance);
                // 更新标志字段，并唤醒其他线程
                flag = false;
                notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存款操作
     * @param depositAmount 存款金额
     */
    public synchronized void deposit(double depositAmount) {
        try {
            if (flag) {
                /* 如果有钱，让存钱线程等待，进入阻塞状态 */
                wait();
            } else {
                // 执行存钱操作
                System.out.println(Thread.currentThread().getName() + " 存款：" + depositAmount);
                balance += depositAmount;
                System.out.println("账户余额：" + balance);
                flag = true;
                notifyAll();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (getId() != null ? !getId().equals(account.getId()) : account.getId() != null) return false;
        if (getBalance() != null ? !getBalance().equals(account.getBalance()) : account.getBalance() != null)
            return false;
        return flag != null ? flag.equals(account.flag) : account.flag == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getBalance() != null ? getBalance().hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        return result;
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
