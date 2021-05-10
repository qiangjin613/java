/**
 * 仅需要把对象的引用（object references）放置在一个新的类里，这就使用了组合。
 *
 * 惰性加载：（需要用到这个引用/对象时，再进行初始化，可减少编译器的负担）
 *      对于非基本类型对象，引用直接防止在新类中；
 *      对于基本类型属性字段则仅进行声明。
 */
class WaterSource {
    private String s;
    WaterSource() {
        System.out.println("WaterSource()");
        s = "Constructed";
    }

    @Override
    public String toString() {
        return "WaterSource{" +
                "s='" + s + '\'' +
                '}';
    }
}
class SprinklerSystem {
    private String valve1, valve2, valve3;
    private WaterSource source = new WaterSource();
    private int i;
    private float f;

    @Override
    public String toString() {
        return "SprinklerSystem{" +
                "valve1='" + valve1 + '\'' +
                ", valve2='" + valve2 + '\'' +
                ", valve3='" + valve3 + '\'' +
                // 字符串只能拼接另一个字符串，
                // 所以在遇到引用时，会自动调用 toString() 将其转换成一个字符串
                ", source=" + source +
                ", i=" + i +
                ", f=" + f +
                '}';
    }

    public static void main(String[] args) {
        SprinklerSystem sprinklers = new SprinklerSystem();
        System.out.println(sprinklers);
    }
}

/**
 * 编译器不会为每一个引用创建一个默认对象，
 * 这是有意义的，因为再许多情况下，会导致不必要的开销。
 *
 * 初始化引用的四个位置：
 *      1) 定义时初始化
 *      2) 构造函数中初始化
 *      3) 使用实例初始化
 *      4) 实际使用对象之前。通常称为“延迟初始化”。在对象创建开销大且不需要每次都创建对象的情况下，可减少开销
 *
 * 注意：如果试图对未初始化的引用调用方法，则未初始化的引用将产生运行时异常！
 */
class Soap {
    private String s;
    Soap() {
        System.out.println("Soap()");
        s = "Constructed";
    }

    @Override
    public String toString() {
        return "Soap{" +
                "s='" + s + '\'' +
                '}';
    }
}
class Bath {
    // [1] 定义时初始化
    private String s1 = "happy";
    private String s3, s4;
    private Soap castille;
    private int i;
    private float toy;
    public Bath() {
        System.out.println("Inside Bath()");
        // [2] 构造函数中初始化
        s3 = "Joy";
        toy = 3.14f;
        castille = new Soap();
    }
    // [3] 使用实例初始化
    {
        i = 47;
    }

    @Override
    public String toString() {
        if (s4 == null) {
            // [4] 实际使用对象之前
            s4 = "Joy";
        }
        return "Bath{" +
                "s1='" + s1 + '\'' +
                ", s3='" + s3 + '\'' +
                ", s4='" + s4 + '\'' +
                ", castille=" + castille +
                ", i=" + i +
                ", toy=" + toy +
                '}';
    }

    public static void main(String[] args) {
        Bath b = new Bath();
        System.out.println(b);
    }
}