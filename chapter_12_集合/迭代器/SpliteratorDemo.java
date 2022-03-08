package 迭代器;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;

/**
 * Spliterator 使用示例
 */
public class SpliteratorDemo {
    public static void main(String[] args) {
        // 准备一个 List
        List<String> strList = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            strList.add(i + "");
        }

        Spliterator<String> a = strList.spliterator();
        Spliterator<String> b = a.trySplit();
        Spliterator<String> c = b.trySplit();
        /*
        分割之后的序列：
        a [50, 100);
        b [25, 50);
        c [0, 25).
         */
        a.forEachRemaining(s -> System.out.print(s + " "));
        b.forEachRemaining(s -> System.out.print(s + " "));
        c.forEachRemaining(s -> System.out.print(s + " "));
        System.out.println("end");
    }
}
