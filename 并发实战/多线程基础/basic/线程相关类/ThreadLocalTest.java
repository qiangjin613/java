package basic.线程相关类;

class Account {
    private ThreadLocal<String> name = new ThreadLocal<>();

    public Account(String name) {
        this.name.set(name);
        System.out.println("线程[" + Thread.currentThread().getName() + "]的 name = " + this.name.get());
    }
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
}

public class ThreadLocalTest extends Thread {
    private Account account;

    public ThreadLocalTest(Account account, String threadName) {
        super(threadName);
        this.account = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            if (i == 6) {
                account.setName(getName());
            }
            System.out.println(account.getName() + " 账户 i 的值：" + i);
        }
    }

    public static void main(String[] args) {
        Account account = new Account("初始名");

        new ThreadLocalTest(account, "甲").start();
        new ThreadLocalTest(account, "乙").start();
        for (int i = 0; i < 10; i++) {
            if (i == 6) {
                account.setName(Thread.currentThread().getName());
            }
            System.out.println(account.getName() + " 账户 i 的值：" + i);
        }
    }
}
