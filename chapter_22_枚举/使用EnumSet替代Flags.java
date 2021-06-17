/*
Set 是一种集合，只能向其中添加不重复的对象。
enum 也要求其成员都是唯一的，所以 enum 看起来也具有集合的行为。

不过，
由于不能从 enum 中删除或添加元素，
所以它只能算是不太有用的集合。

Java SE 5 引入 EnumSet，
是为了通过 enum 创建一种替代品，
以替代传统的基于 int 的“位标志”。
（这种标志可以用来表示某种“开/关”或者“有/无”信息）

EnumSet 的设计充分考虑到了速度因素，
因为它必须与非常高效的 bit 标志相竞争。
（EnumSet 的操作与 HashSet 相比，非常地快）
就其内部而言，
它（可能）就是将一个 long 值作为比特向量，
所以 EnumSet 非常快速高效。
 */

import java.util.EnumSet;

enum AlarmPoints {
    STAIR1, STAIR2, LOBBY, OFFICE1, OFFICE2, OFFICE3,
    OFFICE4, BATHROOM, UTILITY, KITCHEN
}
/**
 * 使用 EnumSet 来跟踪报警器的状态
 */
class EnumSets {
    public static void main(String[] args) {
        EnumSet<AlarmPoints> points = EnumSet.noneOf(AlarmPoints.class);
        System.out.println(points);

        points.add(AlarmPoints.STAIR1);
        System.out.println(points);

        points.addAll(EnumSet.of(AlarmPoints.STAIR1, AlarmPoints.STAIR2, AlarmPoints.LOBBY));
        System.out.println(points);

        points = EnumSet.allOf(AlarmPoints.class);
        System.out.println(points);

        points.removeAll(EnumSet.of(AlarmPoints.STAIR1, AlarmPoints.STAIR2, AlarmPoints.LOBBY));
        System.out.println(points);

        /* 这里的 range() 包含端点值 */
        points.removeAll(EnumSet.range(AlarmPoints.LOBBY, AlarmPoints.UTILITY));
        System.out.println(points);

        /* 补集 */
        points = EnumSet.complementOf(points);
        System.out.println(points);
    }
}

/*
EnumSet.of() 方法被多次重载，
不但为可变数量参数进行了重载，
而且为接收 2 至 5 个显式的参数的情况都进行了重载。
这也从侧面表现了 EnumSet 对性能的关注。
因为，其实只使用单独的 of() 方法解决可变参数已经可以解决整个问题了，
但是对比显式的参数，会有一点性能损失。

采用现在这种设计，当只使用 2 到 5 个参数调用 of() 方法时，
可以调用对应的重载过的方法（速度稍快一点）。
而当使用一个参数或多过 5 个参数时，
调用的将是使用可变参数的 of() 方法。
 */


/*
EnumSet 的基础是 long，
一个 long 值有 64 位.
一个 enum 实例只需一位 bit 表示其是否存在。

也就是说，在不超过一个 long 的表达能力的情况下，
EnumSet 可以应用于最多不超过 64 个元素的 enum。
如果 enum 超过了 64 个元素会发生什么呢？
 */
class BigEnumSet {
    enum Big {
        A0, A1, A2, A3, A4, A5, A6, A7, A8, A9,
        A10, A11, A12, A13, A14, A15, A16, A17, A18, A19,
        A20, A21, A22, A23, A24, A25, A26, A27, A28, A29,
        A30, A31, A32, A33, A34, A35, A36, A37, A38, A39,
        A40, A41, A42, A43, A44, A45, A46, A47, A48, A49,
        A50, A51, A52, A53, A54, A55, A56, A57, A58, A59,
        A60, A61, A62, A63, A64, A65, A66, A67, A68, A69,
        A70, A71, A72, A73, A74, A75
    }

    public static void main(String[] args) {
        EnumSet<Big> bigEnumSet = EnumSet.allOf(Big.class);
        System.out.println(bigEnumSet);
    }
}
/*
显然，EnumSet 是可以应用于多过 64 个元素的 enum 的。
Enum在其内部进行了判断：（部分源码）
        if (universe.length <= 64)
            return new RegularEnumSet<>(elementType, universe);
        else
            return new JumboEnumSet<>(elementType, universe);
RegularEnumSet 是 private long elements = 0L;
而 JumboEnumSet 是 private long elements[];
 */
