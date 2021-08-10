/**
 * 下面的例子对局部内部类与匿名内部类的创建进行了比较：
 */
interface Counter {
    int next();
}
class LocalInnerClass {
    private int count = 0;

    /* final String 就相当于传递了一个新的 name 过去 */
    Counter getCounter(final String name) {
        /* 局部内部类 */
        class LocalCounter implements Counter {
            LocalCounter() {
                System.out.println("局部内部类 LocalCounter() 初始化");
            }
            @Override
            public int next() {
                System.out.println(name);
                return count++;
            }
        }
        return new LocalCounter();
    }

    Counter getCounter2(final String name) {
        return new Counter() {
            {
                System.out.println("局部匿名内部类 LocalCounter() 初始化");
            }
            @Override
            public int next() {
                System.out.println(name);
                return count++;
            }
        };
    }

    public static void main(String[] args) {
        LocalInnerClass lic = new LocalInnerClass();
        String name = "Local inner";
        Counter c1 = lic.getCounter(name);
        for (int i = 0; i < 5; i++) {
            System.out.println(c1.next());
        }

        System.out.println("---------");

        name = "Anonymous inner";
        Counter c2 = lic.getCounter2(name);
        for (int i = 0; i < 5; i++) {
            System.out.println(c2.next());
        }
    }
}

/*
在上述例子中，分别使用了局部类和匿名内部类实现一个功能，
他们具有相同的行为和能力。

Q：局部内部类的名字在方法外是不可见的，
    那为什么我们仍然使用局部内部类而不是匿名内部类呢？
A：唯一的理由是，
    我们需要一个已命名的构造器，或者需要重载构造器，
    而匿名内部类只能使用实例初始化。（匿名内部类中不可以有构造器的，因为他连名字都没有...）

还有一个原因，就是使用局部内部类的时候，可以有多个该内部类的对象。
 */
