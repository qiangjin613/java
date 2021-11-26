package basic.线程组;

class T extends Thread {
    public T(String name) {
        super(name);
    }

    public T(ThreadGroup group, String name) {
        super(group, name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println(getName() + " 线程的 i = " + i);
        }
    }
}

class ThreadGroupTest {
    public static void main(String[] args) {
        /* 获取当前线程（main）的线程组，这是线程默认的线程组 */
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
        System.out.println("主线程组的名字：" + mainGroup.getName());
        System.out.println("主线程组的线程最大优先级：" + mainGroup.getMaxPriority());
        System.out.println("主线程组的是否是后台线程组：" + mainGroup.isDaemon());

        T t = new T("线程甲");
        t.start();

        ThreadGroup tg = new ThreadGroup("新线程组");
        tg.setDaemon(true);

        new T(tg, "线程乙").start();
    }
}
