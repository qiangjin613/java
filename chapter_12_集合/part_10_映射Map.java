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


/**
 * HashMap 在并发场景下的死循环（Infinite Loop）
 */
class HashMapInfiniteLoop {
    private static HashMap<Integer, String> map = new HashMap<>(2, 0.75f);

    public static void main(String[] args) {
        map.put(5, "C");
        new Thread("Thread1") {
            @Override
            public void run() {
                map.put(7, "8");
                System.out.println(map);
            }
        }.start();
        new Thread("Thread2") {
            @Override
            public void run() {
                map.put(3, "A");
                System.out.println(map);
            }
        }.start();

        Thread.yield();
        System.out.println(map.get(11));
        System.out.println("end");
    }
}




/****************** 测试 HashMap 的性能 ********************/

/**
 * 一个工具类 Key
 */
class Key implements Comparable<Key> {

    private final int value;

    Key(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Key o) {
        return Integer.compare(this.value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Key key = (Key) o;
        return value == key.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}

/**
 * Keys 的缓存实例
 */
class Keys {
    public static final int MAX_KEY = 10_000_000;
    public static final Key[] KEYS_CACHE = new Key[MAX_KEY];

    static {
        for (int i = 0; i < MAX_KEY; i++) {
            KEYS_CACHE[i] = new Key(i);
        }
    }

    public static Key of(int value) {
        return KEYS_CACHE[value];
    }
}

/**
 * 现在开始我们的试验，创建不同size的HashMap（1、10、100、......10000000），屏蔽了扩容的情况
 */
class TestHashMap {
    static void test(int mapSize) {
        HashMap<Key, Integer> map = new HashMap<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            map.put(Keys.of(i), i);
        }

        long beginTime = System.nanoTime(); /* 获取纳秒 */
        for (int i = 0; i < mapSize; i++) {
            map.get(Keys.of(i));
        }

        long endTime = System.nanoTime();
        /* 计算getKey的平均时间，我们遍历所有的get方法，计算总的时间，除以key的数量，计算一个平均值 */
        System.out.println((endTime - beginTime) * 1.0 / mapSize);
    }

    public static void main(String[] args) {
        for (int i = 10; i < 10_000_000; i *= 10) {
            test(i);
        }
    }
}
