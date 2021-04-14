/**
 * 向上转型永远是安全的。
 * 派生类是基类的一个超集。派生类可能比基类包含更多的方法，但它必须至少具有与基类一样的方法。
 *
 * 向上转型期间，类接口只可能失去方法，但你不会增加方法。
 * 这就是为什么编译器在没有任何明确转型或其他特殊标记情况下，仍然允许向上转型的原因。
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
        Instrument.tune(flute); // flute 向上转型为 Instrument 类型
    }
}