/*
将对象映射到其他对象的能力是解决编程问题的有效方法。
Map 可以很容易地解决这个问题。
 */

import java.util.*;

/**
 * 举一个简单的例子：
 * 测试 Random 是否会产生完美的数字分布。
 */
class Statistics {
    public static void main(String[] args) {
        Random rand = new Random(47);
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            int r = rand.nextInt(20);
            Integer freq = m.get(r);
            m.put(r, freq == null ? 1 : freq + 1);
        }
        System.out.println(m);
    }
}


/*
Map 与数组和其他的 Collection 一样，可以轻松地扩展到多个维度，
只需要创建一个值为 Map 的 Map（这些 Map 的值可以是其他集合，甚至是其他 Map）。
因此，能够很容易地将集合组合起来以快速生成强大的数据结构。
 */

/**
 * 假设你正在追踪有多个宠物的人，只需要一个 Map<Person, List<Pet>> 即可：
 */
class MapOfList {
    public static final Map<Person, List<Integer>> petPeople = new HashMap<>();
    static {
        petPeople.put(new Person("Dawn"), Arrays.asList(1, 2, 3, 4, 5, 65));
        petPeople.put(new Person("Kate"), Arrays.asList(3, 4, 2, 4, 1));
        petPeople.put(new Person("Kate2"), Arrays.asList(3, 4, 2, 4, 1));
        petPeople.put(new Person("Marilyn"), Arrays.asList(0, 3, 2, 1));
    }

    public static void main(String[] args) {
        System.out.println("People: " + petPeople.keySet());
        System.out.println("Pets: " + petPeople.values());
        for (Person person : petPeople.keySet()) {
            System.out.print(person + " has:");
            for (Integer i : petPeople.get(person)) {
                System.out.print("\t" + i);
            }
            System.out.println("\n");
        }
    }
}
