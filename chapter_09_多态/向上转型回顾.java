/**
 * 音乐乐器的例子
 */
enum Note {
    // 音符
    MIDDLE_C, C_SHARP, B_FLAT;
}
/**
 * 乐器
 */
class Instrument2 {
    public void play(Note n) {
        System.out.println("Instrument2.play() " + n);
    }
}
class Wind2 extends Instrument2 {
    // 覆盖基类方法
    @Override
    public void play(Note n) {
        System.out.println("Wind.play() " + n);
    }
}
/**
 * 播放器
 */
class Music {
    public static void tune(Instrument2 i) {
        i.play(Note.MIDDLE_C);
    }

    public static void main(String[] args) {
        Wind2 flute = new Wind2();
        // 向上转型
        tune(flute);
    }
}


/**
 * 【忘掉对象类型】
 * 当向上转型发生时就会“忘调”对象类型，如果 tune() 使用 Wind 不是更为直观吗？
 * 按照这种推理，再增加 Stringed 和 Brass 这两种 Instrument。
 */
class Stringed extends Instrument2 {
    @Override
    public void play(Note n) {
        System.out.println("Stringed play()" + n);
    }
}
class Brass extends Instrument2 {
    @Override
    public void play(Note n) {
        System.out.println("Brass play()" + n);
    }
}
class Music2 {
    public static void tune(Instrument2 i) {
        i.play(Note.MIDDLE_C);
    }
    public static void tune(Wind2 i) {
        i.play(Note.MIDDLE_C);
    }
    public static void tune(Stringed i) {
        i.play(Note.MIDDLE_C);
    }
    public static void tune(Brass i) {
        i.play(Note.MIDDLE_C);
    }

    public static void main(String[] args) {
        Instrument2 i = new Instrument2();
        Wind2 w = new Wind2();
        Stringed s = new Stringed();
        Brass b = new Brass();
        tune(i); // output: Instrument2.play() MIDDLE_C
        tune(w); // output: Wind.play() MIDDLE_C
        tune(s); // output: Stringed play()MIDDLE_C
        tune(b); // output: Brass play()MIDDLE_C
    }
}

/*
 * 但这会带来一个问题：
 *      要为每一个 Instrument 都编写一个新的 tune() 方法（写多个重载方法）。
 *      这就意味着需要更多的编程，而且以后如果添加类似 tune() 的新方法或 Instrument 的
 *      新类型时，还有大量的工作要做。
 *      如果忘记重载某个方法，编译器也不会提示，会造成类型的整个处理过程变得难以管理。
 * 如果只写一个方法以基类作为参数，而不用管是哪个具体派生类，这样会变得更好吗？
 * 这个正是多态允许的！
 *
 * （详看“转机“章节...)
 */
