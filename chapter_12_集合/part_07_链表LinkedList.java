import java.util.LinkedList;

/*
LinkedList 在 List 中间执行插入和删除操作时比 ArrayList 更高效。
然而,它在随机访问操作效率方面却要逊色一些。

LinkedList 还添加了一些方法，使其可以被用作栈、队列或双端队列（deque）。
在这些方法中，有些彼此之间可能只是名称有些差异，或者只存在些许差异，
以使得这些名字在特定用法的上下文环境中更加适用（特别是在 Queue 中）。
例如：
1. getFirst() 和 element() 是相同的，
    它们都返回列表的头部（第一个元素）而并不删除它，如果 List 为空，则抛出 NoSuchElementException 异常。
   peek() 方法与这两个方法只是稍有差异，它在列表为空时返回 null 。
2. removeFirst() 和 remove() 也是相同的，
    它们删除并返回列表的头部元素，并在列表为空时抛出 NoSuchElementException 异常。
   poll() 稍有差异，它在列表为空时返回 null 。
3. addFirst() 在列表的开头插入一个元素。
4. offer() 与 add() 和 addLast() 相同。 它们都在列表的尾部（末尾）添加一个元素。
5. removeLast() 删除并返回列表的最后一个元素。
 */
class LinkedListFeatures {
    public static void main(String[] args) {
        LinkedList<Pet> pets = new LinkedList<>(Pets.list(5));
        System.out.println(pets);

        // 获取元素 - 完全相同的：为空时抛出异常
        System.out.println("getFirst() 和 element()：");
        System.out.println(pets.getFirst() + " " + pets.element());
        // 获取元素 - 仅在空列表行为上有所不同：为空时返回 null
        System.out.println("peek()：");
        System.out.println(pets.peek());

        // 删除元素 - 完全相同的：为空时抛出异常
        System.out.println("removeFirst() 和 remove()：");
        System.out.println(pets.removeFirst() + " " + pets.remove());
        // 删除元素 - 仅在空列表行为上有所不同：为空时返回 null
        System.out.println("pool()：");
        System.out.println(pets.poll());

        // 添加元素：可能是个 BUG，不管什么都返回 true
        pets.add(new Dog());
        pets.offer(new Dog());
        pets.addLast(new Dog());
    }
}
/*
Queue 接口在 LinkedList 的基础上添加了 element()，offer()，peek()，poll() 和 remove() 方法，
以使其可以成为一个 Queue 的实现。
 */
