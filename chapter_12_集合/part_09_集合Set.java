/*
Set 不保存重复的元素。 如果试图将相同对象的多个实例添加到 Set 中，那么它会阻止这种重复行为。
Set 最常见的用途是测试归属性，可以很轻松地询问某个对象是否在一个 Set 中。
因此，查找通常是 Set 最重要的操作，因此通常会选择 HashSet 实现，该实现针对快速查找进行了优化。

Set 具有与 Collection 相同的接口，
因此没有任何额外的功能，不像前面两种不同类型的 List 那样（自己扩展了一些接口）。
实际上， Set 就是一个 Collection ，只是行为不同。
（这是继承和多态思想的典型应用：表现不同的行为）
 */

import java.util.*;

/**
 * HashSet Demo：
 */
class SetOfInteger {
    public static void main(String[] args) {
        Random rand = new Random(47);
        Set<Integer> intset = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            intset.add(rand.nextInt(30));
        }
        System.out.println(intset);
        /* 这里的输出，Integer 按顺序排序 */
    }
}
/*
HashSet 维护的顺序与 TreeSet 或 LinkedHashSet 不同，因为它们的实现具有不同的元素存储方式：
1. 出于对速度的追求，HashSet 使用了散列。
2. TreeSet 将元素存储在红-黑树数据结构中。
3. LinkedHashSet 因为查询速度的原因也使用了散列，
    但是看起来使用了链表来维护元素的插入顺序。
 */
class SetOfString {
    public static void main(String[] args) {
        Set<String> colors = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            colors.add("Yello");
            colors.add("Blue");
            colors.add("Red");
            colors.add("Orange");
            colors.add("Purple");
        }
        System.out.println(colors);
    }
}
/*
String 对象似乎没有排序。
 */

/**
 * 要对结果进行排序，一种方法是使用 TreeSet：
 */
class SortedSetOfString {
    public static void main(String[] args) {
        Set<String> colors = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            colors.add("Yello");
            colors.add("Blue");
            colors.add("Red");
            colors.add("Orange");
            colors.add("Purple");
        }
        System.out.println(colors);
    }
}


/**
 * 最常见的操作之一是使用 contains() 测试成员归属性，但也有一些其它操作：
 */
class SetOperations {
    public static void main(String[] args) {
        Set<String> set1 = new HashSet<>();
        Collections.addAll(set1, "A B C D E F G H I J K L".split(" "));
        set1.add("M");
        System.out.println("H: " + set1.contains("H"));
        System.out.println("N: " + set1.contains("N"));

        Set<String> set2 = new HashSet<>();
        Collections.addAll(set2, "H I J K L".split(" "));
        System.out.println("set2 in set1: " + set1.containsAll(set2));
        set1.remove("H");
        System.out.println("set2 in set1: " + set1.containsAll(set2));

        set1.removeAll(set2);
        System.out.println("set2 removed from set1: " + set1);
        Collections.addAll(set1, "X Y Z".split(" "));
        System.out.println("'X Y Z' added to set1: " + set1);
    }
}

/*
在上述例子中，TreeSet 对结果使用 字典顺序（lexicographically） 排序。
如果想要按 字母顺序（alphabetically） 排序，
可以向 TreeSet 构造器传入比较器来完成。
 */
class UniqueWordsAlphabetic {
    public static void main(String[] args) {
        Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        /* ... */
    }
}
