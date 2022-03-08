package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试 list 的 toCharry()
 */
public class JDKBugDemo1 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        System.out.println(list.getClass()); // class java.util.ArrayList
        System.out.println(list.toArray().getClass()); // class [Ljava.lang.Object;

        // 注意 toArray() 返回的类型
        List<String> asList = Arrays.asList("1", "2");
        System.out.println(asList.getClass()); // class java.util.Arrays$ArrayList
        System.out.println(asList.toArray().getClass()); //class [Ljava.lang.String;
        // asList.add("4"); // 异常：UnsupportedOperationException

        ArrayList<String> list1 = new ArrayList<>(asList);
        System.out.println(list1.getClass()); // class java.util.ArrayList
        System.out.println(list1.toArray().getClass()); // class [Ljava.lang.Object;
        list1.add("3"); // is OK
    }
}
/*
Arrays.asList 返回的是内部类 ArrayList，
而这个内部类的数组类型为 private final E[] a;
*/