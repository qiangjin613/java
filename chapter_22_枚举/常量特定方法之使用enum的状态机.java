/*
枚举类型非常适合用来创建状态机。
一个状态机可以具有有限个特定的状态，
它通常根据输入，从一个状态转移到下一个状态，
不过也可能存在瞬时状态（transient states），
而一旦任务执行结束，状态机就会立刻离开瞬时状态。

每个状态都具有某些可接受的输入，
不同的输入会使状态机从当前状态转移到不同的新状态。
由于 enum 对其实例有严格限制，非常适合用来表现不同的状态和输入。
一般而言，每个状态都具有一些相关的输出。
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 自动售贷机是一个很好的状态机的例子：
 */
enum Input {
    /* 输入的各种状态 */
    NICKEL(5), DIME(10), QUARTER(25), DOLLAR(100),
    TOOTHPASTE(200), CHIPS(75), SODA(100), SOAP(50),
    ABORT_TRANSACTION {
        @Override
        int amount() {
            throw new RuntimeException("ABORT_TRANSACTION.amount()");
        }
    },
    STOP {
        @Override
        int amount() {
            throw new RuntimeException("STOP.amount()");
        }
    };

    int value;
    Input(int value) {
        this.value = value;
    }
    Input() {}

    int amount() {
        return value;
    }

    static Random rand = new Random(47);
    public static Input randomSelection() {
        return values()[rand.nextInt(values().length - 1)];
    }
}

/*
自动售贷机对输入的第一个反应是将其归类为 Category enum 中的某一个 enum 实例.
下面的例子演示通过 switch 实现，
使代码变得更加清晰且易于管理的：
 */
enum Category {
    /* 钱 */
    MONEY(Input.NICKEL, Input.DIME, Input.QUARTER, Input.DOLLAR),
    /* 货物 */
    ITEM_SELECTION(Input.TOOTHPASTE, Input.CHIPS, Input.SODA, Input.SOAP),
    /* 交易行为 */
    QUIT_TRANSACTION(Input.ABORT_TRANSACTION),
    /* 关机 */
    SHUT_DOWN(Input.STOP);

    private Input[] values;
    Category(Input... type) {
        values = type;
    }
    private static EnumMap<Input, Category> categories = new EnumMap<>(Input.class);

    static {
        for (Category c : Category.class.getEnumConstants()) {
            for (Input type : c.values) {
                categories.put(type, c);
            }
        }
    }

    /* 为 input 获取生成恰当的分类 */
    public static Category categorize(Input input) {
        return categories.get(input);
    }
}

// 自动售贷机
class VendingMachine {
    private static Input selection = null;
    private static int amount = 0;
    private static State state = State.RESTING;

    enum StateDuration { TRANSIENT }
    enum State {
        /* 休息 */
        RESTING {
            @Override
            void next(Input input) {
                /* 休息时：用户塞入钞票、机器停止 */
                switch (Category.categorize(input)) {
                    case MONEY:
                        amount += input.amount();
                        state = ADDING_MONEY;
                        break;
                    case SHUT_DOWN:
                        state = TERMINAL;
                    default:
                }
            }
        },
        /* 用户塞入钞票 */
        ADDING_MONEY {
            @Override
            void next(Input input) {
                switch (Category.categorize(input)) {
                    case MONEY:
                        amount += input.amount();
                        break;
                    case ITEM_SELECTION:
                        selection = input;
                        if (amount < selection.amount()) {
                            System.out.println("Insufficient money for " + selection);
                        } else {
                            state = DISPENSING;
                        }
                        break;
                    case QUIT_TRANSACTION:
                        state = GIVING_CHANGE;
                        break;
                    case SHUT_DOWN:
                        state = TERMINAL;
                    default:
                }
            }
        },
        /* 调剂 */
        DISPENSING(StateDuration.TRANSIENT) {
            @Override
            void next() {
                System.out.println("here is your " + selection);
                amount -= selection.amount();
                state = GIVING_CHANGE;
            }
        },
        /* 改变 */
        GIVING_CHANGE(StateDuration.TRANSIENT) {
            @Override
            void next() {
                if (amount > 0) {
                    System.out.println("Your change: " + amount);
                    amount = 0;
                }
                state = RESTING;
            }
        },
        /* 终端 */
        TERMINAL {
            @Override
            void output() {
                System.out.println("Halted");
            }
        };

        private boolean isTransient = false;
        State() {}
        State(StateDuration trans) {
            isTransient = true;
        }

        void next(Input input) {
            throw new RuntimeException("Only call " +
                    "next(Input input) for non-transient states");
        }
        void next() {
            throw new RuntimeException("Only call next() for " +
                    "StateDuration.TRANSIENT states");
        }
        void output() {
            System.out.println(amount);
        }
    }

    static void run(Supplier<Input> gen) {
        while (state != State.TERMINAL) {
            state.next(gen.get());
            while (state.isTransient) {
                state.next();
            }
            state.output();
        }
    }

    public static void main(String[] args) {
        Supplier<Input> gen = new RandomInputSupplier();
        run(gen);
    }
}


class RandomInputSupplier implements Supplier<Input> {
    @Override
    public Input get() {
        return Input.randomSelection();
    }
}

/*
由于用 switch 语句从 enum 实例中进行选择是最常见的一种方式
（请注意，为了使 enum 在 switch 语句中的使用变得简单，我们是需要付出其他代价的），
所以，我们经常遇到这样的问题：
    将多个 enum 进行分类时，“我们希望在什么 enum 中使用 switch 语句？”
我们通过 VendingMachine 的例子来研究一下这个问题。

对于每一个 State，我们都需要在输入动作的基本分类中进行查找：
用户塞入钞票，选择了某个货物，操作被取消，以及机器停止。
然而，在这些基本分类之下，我们又可以塞人不同类型的钞票，可以选择不同的货物。
Category enum 将不同类型的 Input 进行分组，
因而，可以使用 categorize0 方法为 switch 语句生成恰当的 Cateroy 实例。
并且，该方法使用的 EnumMap 确保了在其中进行查询时的效率与安全。

如果仔细研究 VendingMachine 类，
就会发现每种状态的不同之处，以及对于输入的不同响应，
其中还有两个瞬时状态。
在 run() 方法中，
状态机等待着下一个 Input，并一直在各个状态中移动，直到它不再处于瞬时状态。

通过 Generator 对象，
我们可以使用 Supplier 对象来测试 VendingMachine，
RandomInputSupplier，它会不停地生成除了 SHUT-DOWN 之外的各种输入。
通过长时间地运行 RandomInputSupplier，可以起到健全测试（sanity test）的作用，
能够确保该状态机不会进入一个错误状态。

这种设计有一个缺陷，
它要求 enum State 实例访问的 VendingMachine 属性必须声明为 static，
这意味着，你只能有一个 VendingMachine 实例。
（不过如果我们思考一下实际的（嵌入式 Java）应用，这也许并不是一个大问题，
因为在一台机器上，我们可能只有一个应用程序）
 */
