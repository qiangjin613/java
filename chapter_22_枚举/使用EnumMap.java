/*
EnumMap 是一种特殊的 Map，
它要求其中的键（key）必须来自一个 enum，
由于 enum 本身的限制，所以 EnumMap 在内部可由数组实现。
因此 EnumMap 的速度很快，
我们可以放心地使用 enum 实例在 EnumMap 中进行查找操作。

不过，我们只能将 enum 的实例作为键来调用 put() 方法，
其他操作与使用一般的 Map 差不多。
 */

import java.util.EnumMap;
import java.util.Map;

/**
 * 使用命令模式来展示 EnumMap 的用法
 */
interface Command {
    void action();
}
class EnumMaps {
    public static void main(String[] args) {
        EnumMap<AlarmPoints, Command> em = new EnumMap<>(AlarmPoints.class);
        em.put(AlarmPoints.KITCHEN, () -> System.out.println("Kitchen"));
        em.put(AlarmPoints.BATHROOM, () -> System.out.println("Bathroom"));
        for (Map.Entry<AlarmPoints, Command> e : em.entrySet()) {
            System.out.print(e.getKey() + ": ");
            e.getValue().action();
        }
        try {
            em.get(AlarmPoints.UTILITY).action();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}

/*
与常量相关的方法相比，
EnumMap 有一个优点，
EnumMap 允许程序员改变值对象，
而常量相关的方法在编译期就被固定了。

在有多种类型的 enum，而且它们之间存在互操作的情况下，
可以用 EnumMap 实现多路分发（multiple dispatching）。
 */
