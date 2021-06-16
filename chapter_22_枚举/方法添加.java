/*
【方法添加】
顾名思义：在枚举中添加方法。
> 除了不能继承自一个 enum 之外，我们基本上可以将 enum 看作一个常规的类
也就是说我们可以向 enum 中添加方法。enum 甚至可以有 main() 方法。
 */

import java.util.stream.Stream;

/*
一般来说，我们希望每个枚举实例能够返回对自身的描述，
而不仅仅只是默认的 toString() 实现（因为这只能返回枚举实例的名字）。

为此，可以提供一个构造器，
专门负责处理这个额外的信息，
然后添加一个方法，返回这个描述信息。
 */
enum OzWitch {

    /*
    enum 的语法：
        1. 如果打算定义自己的方法，那么必须在 enum 实例序列的最后添加一个分号。
        2. Java 要求你必须先定义 enum 实例，再定义方法或属性。
     */

    /* 定义 enum 实例 */
    WEST("West West West West"),
    NORTH("North North North North"),
    EAST("East East East East"),
    SOUTH("South South South South");

    /* 实例属性 */
    private String description;

    /*
    构造器（默认是 private 私有的）
    只能在 enum 定义的内部使用其构造器创建 enum 实例，
    一旦 enum 的定义结束，
    编译器就不允许我们再使用其构造器来创建任何实例了。
     */
    private OzWitch(String desc) {
        description = desc;
    }

    /* 实例方法 */
    public String getDescription() {
        return description;
    }

    /* 静态方法 */
    public static void main(String[] args) {
        for (OzWitch witch : OzWitch.values()) {
            System.out.println(witch + ": " + witch.getDescription());
        }
    }
}


/*
【覆盖 enum 的方法】
可以对 enum 的方法进行覆盖。
如，toString()
 */
enum SpaceShip {
    SCOUT, CARGO, TRANSPORT,
    CRUISER, BATTLESHIP, MOTHERSHIP;

    @Override
    public String toString() {
        String id = name();
        String lower = id.substring(1).toLowerCase();
        return id.charAt(0) + lower;
    }

    public static void main(String[] args) {
        Stream.of(values()).forEach(System.out::println);
    }
}



















