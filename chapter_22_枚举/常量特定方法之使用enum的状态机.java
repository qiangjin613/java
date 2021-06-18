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

/**
 * 自动售贷机是一个很好的状态机的例子：
 */
enum Input {
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

    /* 根据 input 获取 分类 */
    public static Category categorize(Input input) {
        return categories.get(input);
    }
}

// 自动售贷机
class VendingMachine {
    private static State state = State.RESTING;
    private static int amount = 0;
    private static Input selection = null;

    enum StateDuration { TRANSIENT }
    enum State {
        /* 休息 */
        RESTING {
            @Override
            void next(Input input) {
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
        /* 加钱 */
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
        if (args.length == 1) {
            gen = new FileInputSupplier(args[0]);
        }
        run(gen);
    }
}

class RandomInputSupplier implements Supplier<Input> {
    @Override
    public Input get() {
        return Input.randomSelection();
    }
}

class FileInputSupplier implements Supplier<Input> {
    private Iterator<String> input;
    FileInputSupplier(String fileName) {
        try {
            input = Files.lines(Paths.get(fileName))
                    .skip(1)
                    .flatMap(s -> Arrays.stream(s.split(";")))
                    .map(String::trim)
                    .collect(Collectors.toList())
                    .iterator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Input get() {
        if (!input.hasNext()) {
            return null;
        }
        return Enum.valueOf(Input.class, input.next().trim());
    }
}
