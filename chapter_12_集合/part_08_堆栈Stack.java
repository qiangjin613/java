import java.util.ArrayDeque;
import java.util.Deque;

/*
堆栈是“后进先出”（LIFO）集合。
它有时被称为叠加栈（pushdown stack），因为最后“压入”（push）栈的元素，第一个被“弹出”（pop）栈。

Java 1.0 中附带了一个 Stack 类，结果设计得很糟糕。
Java 6 添加了 ArrayDeque ，其中包含直接实现堆栈功能的方法：
 */
class StackTest {
    public static void main(String[] args) {
        Deque<String> stack = new ArrayDeque<>(16);
        for (String s : "My dog has fleas".split(" ")) {
            stack.push(s);
        }
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
    }
}

/**
 * 即使它是作为一个堆栈在使用，我们仍然必须将其声明为 Deque。
 * 有时一个名为 Stack 的类更能把事情讲清楚：
 */
class Stack<T> {
    private Deque<T> storage = new ArrayDeque<>();

    public void push(T v) {
        storage.push(v);
    }
    /* peek() 方法将返回栈顶元素，但并不将其从栈顶删除 */
    public T peek() {
        return storage.peek();
    }
    /* pop() 删除并返回顶部元素 */
    public T pop() {
        return storage.pop();
    }
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}
/*
如果只需要栈的行为，那么使用继承是不合适的，
因为这将产生一个具有 ArrayDeque 的其它所有方法的类
（ Java 1.0 设计者在创建 java.util.Stack 时，就犯了这个错误）

使用组合，可以选择要公开的方法以及如何命名它们。

尽管已经有了 java.util.Stack ，但是 ArrayDeque 可以产生更好的 Stack ，因此更可取。
 */

/**
 * 演示这个新的 Stack 类：
 */
class StackTest2 {
    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        for (String s : "My dog has fleas".split(" ")) {
            stack.push(s);
        }
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
    }
}
