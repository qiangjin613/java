/*
在 java.util 包中的 Arrays 和 Collections 类中都有很多实用的方法，
可以在一个 Collection 中添加一组元素。

1. Arrays.asList()
2. Collections.addAll()，更快（首选）
3. 集合.addAll()，只能接受另一个 Collection 作为参数，没有其他两个灵活
 */

import java.util.*;

class AddingGroups {
    public static void main(String[] args) {
        // Collection 的构造器可以接受另一个 Collection，用它来将自身初始化
        Collection<Integer> collection = new ArrayList<>(
                Arrays.asList(1, 2, 3, 4)
        );

        Integer[] moreInts = {5, 6, 7, 8};
        // Collection.addAll() 方法只能接受另一个 Collection 作为参数
        collection.addAll(Arrays.asList(moreInts));

        Collections.addAll(collection, 11, 212, 13);
        Collections.addAll(collection, moreInts);

        /*
        因为该集合的底层是数组，所以在使用时会发生潜在的下标越界。
        产生数组越绝异常：java.lang.ArrayIndexOutOfBoundsException
        */
        List<Integer> list = Arrays.asList(189, 20);
        list.set(1, 88);
        list.set(100, 99);

        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.set(29, 44);
        /**
         * 可见，与集合的来源是不是数组无关。
         */
    }
}


/**
 * 如果 Collection 的来源是数组的话，
 * 尝试在这个 List 上调用 add() 或 remove()，
 * 由于这两个方法会尝试修改数组大小，
 * 所以会在运行时得到“Unsupported Operation（不支持的操作）”错误：
 */
class Snow {}

class Crusty extends Snow {}
class Slush extends Snow {}
class Power extends Snow {}

class Light extends Power {}
class Heavy extends Power {}

class AsListInference {
    public static void main(String[] args) {
        List<Snow> snow1 = Arrays.asList(
                new Crusty(), new Slush(), new Power(), new Light()
        );
        /* 异常：UnsupportedOperationException */
        // snow1.add(new Crusty());

        List<Snow> snow2 = Arrays.<Snow>asList(
                new Crusty(), new Slush(), new Power(), new Light()
        );
        /* 异常：UnsupportedOperationException */
        Collections.addAll(snow2, new Crusty(), new Slush());

        /*
        显示类型参数说明（explicit type argument specification），
        告诉编译器 Arrays.asList() 生成的结果 List 类型的实际目标类型是什么
         */
        List<Snow> snow3 = Arrays.<Snow>asList(
                new Crusty(), new Slush(), new Power(), new Light()
        );


        List<Snow> snow4 = new ArrayList<>();
        Collections.addAll(snow4, new Light(), new Heavy());
        snow4.add(new Crusty());
        /* 等同于上面的语句 */
        Collections.addAll(snow4, new Snow[] {new Crusty(), new Slush(), new Power()});
        snow4.add(new Crusty());
        System.out.println(snow4);
    }
}
/*
可以看出，不管是使用 Collections.addAll() 还是 collection.addAll()/collection.add()，
只要其本质是数组，就会发生 UnsupportedOperationException 异常。
 */
