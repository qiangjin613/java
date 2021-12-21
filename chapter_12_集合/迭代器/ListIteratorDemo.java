package 迭代器;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * ListIterator 使用示例
 *
 * ListIterator 可以双向移动。
 * 还可以生成相对于迭代器在列表中指向的当前位置的后一个和前一个元素的索引，
 * 并且可以使用 set() 方法替换它访问过的最近一个元素，
 * 可以通过调用 listIterator() 方法来生成指向 List 开头处的 ListIterator ，
 * 还可以通过调用 listIterator(n) 创建一个一开始就指向列表索引号为 n 的元素处的 ListIterator。
 */
public class ListIteratorDemo {
    public static void main(String[] args) {
        // 准备一个 List
        List<String> strList = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            strList.add(i + "");
        }

        // 获取 ListIterator
        ListIterator<String> listItr = strList.listIterator();
        // 遍历 list
        while (listItr.hasNext()) {
            System.out.println(listItr.next() + ", " + listItr.nextIndex() + ", " + listItr.previousIndex());
        }

        // 从 指定索引开始替换 list 中的所有元素
        listItr = strList.listIterator(3);
        while (listItr.hasNext()) {
            System.out.println(listItr.next());
            listItr.set("**");
        }
        System.out.println(strList);

        // 给 list 中添加元素：以下操作在 索引 == 2 的位置上添加一个元素
        listItr = strList.listIterator(2);
        listItr.add("##");
        listItr.add("###");
        listItr.add("####");
        System.out.println(listItr.next()); // 返回 元素 ”2“
        /*
        如果是 listItr.previous() 则返回的是新元素 ”####“
         */
    }
}
/*
Note:
1. 对于 set()：必须先调用 next/previous 操作后，才能调用 set 操作（否则抛出异常），而替换的元素为 next/previous 返回的那个元素；
2. 对于 add()：没有 set 中的限制。该元素被插入到 next() 返回的元素的前面，以及 previous() 返回的元素的后面（如果有的话），
    对 next 的后续调用将不受影响，对 previous 的后续调用将返回新元素。
 */
