/*
我们还可以进一步简化实现两路分发的解决方案。

我们注意到，每个 enum 实例都有一个固定的值（基于其声明的次序），
并且可以通过 ordinal() 方法取得该值。

因此我们可以使用二维数组，将竞争者映射到竞争结果。

采用这种方式能够获得最简洁、最直接的解决方案。
（很可能也是最快速的，虽然我们知道 EnumMap 内部其实也是使用数组实现的）
 */
enum RoShamBo6 implements Competitor<RoShamBo6> {
    PAPER, SCISSORS, ROCK;
    private static OutCome[][] table = {
            {OutCome.DRAW, OutCome.LOSE, OutCome.WIN}, // PAPER
            { OutCome.WIN, OutCome.DRAW, OutCome.LOSE }, // SCISSORS
            { OutCome.LOSE, OutCome.WIN, OutCome.DRAW }, // ROCK
    };

    @Override
    public OutCome compete(RoShamBo6 other) {
        return table[this.ordinal()][other.ordinal()];
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            RoShamBo3 a = Enums.random(RoShamBo3.class);
            RoShamBo3 b = Enums.random(RoShamBo3.class);
            System.out.println(a + " vs. "  + b + ": " + a.compete(b));
        }
    }
}
/*
这个程序代码虽然简短，但表达能力却更强，
部分原因是其代码更易于理解与修改，而且也更直接。

不过，由于它使用的是数组，所以这种方式不太“安全”。
如果使用一个大型数组，
可能会不小心使用了错误的尺寸，
而且，如果你的测试不能覆盖所有的可能性，有些错误可能会从你眼前溜过。

事实上，以上所有的解决方案只是各种不同类型的表罢了。
不过，分析各种表的表现形式，找出最适合的那一种，还是很有价值的。

注意，虽然上例是最简洁的一种解决方案，但它也是相当僵硬的方案，
因为它只能针对给定的常量输入产生常量输出。

然而，也没有什么特别的理由阻止你用 table 来生成功能对象。
对于某类问题而言，“表驱动式编码”的概念具有非常强大的功能。
 */
