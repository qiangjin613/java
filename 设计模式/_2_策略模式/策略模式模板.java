package _2_策略模式;

/*
策略模式的一个模板
 */

// 抽象策略类
abstract class Strategy {
    // 算法方法
    public abstract void AlgorithmInterface();
}

// 具体的算法或行为
class ConcreteStrategyA extends Strategy {
    @Override
    public void AlgorithmInterface() {
        /* to do sth. */
    }
}
class ConcreteStrategyB extends Strategy {
    @Override
    public void AlgorithmInterface() {
        /* to do sth. */
    }
}

// 上下文
class Context {
    Strategy strategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public void contextInterface() {
        /* 执行具体的策略方法 */
        strategy.AlgorithmInterface();
    }
}

class Main {
    public static void main(String[] args) {
        /* 实例化不同的策略，在调用策略方法的时候，会有不同的结果 */
        Context context = new Context(new ConcreteStrategyA());
        context.contextInterface();
    }
}
