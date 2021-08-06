/*
每当一个方法与一个类而不是接口一起工作时(当方法的参数是类而不是接口)，
你只能应用那个类或它的子类。
如果你想把这方法应用到一个继承层次之外的类，在不使用接口，是做不到的。

接口在很大程度上放宽了这个限制，因而使用接口可以编写复用性更好的代码。
（完全解耦...）
 */

/**
 * 一个”完全解耦“的例子：
 */
class Processor {
    public String name() {
        return getClass().getSimpleName();
    }
    public Object process(Object input) {
        return input;
    }
}
class Upcase extends Processor {
    /* 返回协变类型 */
    @Override
    public String process(Object input) {
        return ((String) input).toUpperCase();
    }
}
class Downcase extends Processor {
    @Override
    public String process(Object input) {
        return ((String) input).toLowerCase();
    }
}
class Applicator {
    public static void apply(Processor p, Object s) {
        System.out.println("Using Processor " + p.name());
        System.out.println(p.process(s));
    }

    public static void main(String[] args) {
        String s = "we are such s dreams are made on";
        /* apply() 可以接收任意类型的实现 Processor 接口的类 */
        apply(new Upcase(), s);
        apply(new Downcase(), s);
    }
}
/*
本例中，创建一个能根据传入的参数类型从而具备不同行为的方法称为策略设计模式。
策略就是传入的对象，是变化的部分。
在 main() 中，展示了两种不同的应用于 String s 上的策略。
 */


/*
另一个例子，
假设现在发现了一组电子滤波器，它们看起来好像能使用 Applicator 的 apply() 方法：
 */
class Waveform2 {
    private static long counter;
    private final long id = counter++;

    @Override
    public String toString() {
        return "Waveform " + id;
    }
}
class Filter2 {
    public String name() {
        return getClass().getSimpleName();
    }
    public Waveform2 process(Waveform2 w) {
        return w;
    }
}
class LowPass extends Filter2 {
    double cutoff;
    public LowPass(Double cutoff) {
        this.cutoff = cutoff;
    }

    /* Dummy processing 哑处理 */
    @Override
    public Waveform2 process(Waveform2 w) {
        return w;
    }
}
class HighPass extends Filter2 {
    double cutoff;
    public HighPass(double cutoff) {
        this.cutoff = cutoff;
    }

    @Override
    public Waveform2 process(Waveform2 w) {
        return w;
    }
}
class BandPass extends Filter2 {
    double lowCutoff, hightCutoff;
    public BandPass(double low, double hig) {
        lowCutoff = low;
        hightCutoff = hig;
    }

    @Override
    public Waveform2 process(Waveform2 w) {
        return w;
    }
}
/*
Filter 类与 Processor 类具有相同的接口元素，但是因为它不是继承自 Processor，
因此，不能将 Applicator 的 apply() 方法应用在 Filter 类上。
 *
主要是因为 Applicator 的 apply() 方法和 Processor 过于耦合，
这阻止了 Applicator 的 apply() 方法被复用。
另外要注意的一点是 Filter 类中 process() 方法的输入输出都是 Waveform。
 *
但如果 Processor 是一个接口，
那么限制就会变得松动到足以复用 Applicator 的 apply() 方法，
用来接受那个接口参数。
 */


/**
 * 下面是修改后的 Processor 和 Applicator 版本：
 */
interface Processor2 {
    default String name() {
        return getClass().getSimpleName();
    }
    Object process(Object input);
}
class Upcase2 implements Processor2 {
    // 返回协变类型
    @Override
    public String process(Object input) {
        return ((String) input).toUpperCase();
    }
}
class Downcase2 implements StringProcessor2 {
    @Override
    public String process(Object input) {
        return ((String) input).toLowerCase();
    }
}

class Applicator2 {
    public static void apply(Processor2 p, Object s) {
        System.out.println("Using Processor " + p.name());
        System.out.println(p.process(s));
    }
    public static final String S = "If she weighs the same as a duck, she's made of wood";
    public static void main(String[] args) {
        Applicator2.apply(new Upcase2(), S);
        Applicator2.apply(new Downcase2(), S);
    }
}
/*
复用代码的第一种方式是客户端程序员遵循接口编写类，像这样：
 */
interface StringProcessor2 extends Processor2 {
    /* 这个方法的声明是不必要的，即使这里没有这个方法，也不会报错，这里返回值还是用了协变返回类型 */
    @Override
    String process(Object input);

    String S = "If she weighs the same as a duck, she's made of wood";

    /* 可以在接口中定义 main() 方法 */
    static void main(String[] args) {
        Applicator2.apply(new Upcase2(), S);
        Applicator2.apply(new Downcase2(), S);
    }
}


/*
使用适配器设计模式继续优化。
适配器（FilterAdapter）允许代码接受已有的接口（Filter2）产生需要的接口（Processor2）。
 */
class FilterAdapter implements Processor2 {
    Filter2 filter;

    FilterAdapter(Filter2 filter) {
        this.filter = filter;
    }

    /* 可以重写 default 方法 */
    @Override
    public String name() {
        return filter.name();
    }

    @Override
    public Waveform2 process(Object input) {
        return filter.process((Waveform2) input);
    }
}
class FilterProcessor {
    public static void main(String[] args) {
        Waveform2 w = new Waveform2();
        Applicator2.apply(new FilterAdapter(new LowPass(1.0)), w);
        Applicator2.apply(new FilterAdapter(new HighPass(2.0)), w);
        Applicator2.apply(new FilterAdapter(new BandPass(1.0, 2.0)), w);
    }
}

/*
将接口与实现解耦使得接口可以应用于多种不同的实现，因而代码更具可复用性。
 */
