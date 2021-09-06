import java.util.*;

/*
使用 Arrays.toString() 来生成数组的可打印形式。
 */
class PrintingCollections {
    static Collection fill(Collection<String> collection) {
        collection.add("rat");
        collection.add("cat");
        collection.add("dog");
        collection.add("dog");
        return collection;
    }
    static Map fill(Map<String, String> map) {
        map.put("rat", "Fuzzy");
        map.put("cat", "Rags");
        map.put("dog", "Bosco");
        map.put("dog", "Spot");
        return map;
    }

    public static void main(String[] args) {
        /*
        默认的打印行为，使用集合提供的 toString() 方法即可生成可读性很好的结果。
         */
        System.out.println(fill(new ArrayList<>()));
        System.out.println(fill(new LinkedList<>()));
        System.out.println(fill(new HashSet<>()));
        /* Output:
        [rat, cat, dog, dog]
        [rat, cat, dog, dog]
        [rat, cat, dog]
         */

        System.out.println(Arrays.toString(new Integer[] {1, 2, 3, 4}));
        System.out.println(new Integer[] {1, 2, 3, 4});
        /* Output:
        [1, 2, 3, 4]
        [Ljava.lang.Integer;@4554617c
         */
    }
}
