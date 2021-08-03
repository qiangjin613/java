/*
【函数对象】
一个 函数对象 封装了一个函数。其特点就是将被调用函数的选择与那个函数被调用的位置进行解耦。
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/*
【命令模式】
从最直观的角度来看，
命令模式 就是一个函数对象：一个作为对象的函数。
我们可以将 函数对象 作为参数传递给其他方法或者对象，来执行特定的操作。
 *
在Java 8之前，想要产生单个函数的效果，我们必须明确将方法包含在对象中，
而这需要太多的仪式了。
而利用Java 8的lambda特性， 命令模式 的实现将是微不足道的。
 */

/**
 * 一个简单的例子：
 */
class CommandPattern {
    public static void main(String[] args) {
        List<Runnable> macro = Arrays.asList(
                () -> System.out.print("Hello "),
                () -> System.out.println("World!"),
                () -> System.out.println("I'm the command pattern!")
        );
        macro.forEach(Runnable::run);
    }
}
/*
命令模式 的主要特点是允许向一个方法或者对象传递一个想要的动作。
在上面的例子中，这个对象就是 macro，而 命令模式 提供了将一系列需要一起执行的动作集进行排队的方法。
在这里，命令模式 允许我们动态的创建新的行为。
 *
《设计模式》 认为“命令模式是回调的面向对象的替代品”。
尽管如此，我认为"back"（回来）这个词是callback（回调）这一概念的基本要素。
也就是说，我认为回调（callback）实际上是返回到回调的创建者所在的位置。
另一方面，对于 命令 对象，通常只需创建它并将其交给某种方法或对象，
而不是自始至终以其他方式联系命令对象。
不管怎样，这就是我（作者）对它的看法。
 */


/*
【策略模式】
策略模式 看起来像是从同一个基类继承而来的一系列 命令 类。
但是仔细查看 命令模式，你就会发现它也具有同样的结构：一系列分层次的 函数对象。
 *
不同之处在于，这些函数对象的用法和策略模式不同。
就像前面的 io/DirList.java 那个例子：
使用 命令 是为了解决特定问题 -- 从一个列表中选择文件。
“不变的部分”是被调用的那个方法，而变化的部分被分离出来放到 函数对象 中。
 *
我认为 命令模式 在编码阶段提供了灵活性，而 策略模式 的灵活性在运行时才会体现出来。
尽管如此，这种区别却是非常模糊的。
 *
另外，策略模式 还可以添加一个“上下文（context）”，
这个上下文（context）可以是一个代理类（surrogate class），
用来控制对某个特定 策略 对象的选择和使用。
就像 桥接模式 一样！
 */

/**
 * 一个小例子
 */
// 常用策略的基类型:
class FindMinima {
    Function<List<Double>, List<Double>> algorithm;
}

// 不同的策略:
class LeastSquares extends FindMinima {
    LeastSquares() {
        algorithm = (line) -> Arrays.asList(1.1, 2.2);
    }
}
class Perturbation extends FindMinima {
    Perturbation() {
        algorithm = (line) -> Arrays.asList(3.3, 4.4);
    }
}

// Context 控制策略：
class MinimaSolver {
    private FindMinima strategy;

    MinimaSolver(FindMinima state) {
        strategy = state;
    }

    List<Double> minima(List<Double> line) {
        return strategy.algorithm.apply(line);
    }

    void changeAlgorithm(FindMinima newAlgorithm) {
        strategy = newAlgorithm;
    }
}

class StrategyPattern {
    public static void main(String[] args) {
        MinimaSolver solver = new MinimaSolver(new LeastSquares());
        List<Double> line = Arrays.asList(
                1.0, 2.0, 1.0, 2.0, -1.0,
                3.0, 4.0, 5.0, 4.0);
        System.out.println(solver.minima(line));

        // 改变策略：
        solver.changeAlgorithm(new Perturbation());
        System.out.println(solver.minima(line));
    }
}
/*
MinimaSolver 中的 changeAlgorithm() 方法将一个不同的策略插入到了 私有 域 strategy 中，
这使得在调用 minima() 方法时，可以使用新的策略。
 */

/*
下面将上下文注入到 FindMinima 中来简化解决方案
 */
class FindMinima2 {
    Function<List<Double>, List<Double>> algorithm;

    FindMinima2() {
        /* 默认使用策略 */
        leastSquares();
    }

    /* 将上下文合并到策略基类中 */
    void leastSquares() {
        algorithm = (line) -> Arrays.asList(1.1, 2.2);
    }
    void perturbation() {
        algorithm = (line) -> Arrays.asList(3.3, 4.4);
    }
    void bisection() {
        algorithm = (line) -> Arrays.asList(5.5, 6.6);
    }

    List<Double> minima(List<Double> line) {
        return algorithm.apply(line);
    }
}

class StrategyPattern2 {
    public static void main(String[] args) {
        FindMinima2 solver = new FindMinima2();
        List<Double> line = Arrays.asList(
                1.0, 2.0, 1.0, 2.0, -1.0,
                3.0, 4.0, 5.0, 4.0);
        System.out.println(solver.minima(line));

        // 改变策略：
        solver.bisection();
        System.out.println(solver.minima(line));
    }
}
/*
FindMinima2 封装了不同的算法，也包含了“上下文”（Context），
所以它便可以在一个单独的类中控制算法的选择了。
 */


/*
【责任链模式】
责任链模式 也许可以被看作一个使用了 策略 对象的“递归的动态一般化”。
此时我们进行一次调用，在一个链序列中的每个策略都试图满足这个调用。
这个过程直到有一个策略成功满足该调用或者到达链序列的末尾才结束。
 *
在递归方法中，一个方法将反复调用它自身直至达到某个终止条件；
 *
使用责任链，一个方法会调用相同的基类方法（拥有不同的实现），
这个基类方法将会调用基类方法的其他实现，
如此反复直至达到某个终止条件。
 *
除了调用某个方法来满足某个请求以外，
链中的多个方法都有机会满足这个请求，
因此它有点专家系统的意味。
 *
由于责任链实际上就是一个链表，它能够动态创建，
因此它可以看作是一个更一般的动态构建的 switch 语句。
 */

/**
 * 在上面的 StrategyPattern.java 例子中，
 * 我们可能想自动发现一个解决方法。
 * 而 责任链 就可以达到这个目的：
 */
class Result {
    boolean success;
    List<Double> line;

    Result(List<Double> data) {
        success = true;
        line = data;
    }
    Result() {
        success = false;
        line = Collections.<Double>emptyList();
    }
}
class Fail extends Result {}

interface Algorithm {
    Result algorithm(List<Double> line);
}

class FindMinima3 {
    // 责任链：
    static List<Function<List<Double>, Result>> algorithm = Arrays.asList(
            FindMinima3::leastSquares,
            FindMinima3::bisection,
            FindMinima3::perturbation);

    // 一个算法的列表（这就是所谓的“链”）：
    public static Result leastSquares(List<Double> line) {
        System.out.println("leastSquares.algorithm");
        boolean weSucceed = false;
        if (weSucceed) {
            return new Result(Arrays.asList(1.1, 2.2));
        } else {
            return new Fail();
        }
    }
    public static Result perturbation(List<Double> line) {
        System.out.println("perturbation.algorithm");
        boolean weSucceed = true;
        if (weSucceed) {
            return new Result(Arrays.asList(3.3, 4.4));
        } else {
            return new Fail();
        }
    }
    public static Result bisection(List<Double> line) {
        System.out.println("bisection.algorithm");
        boolean weSucceed = false;
        if (weSucceed) {
            return new Result(Arrays.asList(5.5, 6.6));
        } else {
            return new Fail();
        }
    }

    // 轮询策略：
    public static Result minima(List<Double> line) {
        for (Function<List<Double>, Result> alg : algorithm) {
            Result result = alg.apply(line);
            if (result.success) {
                return result;
            }
        }
        return new Fail();
    }
}

class ChainOfResponsibility {
    public static void main(String[] args) {
        FindMinima3 solver = new FindMinima3();
        List<Double> line = Arrays.asList(
                1.0, 2.0, 1.0, 2.0, -1.0,
                3.0, 4.0, 5.0, 4.0);
        Result result = solver.minima(line);
        if (result.success) {
            System.out.println(result.line);
        } else {
            System.out.println("No algorithm found");
        }
    }
}
/*
从定义一个 Result 类开始，这个类包含一个 success 标志，
因此接收者就可以知道算法是否成功执行，而 line 变量保存了真实的数据。
当算法执行失败时， Fail 类可以作为返回值。
 *
要注意的是，
当算法执行失败时，返回一个 Result 对象要比抛出一个异常更加合适，
因为我们有时可能并不打算解决这个问题，而是希望程序继续执行下去。
 *
每一个 Algorithm 接口的实现，都实现了不同的 algorithm() 方法。
在 FindMinama 中，将会创建一个算法的列表（这就是所谓的“链”），
而 minima() 方法只是遍历这个列表，然后找到能够成功执行的算法而已。
 */
