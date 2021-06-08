/*
Lambda 表达式是使用最小可能语法编写的函数定义。
 */

interface Description2 {
    String brief();
}
interface Body {
    String detailed(String head);
}
interface Multi {
    String twoArg(String head, Double d);
}

class LambdaExpressions {
    static Body bod1 = h -> h + " No Parens!";
    static Body bod2 =(h) -> h + " More details";
    static Description2 desc = () -> "Short info";
    static Multi multi = (h, n) -> h + n;
    static Description2 moreLines = () -> {
        System.out.println("moreLines 的方法体");
        return "from Description2 的 moreLines";
    };

    /*
    1. 没有方法体的 Lambda 表达式的结果自动成为 Lambda 表达式的返回值（是不需要 return 的）；
    2. 如果 Lambda 表达式需要多行，则必须放在方法体 {} 中，并且需要 return 显示返回。
     */


    public static void main(String[] args) {
        System.out.println(bod1.detailed("bod1"));
        System.out.println(bod2.detailed("bod2"));
        System.out.println(desc.brief());
        System.out.println(multi.twoArg("money", 100d));
        System.out.println(moreLines.brief());
    }
}


/*
【递归】
编写递归的 Lambda 表达式。
注意：递归方法必须是实例变量或静态变量，否则会出现编译时错误。
 */
interface IntCall {
    int call(int arg);
}

/**
 * 静态变量：阶乘函数
 */
class RecursiveFactorial {
    static IntCall fact;

    public static void main(String[] args) {
        fact = n -> n == 0 ? 1 : n * fact.call(n - 1);
        for (int i = 0; i < 10; i++) {
            System.out.println(fact.call(i));
        }
    }
}

/**
 * 示例变量：Fibonacci 序列
 */
class RecursiveFibonacci {
    IntCall fib;
    RecursiveFibonacci() {
        fib = n -> n == 0 ? 0 :
                n == 1 ? 1 :
                        fib.call(n - 1) + fib.call(n - 2);
    }

    int fibonacci(int n) {
        return fib.call(n);
    }

    public static void main(String[] args) {
        RecursiveFibonacci rf = new RecursiveFibonacci();
        for (int i = 0; i < 10; i++) {
            System.out.println(rf.fibonacci(i));
            /* 或者 */
            System.out.println(rf.fib.call(i));
        }
    }
}
