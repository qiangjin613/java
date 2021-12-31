/*
队列是一个典型的“先进先出”（FIFO）集合。
队列通常被当做一种可靠的将对象从程序的某个区域传输到另一个区域的途径。

队列在并发编程中尤为重要，
因为它们可以安全地将对象从一个任务传输到另一个任务。
 */

import java.lang.reflect.Method;
import java.util.*;

/**
 * LinkedList 实现了 Queue 接口，并且提供了一些方法以支持队列行为，
 * 因此 LinkedList 可以用作 Queue 的一种实现。
 * 如：
 */
class QueueDemo {
    public static void printQ(Queue queue) {
        while (queue.peek() != null) {
            System.out.print(queue.remove() + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        Random rand = new Random(47);
        for (int i = 0; i < 10; i++) {
            queue.offer(rand.nextInt(i + 10));
        }
        printQ(queue);

        Queue<Character> qc = new LinkedList<>();
        for (char c : "dsfsdfsdfsd".toCharArray()) {
            qc.offer(c);
        }
        printQ(qc);
    }
}


/*
【优先级队列PriorityQueue】
先进先出（FIFO）描述了最典型的队列规则（queuing discipline）。

队列规则是指在给定队列中的一组元素的情况下，
确定下一个弹出队列的元素的规则。
先进先出声明的是下一个弹出的元素应该是等待时间最长的元素。

优先级队列声明下一个弹出的元素是最需要的元素（具有最高的优先级）。

默认的排序使用队列中对象的自然顺序（natural order），
但是可以通过提供自己的 Comparator 来修改这个顺序。

在Java 5 中添加了 PriorityQueue ，以便自动实现这种行为。
PriorityQueue 确保在调用 peek() ， poll() 或 remove() 方法时，
获得的元素将是队列中优先级最高的元素。
 */
class PriorityQueueDemo {
    public static void main(String[] args) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        Random rand = new Random(47);
        for (int i = 0; i < 10; i++) {
            priorityQueue.offer(rand.nextInt(i + 10));
        }
        QueueDemo.printQ(priorityQueue);

        List<Integer> ints = Arrays.asList(25, 22, 20,
                18, 14, 9, 3, 1, 1, 2, 3, 9, 14, 18, 21, 23, 25);
        priorityQueue = new PriorityQueue<>(ints);
        QueueDemo.printQ(priorityQueue);
        priorityQueue = new PriorityQueue<>(ints.size(), Collections.reverseOrder());
        priorityQueue.addAll(ints);
        QueueDemo.printQ(priorityQueue);

        String fact = "EDUCATION SHOULD ESCHEW OBFUSCATION";
        List<String> strings = Arrays.asList(fact.split(""));
        PriorityQueue<String> stringPQ = new PriorityQueue<>(strings);
        QueueDemo.printQ(stringPQ);
        stringPQ = new PriorityQueue<>(strings.size(), Collections.reverseOrder());
        stringPQ.addAll(strings);
        QueueDemo.printQ(stringPQ);

        /* 添加一个 HashSet 来消除重复的 Character */
        Set<Character> charSet = new HashSet<>();
        for(char c : fact.toCharArray()) {
            charSet.add(c); // Autoboxing
        }
        PriorityQueue<Character> characterPQ = new PriorityQueue<>(charSet);
        QueueDemo.printQ(characterPQ);
    }
}
/*
PriorityQueue 与 内置类型（Integer、String 和 Character 等）一起工作易如反掌。
因为这些类已经内置了自然排序。

如果想在 PriorityQueue 中使用自己的类，
则必须包含额外的功能以产生自然排序，
或者必须提供自己的 Comparator 。
 */

/**
 * 与获取接口不同，方法可以获取到继承了基类的方法和仅获取当前类中的方法：
 * class.getMethods(): 获取当前类所有的方法；
 * class.getDeclaredMethods(): 仅获取当前类中的方法（不包括继承基类和接口中的方法）。
 */
class TestQueue {
    public static void main(String[] args) {
        Class<Queue> queueClass = Queue.class;
        for (Method method : queueClass.getMethods()) {
            System.out.println(method);
        }

        for (Method method : queueClass.getDeclaredMethods()) {
            System.out.println(method);
        }
    }
}
