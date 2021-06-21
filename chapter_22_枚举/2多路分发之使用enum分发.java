/*
直接将使用接口分发的代码翻译为基于 enum 的版本是有问题的，
因为 enum 实例不是类型，不能将 enum 实例作为参数的类型，
所以无法重载 eval() 方法。

不过，还有很多方式可以实现多路分发，并从 enum 中获益。
 */

/**
 * 一种方式是使用构造器来初始化每个 enum 实例，
 * 并以“一组”结果作为参数。
 * 这二者放在一块，形成了类似查询表的结构：
 */
interface Competitor<T extends Competitor<T>> {
    OutCome compete(T competitor);
}

enum RoShamBo2 implements Competitor<RoShamBo2> {
    PAPER(OutCome.DRAW, OutCome.LOSE, OutCome.WIN),
    SCISSORS(OutCome.WIN, OutCome.DRAW, OutCome.LOSE),
    ROCK(OutCome.LOSE, OutCome.WIN, OutCome.DRAW);
    private OutCome vPAPER, vSCISSORS, vROCK;
    RoShamBo2(OutCome paper, OutCome scissors, OutCome rock) {
        this.vPAPER = paper;
        this.vSCISSORS = scissors;
        this.vROCK = rock;
    }

    @Override
    public OutCome compete(RoShamBo2 it) {
        switch (it) {
            default:
            case PAPER:
                return vPAPER;
            case SCISSORS:
                return vSCISSORS;
            case ROCK:
                return vROCK;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            RoShamBo2 a = Enums.random(RoShamBo2.class);
            RoShamBo2 b = Enums.random(RoShamBo2.class);
            System.out.println(a + " vs. "  + b + ": " + a.compete(b));
        }

        // 等效于：
//        RoShamBo21.play(RoShamBo2.class, 20);
    }
}
/*
在 RoShamBol.java 中，
两次分发都是通过实际的方法调用实现，
而在这个例子中，只有第一次分发是实际的方法调用。
第二个分发使用的是 switch，不过这样做是安全的，
因为 enum 限制了 switch 语句的选择分支。
 */


class RoShamBo21 {
    public static <T extends Competitor<T>> void match(T a, T b) {
        System.out.println(a + " vs. "  + b + ": " + a.compete(b));
    }
    public static <T extends Enum<T> & Competitor<T>> void play(Class<T> rsbClass, int size) {
        for (int i = 0; i < size; i++) {
            match(Enums.random(rsbClass), Enums.random(rsbClass));
        }
    }
}
