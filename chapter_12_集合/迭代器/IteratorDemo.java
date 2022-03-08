package 迭代器;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Iterator 使用示例
 *
 * Iterator 只能单向移动，可以支持 remove 操作（如果实现类实现了的话）。
 */
public class IteratorDemo {
    public static void main(String[] args) {
        // 准备一个 List
        List<String> strList = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            strList.add(i + "");
        }

        // 如果只是向前遍历 List，并不打算修改 List 本身，使用 for-each 语法更加简洁
        for (String s : strList) {
            System.out.print(s);
        }

        // 使用 Iterator 遍历 List
        Iterator<String> itr = strList.iterator();
        while (itr.hasNext()) {
            System.out.print(itr.next() + " ");
            itr.remove(); /* 删除当前元素 */
        }
        System.out.print("\n当前 strList 大小为：" + strList.size());
    }
}
/*
Note：在调用 remove() 之前必须先调用 next()，否则会抛出 java.lang.IllegalStateException 异常
 */
