/*
Q：如果我们希望方法在调用时行为不同，该怎么做呢？
A：通常，传递给方法的数据不同，结果不同。
    只要能将（行为）代码传递给方法，我们就可以控制它的行为。
 */

/**
 * 用传统形式、Lambda 表达式和 Java 8 的方法引用分别演示：
 */
interface Strategy {
    String approach(String msg);
}
class Soft implements Strategy {
    @Override
    public String approach(String msg) {
        return msg.toLowerCase() + "?";
    }
}
class Unrelated {
    static String twice(String msg) {
        return msg + " " + msg;
    }
}
class Strategize {
    Strategy strategy;
    String msg;
    Strategize(String msg) {
        /* Soft 作为默认策略 */
        strategy = new Soft();
        this.msg = msg;
    }

    void communicate() {
        System.out.println(strategy.approach(msg));
    }

    void changeStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public static void main(String[] args) {
        Strategy[] strategies = {
                // [1] 匿名内部类
                new Strategy() {
                    @Override
                    public String approach(String msg) {
                        return msg.toUpperCase() + "！";
                    }
                },
                // [2] Lambda
                msg -> msg.substring(0, 5),
                // [3] 方法绑定
                Unrelated::twice
        };
        Strategize s = new Strategize("Hello there");
        s.communicate();
        for (Strategy strategy : strategies) {
            s.changeStrategy(strategy);
            s.communicate();
        }
    }
}

/*
相比于传统方法，Lambda 和方法引用显得灵活优雅。
 */
