/*
当要处理多种交互类型时，程序可能会变得相当杂乱。

举例来说，
如果一个系统要分析和执行数学表达式。
我们可能会声明 Number.plus(Number)，Number.multiple(Number) 等等，
其中 Number 是各种数字对象的超类。
然而，当你声明 a.plus(b) 时，
你并不知道 a 或 b 的确切类型，
那你如何能让它们正确地交互呢？

可能，从未思考过这个问题的答案。
【Java 只支持单路分发。】
也就是说，如果要执行的操作包含了不止一个类型未知的对象时，
那么 Java 的动态绑定机制只能处理其中一个的类型。
这就无法解决我们上面提到的问题。
所以，你必须自己来判定其他的类型，从而实现自己的动态限定行为。

解决上面问题的办法就是多路分发。（封装+多态）
对于上面的问题，如果你想使用两路分发，那么就必须有两个方法调用：
    第一个方法调用决定第一个未知类型，
    第二个方法调用决定第二个未知的类型。
 */

import java.util.Random;

/**
 * 下面，使用 RoShamBo “石头、剪刀、布” 游戏具体说明：
 */
enum OutCome {
    WIN, LOSE, DRAW
}
interface Item {
    OutCome compete(Item it);
    OutCome eval(Paper p);
    OutCome eval(Scissors p);
    OutCome eval(Rock p);
}
class Paper implements Item {

    @Override
    public OutCome compete(Item it) {
        return it.eval(this);
    }

    @Override
    public OutCome eval(Paper p) {
        return OutCome.DRAW;
    }
    @Override
    public OutCome eval(Scissors p) {
        return OutCome.WIN;
    }
    @Override
    public OutCome eval(Rock p) {
        return OutCome.LOSE;
    }
}
class Scissors implements Item {

    @Override
    public OutCome compete(Item it) {
        return it.eval(this);
    }

    @Override
    public OutCome eval(Paper p) {
        return OutCome.LOSE;
    }
    @Override
    public OutCome eval(Scissors p) {
        return OutCome.DRAW;
    }
    @Override
    public OutCome eval(Rock p) {
        return OutCome.WIN;
    }
}
class Rock implements Item {

    @Override
    public OutCome compete(Item it) {
        return it.eval(this);
    }

    @Override
    public OutCome eval(Paper p) {
        return OutCome.WIN; }
    @Override
    public OutCome eval(Scissors s) {
        return OutCome.LOSE; }
    @Override
    public OutCome eval(Rock r) {
        return OutCome.DRAW; }
    @Override
    public String toString() {
        return "Rock"; }
}
class RoShamBo {
    static final int SIZE = 20;
    private static Random random = new Random(47);
    public static Item newItem() {
        switch (random.nextInt(3)) {
            default:
            case 0: return new Scissors();
            case 1: return new Paper();
            case 2: return new Rock();
        }
    }
    public static void match(Item a, Item b) {
        System.out.println(a + " vs. "  + b + ": " + a.compete(b));
    }

    public static void main(String[] args) {
        for (int i = 0; i < SIZE; i++) {
            match(newItem(), newItem());
        }
    }
}
/*
Item 是这几种类型的接口，将会被用作多路分发。
RoShamBo.match() 有两个 Item 参数，
通过调用 Item.compete() 方法开始两路分发:
    1.判定 a 的类型，分发机制会在 a 的实际类型的 compete（内部起到分发的作用）
    2.compete() 方法通过调用 eval() 来为另一个类型实现第二次分法。

将自身（this）作为参数调用 eval，能够调用重载过的 eval() 方法，
这能够保留第一次分发的类型信息。
当第二次分发完成时，你就能够知道两个 Item 对象的具体类型了。
 */


/*
要配置好多路分发需要很多的工序，
不过要记住，它的**好处在于方法调用时的优雅**，
这避免了在一个方法中判定多个对象的类型的丑陋代码，
你只需说，“嘿，你们两个，我不在乎你们是什么类型，请你们自己交流！”
不过，在使用多路分发前，
请先明确，这种优雅的代码对你确实有重要的意义。
 */
