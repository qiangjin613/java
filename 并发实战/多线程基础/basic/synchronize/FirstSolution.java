package basic.synchronize;

/*
线程安全的解决方案（一）：同步代码块
    synchronized (同步监视器) {
        // 同步代码块
    }
含义：线程在执行同步代码块之前，必须先获得对同步监视器的锁定。
 */

class DrawThread2 extends Thread {
    // 这里线程安全问题的 账户类
    private Account account;
    private Double drawAmount;

    public DrawThread2(String name, Account account, Double drawAmount) {
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

/**
 * 模拟多人对同一账户取钱操作
 */
class DrawTest2 {
    public static void main(String[] args) {
        Account account = new Account(123, 1000.0);
        new DrawThread2("张三", account, 800.0).start();
        new DrawThread2("李四", account, 800.0).start();
    }
}

