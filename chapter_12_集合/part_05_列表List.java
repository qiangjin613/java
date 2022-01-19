/*
List 承诺将元素保存在特定的序列中，主要有两种类型的 List：
1. ArrayList
    擅长随机访问元素
2. LinkedList
    擅长插入和删除操作，提供优化的顺序访问
 */

import java.util.*;

class ListFeatures {
    public static void main(String[] args) {
//        List<Pet> pets = Pets.list(7);
//        System.out.println(pets);
//        /* “洗牌”操作 */
//        Collections.shuffle(pets);
//        System.out.println(pets);
//
//        /* List -> Array */
//        Object[] objects = pets.toArray();
//        System.out.println(Arrays.toString(objects));
//
//        /* 其内部使用了 equals() 进行比较 */
//        pets.indexOf(pets.get(2));
//
//        List<Pet> sub = Arrays.asList(pets.get(2), pets.get(4), pets.get(6));
//
//        List<Integer> intList = Arrays.asList(new Integer[]{1, 2, 3, 4});
//        Integer[] intArray = intList.toArray(new Integer[]{5, 6, 7});
//        System.out.println(Arrays.toString(intArray));

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i + "asdf");
        }
        List<String> list1 = list.subList(0, 5);
        list1.clear();
        System.out.println("end");
    }
}
/*
【注意 List 行为会根据 equals() 行为而发生变化】
当确定元素是否是属于某个 List ，
寻找某个元素的索引，
以及通过引用从 List 中删除元素时，
都会用到 equals() 方法（根类 Object 的一个方法）。
对于其他类，equals() 方法的定义可能不同，请务必注意这一点。
【在使用索引时，不必担心 equals() 的行为】
 */
