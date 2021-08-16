import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 从 Pet 层次结构生成对象的问题是：
 * 每当向层次结构中添加一种新类型的 Pet 时，
 * 必须记住将其添加到 LiteralPetCreator.java 的条目中。
 * （在一个定期添加更多类的系统中，这可能会成为问题）
 *
 * 基本上，你必须自己手工创建列表（除非你编写了一个工具来搜索和分析源代码，然后创建和编译列表）。
 * 所以你能做的最好的事情就是把列表集中放在一个明显的地方。
 * 层次结构的基类可能是最好的地方。
 *
 * 在“注册工厂”中，使用“工厂方法”设计模式将对象的创建推迟到类本身。
 * 工厂方法可以以多态方式调用，并为你创建适当类型的对象。
 */
class Part implements Supplier<Part> {
    static List<Supplier<? extends Part>> prototypes = Arrays.asList(
            new FuelFilter(),
            new AirFilter(),
            new CabinAirFilter(),
            new OilFilter(),
            new FanBelt(),
            new PowerSteeringBelt(),
            new GeneratorBelt()
    );

    private static Random rand = new Random(47);
    @Override
    public Part get() {
        int n = rand.nextInt(prototypes.size());
        return prototypes.get(n).get();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

class Filter extends Part {}
class FuelFilter extends Filter {
    @Override
    public Part get() {
        return new FuelFilter();
    }
}
class AirFilter extends Filter {
    @Override
    public AirFilter get() {
        return new AirFilter();
    }
}
class CabinAirFilter extends Filter {
    @Override
    public CabinAirFilter get() {
        return new CabinAirFilter();
    }
}
class OilFilter extends Filter {
    @Override
    public OilFilter get() {
        return new OilFilter();
    }
}

class Belt extends Part {}
class FanBelt extends Belt {
    @Override
    public FanBelt get() {
        return new FanBelt();
    }
}
class GeneratorBelt extends Belt {
    @Override
    public GeneratorBelt get() {
        return new GeneratorBelt();
    }
}
class PowerSteeringBelt extends Belt {
    @Override
    public PowerSteeringBelt get() {
        return new PowerSteeringBelt();
    }
}

class RegisteredFactories {
    public static void main(String[] args) {
        Stream.generate(new Part())
                .limit(10)
                .forEach(System.out::println);
    }
}

/*
并非层次结构中的所有类都应实例化；
这里的 Filter 和 Belt 只是分类器，
这样你就不会创建任何一个类的实例，而是只创建它们的子类
 */
