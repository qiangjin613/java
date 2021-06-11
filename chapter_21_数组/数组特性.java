/*
随着 Java Collection 和 Stream 类中高级功能的不断增加，
日常编程中使用数组的需求也在变少.

但有时候你必须在数组上进行更复杂的操作，
也可能需要在数组和更加灵活的 集合 (Collection)之间做出评估。

因此，接下来对数组进行更加深入的分析。
 */

/*
将数组和其他类型的集合区分开来的原因有三：
1. 效率
2. 类型
3. 保存基本数据类型的能力
*/


/*
【效率】
在 Java 中，使用数组存储和随机访问对象引用序列是非常高效的。
数组是简单的线性序列，这使得对元素的访问变得非常快。
然而这种高速也是有代价的，
代价就是数组对象的大小是固定的，且在该数组的生存期内不能更改。

说集合 约等于 数组：
如 ArrayList，它将数组封装起来。
必要时，它会自动分配更多的数组空间，创建新数组，并将旧数组中的引用移动到新数组。
这种灵活性需要开销。
所以，ArrayList 的效率不如数组。在极少数情况下称为效率问题的时候，可以直接使用数组。
 */


/*
【类型】
在泛型前，其他的集合类以一种宽泛的方式处理对象（就好像它们没有特定类型一样）。
事实上，这些集合类把保存对象的类型默认为 Object，也就是 Java 中所有类的基类。

而数组是优于 预泛型 (pre-generic)集合类的，
因为你创建一个数组就可以保存特定类型的数据。
这意味着你获得了一个编译时的类型检查，而这可以防止你插入错误的数据类型，
或者搞错你正在提取的数据类型。
 */


/*
【保存基本数据类型的能力】
针对于保存基本数据类型：
一个数组可以保存基本数据类型，而一个预泛型的集合不可以。

然而对于泛型而言，集合可以指定和检查他们保存对象的类型，
而通过 自动装箱 (autoboxing)机制，集合表现地就像它们可以保存基本数据类型一样。
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

/**
 * 一个小例子：
 */
class BerylliumSphere {
    private static long counter;
    private final long id = counter++;

    @Override
    public String toString() {
        return "Sphere " + id;
    }
}

class CollectionComparison {
    public static void main(String[] args) {
        // 类型
        /* 数组 */
        BerylliumSphere[] spheres = new BerylliumSphere[10];
        for (int i = 0; i < 5; i++) {
            spheres[i] = new BerylliumSphere();
        }
        show(spheres);
        System.out.println(spheres[3]);

        /* 泛型集合 */
        List<BerylliumSphere> sphereList = new ArrayList<>(10);
        sphereList.add(new BerylliumSphere());
        sphereList.add(new BerylliumSphere());
        sphereList.add(new BerylliumSphere());
        sphereList.add(new BerylliumSphere());
        sphereList.add(new BerylliumSphere());
        System.out.println(sphereList);
        System.out.println(sphereList.get(3));

        // 基本类型
        /* 数组 */
        int[] ints = {0, 1, 2, 3, 4, 5};
        System.out.println(Arrays.toString(ints));

        /* 泛型集合 */
        List<Integer> intList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
        intList.add(99);
        System.out.println(intList);
    }

    public static void show(Object[] o) {
        Formatter f = new Formatter(new StringBuilder());
        for (Object o1 : o) {
            f.format("%s, ", o1);
        }
        System.out.println(f.toString().substring(0, f.toString().length() - 2));
    }
}

/*
对于数组、泛型集合保存对象的方式都是有类型检查的，
唯一比较明显的区别就是数组使用 [ ] 来随机存取元素，
而一个 List 使用诸如 add() 和 get() 等方法。

数组和 ArrayList 之间的相似是设计者有意为之，所以在概念上，两者很容易切换。
在集合中，集合的功能明显多于数组。
随着 Java 自动装箱技术的出现，
通过集合使用基本数据类型几乎和通过数组一样简单。

数组唯一剩下的优势就是效率。
 */


/*
【一个打印数组的实用程序】
就是使用 Arrays.toString() 方法
 */
interface ArrayShow {
    static void show(Object[] o) {
        System.out.println(Arrays.toString(o));
    }
    static void show(boolean[] a) {
        System.out.println(Arrays.toString(a));
    }
    static void show(char[] a) {
        System.out.println(Arrays.toString(a));
    }
    static void show(short[] a) {
        System.out.println(Arrays.toString(a));
    }
    static void show(int[] a) {
        System.out.println(Arrays.toString(a));
    }
    static void show(long[] a) {
        System.out.println(Arrays.toString(a));
    }
    static void show(float[] a) {
        System.out.println(Arrays.toString(a));
    }
    static void show(double[] a) {
        System.out.println(Arrays.toString(a));
    }
}
