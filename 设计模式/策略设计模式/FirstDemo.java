package 策略设计模式;

import java.util.Arrays;

/*
简单的使用继承实现策略模式

创建一个能根据传入的参数类型从而具备不同行为的方法称为策略设计模式。
方法包含算法中不变的部分，策略包含变化的部分。策略就是传入的对象，它包含要执行的代码。

在这里，Processor 对象是策略，Test 的 main() 方法展示了三种不同的应用于 String s 上的策略。
 */


// 通用的策略
class Procesor {
    public String name() {
        return getClass().getSimpleName();
    }
    public Object process(Object input) {
        return input; // 在这里，返回什么不重要，这个方法主要是给其继承类调用；  其实，这里也可以是一个所有都有的通用版本！
    }
}

// 不同的策略
class Upcase extends Procesor {
    @Override
    public String process(Object input) {
        return ((String) input).toUpperCase();
    }
}
class Downcase extends Procesor {
    @Override
    public String process(Object input) {
        return ((String) input).toLowerCase();
    }
}
class Splitter extends Procesor {
    @Override
    public String process(Object input) {
        return Arrays.toString(((String) input).split(""));
    }
}

// 测试类
class Test {
    public static void apply(Procesor p, Object s) {
        System.out.println(p.name() + " " + p.process(s));
    }

    public static void main(String[] args) {
        String s = "We are such stuff as dreams are made on";
        apply(new Upcase(), s);
        apply(new Downcase(), s);
        apply(new Splitter(), s);
    }
}



/*
不巧，现在发现了另一组类 Waveform，貌似能使用 Applicator 的 apply() 方法。
 */

class Waceform {
    private static long counter;
    private final long id = counter++;

    @Override
    public String toString() {
        return "Waceform{id=" + id + '}';
    }
}

// 与 FirstDemo 中的 Procesor 极为相似
class Filter {
    public String name() {
        return getClass().getSimpleName();
    }
    public Waceform process(Waceform input) {
        return input;
    }
}

class LowPass extends Filter {
    double cutoff;

    public LowPass(double cutoff) {
        this.cutoff = cutoff;
    }

    @Override
    public Waceform process(Waceform input) {
        return input; // 哑处理
    }
}

class HighPass extends Filter {
    double cutoff;

    public HighPass(double cutoff) {
        this.cutoff = cutoff;
    }

    @Override
    public Waceform process(Waceform input) {
        return input; // 哑处理
    }
}

// 测试类
class Test2 {

    public static void main(String[] args) {
        String s = "We are such stuff as dreams are made on";
        /* 编译错误 */
        // Test.apply(new LowPass(1), s);
        // Test.apply(new HighPass(2), s);
    }
}


/*
在 Test 的 apply(Procesor p, Object s) 方法无法使用在 Filter 上，
这是因为 Filter 和 Procesor 没有任何关系，而且在 Filter 的派生类 LowPass/HighPass 中无法再继承 Procesor 类。

主要是因为 Applicator 的 apply() 方法和 Processor 过于耦合，这阻止了 Applicator 的 apply() 方法被复用。
另外要注意的一点是 Filter 类中 process() 方法的输入输出都是 Waveform。

但如果 Processor 是一个接口，那么限制就会变得松动到足以复用 Applicator 的 apply() 方法，用来接受那个接口参数。
 */