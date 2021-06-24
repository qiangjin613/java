import java.util.Iterator;
import java.util.List;

/*
在任何集合中，都必须有某种方式可以插入元素并再次获取它们。
毕竟，保存事物是集合最基本的工作。
对于 List ， add() 是插入元素的一种方式， get() 是获取元素的一种方式。

如果从更高层次的角度考虑，会发现这里有个缺点：
要使用集合，必须对集合的确切类型编程。

这一开始可能看起来不是很糟糕，但是考虑下面的情况：
如果原本是对 List 编码的，
但是后来发现如果能够将相同的代码应用于 Set 会更方便，此时应该怎么做？

或者假设想从一开始就编写一段通用代码，
它不知道或不关心它正在使用什么类型的集合，
因此它可以用于不同类型的集合，
那么如何才能不重写代码就可以应用于不同类型的集合？

【迭代器应运而生】
迭代器（也是一种设计模式）的概念实现了这种抽象。
迭代器是一个对象，它在一个序列中移动并选择该序列中的每个对象，
而客户端程序员不知道或不关心该序列的底层结构。

另外，迭代器通常被称为轻量级对象（lightweight object）：创建它的代价小。
因此，经常可以看到一些对迭代器有些奇怪的约束：
比如，Java 的 Iterator 只能单向移动。
只能用来：
1. iterator() 方法要求集合返回一个 Iterator
2. next() 方法获得序列中的下一个元素
3. hasNext() 方法检查序列中是否还有元素
4. remove() 方法将迭代器最近返回的那个元素删除
 */
class SimpleIteration {
    public static void main(String[] args) {
        List<Pet> pets = Pets.list(12);
        Iterator<Pet> it = pets.iterator();
        while (it.hasNext()) {
            Pet p = it.next();
            System.out.print(p.id() + ": " + p + " ");
        }
        System.out.println("\n");

        /* 如果只是向前遍历 List，并不打算修改 List 本身，使用 for-in 语法更加简洁 */
        for (Pet p : pets) {
            System.out.print(p.id() + ": " + p + " ");
        }
        System.out.println("\n");

        // 迭代器也可以删除元素：
        it = pets.iterator();
        for (int i = 0; i < 6; i++) {
            System.out.print(it.next() + " ");
            it.remove();
        }
        System.out.println("\n" + pets);
    }
}
/*
有了 Iterator 就不必再为集合中的元素数量
 */













