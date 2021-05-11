/**
 * 向上转型永远是安全的。
 * 派生类是基类的一个超集。派生类可能比基类包含更多的方法，但它必须至少具有与基类一样的方法。
 *
 * 向上转型期间，类接口只可能失去方法，但你不会增加方法。
 * 这就是为什么编译器在没有任何明确转型或其他特殊标记情况下，仍然允许向上转型的原因。
 *
 * 也可以想象成 “基本数据类型的 int 转为更大的 long”，在这里换一种思路：
 * 因为 int 转为 long 不会丢失精度；
 * 派生类转为基类，不会发生基类调用方法然后找不到的情况。因为派生类中一定含有与基类同样的方法，
 *
 * 【乐器的例子】
 */
class Instrument {
    public void play() {}
    static void tune(Instrument i) {
        i.play();
    }
}

class Wind extends Instrument {
    public static void main(String[] args) {
        Wind flute = new Wind();
        /*
        Java 对类型检查十分严格，一个接收一种类型的方法接受了另一种类型看起来很奇怪，
        除非你意识到 Wind 对象同时也是一个 Instrument 对象，
        而且 Instrument 的 tune 方法一定会存在于 Wind 中。
         */
        Instrument.tune(flute); /* flute 向上转型为 Instrument 类型 */

        // 更加明显的展示：
        Instrument i = new Wind(); /* 意味着你可以准确地说 Wind 对象也是一种类型的 Instrument */
        i.play();
    }
}

/**
 * “向上转型”将在 第九章（多态） 中进一步讨论
 *
 * 为什么叫“向上转型”，这是基于传统的类继承图：图最上面是根，然后向下铺展。
 */
