/*
一般来说，数组和泛型并不能很好的结合。

不能实例化 参数化类型的数组：
Peel<Banana>[] peels = new Peel<Banana>[10]; // Illegal
因为类型擦除需要删除参数类型信息，
而且数组必须知道它们所保存的确切类型，以强制保证类型安全。（矛盾点）

但是，有几个例外：
1. 可以参数化数组本身的类型
2. 允许创建泛型类型的数组的引用
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [1] 参数化数组本身的类型：
 *  实则是通过泛型进行转化，从而确认不确定的类型，再传给数组
 */
class ClassParameter<T> {
    public T[] f(T[] arg) {
        return arg;
    }
}
class MethodParameter {
    public static <T> T[] f(T[] arg) {
        return arg;
    }
}
class ParameterizedArrayType {
    public static void main(String[] args) {
        Integer[] ints = {1, 2, 3, 4, 5};
        Double[] doubles = {1.1, 2.2, 3.3, 4.4, 5.5};

        Integer[] ints2 = new ClassParameter<Integer>().f(ints);
        Double[] doubles2 = new ClassParameter<Double>().f(doubles);

        ints2 = MethodParameter.f(ints);
        doubles2 = MethodParameter.f(doubles);
    }
}
/*
Tip:
1. 比起使用参数化类，使用参数化方法（如，MethodParameter.f()）很方便。
    不必为应用它的每个不同类型都实例化一个带有参数的类。
    虽然不能总是选择使用参数化方法而不用参数化的类，但通常参数化方法是更好的选择。
 */


/**
 * "不能创建泛型类型的数组" 这种说法并不完全正确。
 *
 * 是的，编译器不会让你 实例化 一个泛型的数组。
 * 但是，它将允许你创建对此类数组的引用。
 *
 * [2] 创建泛型类型的数组的引用
 */
class ArrayOfGenerics {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        /* 创建泛型类型的数组的引用 */
        List<String>[] ls;

        /* 可以创建一个非泛型的数组并对其进行强制类型转换 */
        List[] la = new List[10];
        ls = (List<String>[]) la;

        /* 使用“泛型数组” */
        ls[0] = new ArrayList<>();
        ls[1] = new ArrayList<String>();
        // Error: 不兼容的类型: java.util.ArrayList<java.lang.Integer>无法转换为java.util.List<java.lang.String>
        // ls[1] = new ArrayList<Integer>();


        /* 数组是协变的，所以 List[] 也是一个 Object[] */
        /* List<String> 是 Object 的子类型，当然可以进行转换 */
        Object[] objects = ls;
        /* 编译和运行没有任何抱怨 */
        objects[1] = new ArrayList<Integer>();
        System.out.println(Arrays.toString(objects));


        /* 如果需求简单，则可以创建泛型数组，尽管带有“unchecked cast”警告 */
        List<BerylliumSphere>[] spheres = (List<BerylliumSphere>[]) new List[10];
        Arrays.setAll(spheres, n -> new ArrayList<>());
        System.out.println(Arrays.toString(spheres));
    }
}
/*
Tip:
如果你知道你不会进行向上类型转换，并且你的需求相对简单。
那么可以创建一个泛型数组，它将提供基本的编译时类型检查。
然而，一个泛型 Collection 实际上是一个比泛型数组更好的选择。
 */


/**
 * 不能创建泛型类型的数组的情况：
 */
class ArrayOfGenericType<T> {
    T[] array;

    @SuppressWarnings("unchecked")
    public ArrayOfGenericType(int size) {
        /* 类型参数'T'不能直接实例化 */
        // array = new T[size];

        /* 开辟Object的空间，存放在 T 类型为名的房间里 */
        array = (T[]) new Object[size];
    }


    public static void main(String[] args) {
        ArrayOfGenericType<Integer> a = new ArrayOfGenericType<>(10);

        /* 尽管 array 的房间为 T，但数组是协变的，所以可以转为 Object[]，从而肆无忌惮的存放其他类型信息 */
        Object[] s = a.array;
        s[0] = "000";
        s[1] = new Z("zz", "20");

        System.out.println(Arrays.toString(a.array));
    }
}
/*
Tip:
1. 可以理解，泛型是编译器的规矩。
    具体存什么，还是要看 new 的时候开辟的是什么类型的空间。
 */
