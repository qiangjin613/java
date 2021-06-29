import java.util.*;

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
            /*
             在调用 remove() 之前必须先调用 next()
             */
            // it.remove();
        }
        System.out.println("\n" + pets);
    }
}
/*
有了 Iterator 就不必再为集合中的元素数量操心了，这是由 hasNext() 和 next() 关心的事情。
 */

/**
 * 现在考虑创建一个 display() 方法，它不必知晓集合的确切类型：
 */
class CrossCollectionIteration {
    public static void display(Iterator<Integer> itr) {
        while (itr.hasNext()) {
            Integer p = itr.next();
            System.out.print(p + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>(16);
        Collections.addAll(ints, 1, 6, 4, 4, 2, 6, 3);
        LinkedList<Integer> intsLL = new LinkedList<>(ints);
        HashSet<Integer> intsHS = new HashSet<>(ints);
        TreeSet<Integer> intsTS = new TreeSet<>(ints);
        display(ints.iterator());
        display(intsLL.iterator());
        display(intsHS.iterator());
        display(intsTS.iterator());
    }
}
/*
display() 方法不包含任何有关它所遍历的序列的类型信息（不管是什么类型的盒子）。

展示了 Iterator 的真正威力：
    将遍历序列的操作与该序列的底层结构分离。
出于这个原因，我们有时会说：迭代器统一了对集合的访问方式。
 */


/**
 * 使用 Iterable 接口生成上一个示例的更简洁版本，
 * 描述了“可以产生 Iterator 的任何东西”：
 */
class CrossCollectionIteration2 {
    public static void display(Iterable<Integer> it) {
        Iterator<Integer> itr = it.iterator();
        while (itr.hasNext()) {
            Integer p = itr.next();
            System.out.print(p + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        List<Integer> ints = new ArrayList<>(16);
        Collections.addAll(ints, 1, 6, 4, 4, 2, 6, 3);
        LinkedList<Integer> intsLL = new LinkedList<>(ints);
        HashSet<Integer> intsHS = new HashSet<>(ints);
        TreeSet<Integer> intsTS = new TreeSet<>(ints);
        display(ints);
        display(intsLL);
        display(intsHS);
        display(intsTS);
    }
}
/*
显然，在使用上更简单。
 */



/*
【ListIterator】
ListIterator 是一个更强大的 Iterator 子类型，它只能由各种 List 类生成。

Iterator 只能向前移动，而 ListIterator 可以双向移动。
还可以生成相对于迭代器在列表中指向的当前位置的后一个和前一个元素的索引，
并且可以使用 set() 方法替换它访问过的最近一个元素，
可以通过调用 listIterator() 方法来生成指向 List 开头处的 ListIterator ，
还可以通过调用 listIterator(n) 创建一个一开始就指向列表索引号为 n 的元素处的 ListIterator。
 */
class ListIteration {
    public static void main(String[] args) {
        List<Pet> pets = Pets.list(8);
        ListIterator<Pet> it = pets.listIterator();
        while (it.hasNext()) {
            System.out.println(it.next() + ", " + it.nextIndex() + ", " + it.previousIndex() + ";");
        }
        System.out.println();

        while (it.hasPrevious()) {
            System.out.print(it.previous().id() + " ");
        }
        System.out.println();

        System.out.println(pets);
        // 从位置 3 开始替换 List 中的所有 Pet 对象：
        it = pets.listIterator(3);
        while (it.hasNext()) {
            it.next();
            it.set(Pets.get());
        }
        System.out.println(pets);
    }
}
