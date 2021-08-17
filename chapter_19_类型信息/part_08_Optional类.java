import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
【Optional 类】
---背景---
如果你使用内置的 null 来表示没有对象，
每次使用引用的时候就必须测试一下引用是否为 null，这显得有点枯燥，
而且势必会产生相当乏味的代码。
（问题在于 null 没什么自己的行为，只会在你想用它执行任何操作的时候产生 NullPointException）

---作用---
java.util.Optional 为 null 值提供了一个轻量级代理，
可以防止你的代码直接抛出 NullPointException。

实际上，在所有地方都使用 Optional 是没有意义的，
有时候检查一下是不是 null 也挺好的，或者有时我们可以合理地假设不会出现 null，
甚至有时候检查 NullPointException 异常也是可以接受的。
Optional 最有用武之地的是在那些“更接近数据”的地方，在问题空间中代表实体的对象上。
 */


/**
 * 一个例子：
 */
class Person2 {
    public final Optional<String> first;
    public final Optional<String> last;
    public final Optional<String> address;
    public final Boolean empty;
    Person2(String first, String last, String address) {
        this.first = Optional.ofNullable(first);
        this.last = Optional.ofNullable(last);
        this.address = Optional.ofNullable(address);
        /* 都为 null 的时候 empty 为 null */
        empty = !this.first.isPresent() && !this.last.isPresent() && !this.address.isPresent();
    }
    Person2(String first, String last) {
        this(first, last, null);
    }
    Person2(String last) {
        this(null, last, null);
    }
    Person2() {
        this(null, null, null);
    }

    @Override
    public String toString() {
        if (empty) {
            return "<Empty>";
        }
        return (first.orElse("") + "-"
                + last.orElse("") + "-"
                + address.orElse("")).trim();
    }

    public static void main(String[] args) {
        System.out.println(new Person2());
        System.out.println(new Person2("Smith"));
        System.out.println(new Person2("Bob", "Smith"));
        System.out.println(new Person2("Bob", "Smith", "11 Degree Lane, Frostbite Falls, MN"));
    }
}


/**
 * 另一个例子：
 */
class EmptyTitleException extends RuntimeException {}
class Position {
    private String title;
    private Person2 person;

    Position(String jobTitle, Person2 employee) {
        setTitle(jobTitle);
        setPerson(employee);
    }
    Position(String jobTitle) {
        this(jobTitle, null);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String newTitle) {
        title = Optional.ofNullable(newTitle).orElseThrow(EmptyTitleException::new);
    }
    public Person2 getPerson() {
        return person;
    }
    public void setPerson(Person2 newPerson) {
        /* 使用 Optional，如果 newPerson 为 null 的时候，新建类 */
        person = Optional.ofNullable(newPerson).orElse(new Person2());
    }

    @Override
    public String toString() {
        return "Position: " + title +
                ", Employee: " + person;
    }

    public static void main(String[] args) {
        System.out.println(new Position("CEO"));
        System.out.println(new Position("CEO", new Person2("Arthur", "Fonzarelli")));
        try {
            new Position(null);
        } catch (Exception e) {
            System.out.println("caught: " + e);
        }
    }
}
/*
在上述示例中，title 和 person 变量都是普通字段，本身并不受 Optional 的保护。
但是，这些字段本身是 private 的，修改这些字段的唯一途径是
调用 setTitle() 和 setPerson() 方法，这两个都借助 Optional 对字段进行了严格的限制：
1. title 字段
    当 title 字段被设置为 null 的时候，就会得到一个异常
2. person 字段
    当 person 字段被设置为 null 的时候，就会得到一个空的 Person 对象。
 */

/**
 * 【一个特例】
 * 使用 Option 的时候可以免受 NullPointerExceptions 的困扰，
 * 但是 Staff 类却对此毫不知情：
 */
class Staff extends ArrayList<Position> {
    public void add(String title, Person2 person) {
        /* 使用 ArrayList 的 add(E e) */
        add(new Position(title, person));
    }
    public void add(String... titles) {
        for (String title : titles) {
            /* 使用 ArrayList 的 add(E e) */
            add(new Position(title));
        }
    }
    public Staff(String... titles) {
        /* 使用的是 add(String... titles) */
        add(titles);
    }

    /**
     * 根据 title 寻找 Position 对象，且该对象不为空，返回 true
     */
    public Boolean positionAvailable(String title) {
        /* this 代表的是一个 ArrayList<Position> 对象 */
        for (Position position : this) {
            if (position.getTitle().equals(title)
                    && position.getPerson().empty) {
                return true;
            }
        }
        return false;
    }

    /**
     * 填充 title 对应的 Person2 对象，
     * 如果 title 不存在，则抛出异常
     */
    public void fillPosition(String title, Person2 hire) {
        for (Position position : this) {
            if (position.getTitle().equals(title)
                    && position.getPerson().empty) {
                position.setPerson(hire);
                return;
            }
        }
        throw new RuntimeException("Position " + title + " not available");
    }

    public static void main(String[] args) {
        Staff staff = new Staff("President", "CTO",
                "Marketing Manager", "Product Manager",
                "Project Lead", "Software Engineer",
                "Software Engineer", "Software Engineer",
                "Software Engineer", "Test Engineer",
                "Technical Writer");
        staff.fillPosition("President", new Person2("Me", "Last", "address"));
        if (staff.positionAvailable("Software Engineer")) {
            staff.fillPosition("Software Engineer", new Person2("Bob", "Coder", "address"));
        }
        System.out.println(staff);
    }
}


/**
 * 【标记接口】
 * 除了上述的 Option，有时候使用一个标记接口来表示空值会更方便。
 * 标记接口里边什么都没有，你只要把它的名字当做标签来用就可以。
 */
interface Null {}

/**
 * （先定义一个 Operation，在下面的 Robot 会用到）
 * Operation 包含一个描述和一个命令（这用到了命令模式）。
 * 它们被定义成函数式接口的引用，
 * 所以可以把 lambda 表达式或者方法的引用传给 Operation 的构造器：
 */
class Operation {
    public final Supplier<String> description;
    public final Runnable command;
    public Operation(Supplier<String> desc, Runnable cmd) {
        description = desc;
        command = cmd;
    }
}

/*
如果你用接口取代具体类，那么就可以使用 DynamicProxy 来自动地创建 Null 对象。
假设我们有一个 Robot 接口，
它定义了一个名字、一个模型和一个描述 Robot 行为能力的 List<Operation>：
可以通过调用 operations() 来访问 Robot 的服务
 */
interface Robot {
    String name();
    String model();
    List<Operation> operations();

    static void test(Robot r) {
        if (r instanceof Null) {
            System.out.println("这个 Robot 是个 null");
        }
        System.out.println("Robot name: " + r.name());
        System.out.println("Robot model: " + r.model());
        for (Operation operation : r.operations()) {
            System.out.println(operation.description.get());
            operation.command.run();
        }
    }
}

/*
现在创建一个扫雷 Robot：
 */
class SnowRemovalRobot implements Robot {
    private String name;

    public SnowRemovalRobot(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String model() {
        return "扫地机器人的 model";
    }

    private List<Operation> ops = Arrays.asList(
            new Operation(
                    () -> name + " 能够扫雪",
                    () -> System.out.println(name + " 执行扫雪")),
            new Operation(
                    () -> name + " 能够清冰",
                    () -> System.out.println(name + " 执行清冰")));

    @Override
    public List<Operation> operations() {
        return ops;
    }

    public static void main(String[] args) {
        Robot.test(new SnowRemovalRobot("一号"));
    }
}

/*
难度升级：
假设存在许多不同类型的 Robot，
我们想让每种 Robot 都创建一个 Null 对象来执行一些特殊的操作：
（在本例中，即提供 Null 对象所代表 Robot 的确切类型信息）
（这些信息是通过动态代理捕获的）
 */
class NullRobotProxyHandler implements InvocationHandler {

    private String nullName;
    private Robot proxied = new NRobot();

    NullRobotProxyHandler(Class<? extends Robot> type) {
        nullName = type.getSimpleName() + " NullRobot";
    }

    private class NRobot implements Null, Robot {

        @Override
        public String name() {
            return nullName;
        }

        @Override
        public String model() {
            return nullName;
        }

        @Override
        public List<Operation> operations() {
            return Collections.emptyList();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }
}
class NullRobot {
    public static Robot newNullRobot(Class<? extends Robot> type) {
        return (Robot) Proxy.newProxyInstance(
                NullRobot.class.getClassLoader(),
                new Class[] { Null.class, Robot.class },
                new NullRobotProxyHandler(type));
    }

    public static void main(String[] args) {
        Stream.of(
                new SnowRemovalRobot("SnowBee"),
                newNullRobot(SnowRemovalRobot.class)
        ).forEach(Robot::test);
    }
}
/*
上述的示例代码运行死循环了，暂不清楚是因为什么... 示例暂时看不明白
 */
