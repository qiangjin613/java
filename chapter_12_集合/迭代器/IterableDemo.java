package 迭代器;

import java.util.Iterator;

/**
 * Iterable 使用示例
 *
 * 目标：
 * 1. 继承 Iterable 接口，实现 iterator()；
 * 2. 使用 for-each。
 */
class IterableImpl implements Iterable {

    char[] number = "0123456789".toCharArray();

    @Override
    public Iterator iterator() {
        return new Iterator() {
            int i;

            @Override
            public boolean hasNext() {
                return i != number.length;
            }

            @Override
            public Object next() {
                return number[i++];
            }
        };
    }
}

class MyIterableText {
    public static void main(String[] args) {
        IterableImpl itr = new IterableImpl();
        // 使用 for-each 遍历
        for (Object o : itr) {
            System.out.println(o);
        }

        // 使用迭代器 Iterator 遍历
        Iterator iterator = itr.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
    }
}
