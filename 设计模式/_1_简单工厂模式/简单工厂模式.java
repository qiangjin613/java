package _1_简单工厂模式;

/*
在“业务的封装”中，使用封装将业务进行封装，达到了易维护、易扩展的目标。
接下来，使用 继承 这一特性，将业务进一步的细分，实现了各子业务之间相互独立，实现 易扩展 这一特性。
 */

/**
 * 抽象父类
 */
abstract class AbstractOperation {
    private double numA;
    private double numB;

    public double getNumA() {
        return numA;
    }
    public void setNumA(double numA) {
        this.numA = numA;
    }
    public double getNumB() {
        return numB;
    }
    public void setNumB(double numB) {
        this.numB = numB;
    }

    public abstract double GetResult();
}

/**
 * 加减乘除操作类
 */
class OperationAdd extends AbstractOperation {
    @Override
    public double GetResult() {
        return getNumA() + getNumB();
    }
}
class OperationSub extends AbstractOperation {
    @Override
    public double GetResult() {
        return getNumA() - getNumB();
    }
}
class OperationMul extends AbstractOperation {
    @Override
    public double GetResult() {
        return getNumA() * getNumB();
    }
}
class OperationDiv extends AbstractOperation {
    @Override
    public double GetResult() {
        if (getNumB() == 0) {
            throw new RuntimeException("除数不能为 0！");
        }
        return getNumA() / getNumB();
    }
}
/*
使用面向对象的 继承 特性将各个子业务之间进行隔离。
 */

/**
 * 使用 “简单工厂模式” 解决实例化对象问题。
 * Notice：
 * 1) 工厂，即为，用一个单独的类来进行创造实例化的任务
 * 2) 使用到了 面向对象的 多态
 */
class OperationFactory {
    public static AbstractOperation createOperate(String opt) {
        AbstractOperation oper = null;
        switch (opt) {
            case "+": oper = new OperationAdd(); break;
            case "-": oper = new OperationSub(); break;
            case "*": oper = new OperationMul(); break;
            case "/": oper = new OperationDiv(); break;
        }
        return oper;
    }
}

/**
 * 使用 ”计算器“
 */
class Main2 {
    public static void main(String[] args) {
        AbstractOperation operate = OperationFactory.createOperate("+");
        operate.setNumA(1);
        operate.setNumB(1);
        System.out.println(operate.GetResult());
    }
}
/*
以上设计的优点：
1. 如果要该相关子业务的计算规则，只需改写对应的业务类。不影响其他业务。
2. 如果增加相关子业务，只需新添子业务类，在工厂中添加相关子业务即可。
 */
