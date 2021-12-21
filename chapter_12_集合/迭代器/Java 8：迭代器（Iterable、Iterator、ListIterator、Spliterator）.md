# 迭代器

迭代器通常被称为轻量级对象（lightweight object）：创建它的代价小。因此，经常可以看到一些对迭代器有些奇怪的约束。比如，Java 中的 Iterator 只能单向移动。

迭代器是一个对象，它在一个序列中移动并选择该序列中的每个对象，而客户端程序员可以不知道或不关心该序列的底层结构。可以说：迭代器统一了对集合的访问方式。

在 Java 中，集合框架的顶级接口 Collection 继承了 Iterable 接口（Iterable 接口描述了“可以产生 Iterator 的任何东西”）。代表着该框架中的所有对象都可以使用迭代器（通过 iterator()、spliterator() 获得）的形式进行遍历。此外，List 接口则进一步提供了 ListIterator 的实现。

## Iterable 接口

> Implementing this interface allows an object to be the target of the "for-each loop" statement.

就是说，任何实现了 Iterable 接口的类都可以使用 for-each 循环。如

```java
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
```

### 方法

`Iterator<T> iterator()`：返回 T 类型元素的 Iterator 迭代器。

`default void forEach(Consumer<? super T> action)`：对 Iterable 中的每个元素执行给定的 action 操作，直到所有元素都被处理或该操作引发异常。（Since：1.8）

`default Spliterator<T> spliterator()`：在 Iterable 描述的元素上创建一个 Spliterator。（Since：1.8）

### 源码

比较有趣的是 forEach 操作。通过 forEach 方法，转换为 for-each 循环遍历 Iterable 对象。

```java
public interface Iterable<T> {

    Iterator<T> iterator();

    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
```

## Iterator 接口

位于集合上的迭代器。在 Java Collections Framework 中，迭代器取代了枚举。迭代器与枚举器在两方面不同：

- 迭代器允许调用者使用定义良好的语义在迭代期间从底层集合中删除元素；
- 改进了方法名。

### 方法

`boolean hasNext()`：如果迭代中有更多的元素，则返回 true（在不抛出异常的情况下）。

`E next()`：返回迭代中的下一个元素。

`default void remove()`：从基础集合中移除该迭代器返回的最后一个元素（可选操作）。这个方法在每次调用 next() 时只能调用一次。如果在迭代过程中以除调用此方法之外的任何方式修改了基础集合，则迭代器的行为是未指定的。

`default void forEachRemaining(Consumer<? super E> action)`：对每个剩余元素执行给定的操作，直到所有元素都被处理或操作引发异常。如果指定了迭代的顺序，则按照迭代的顺序执行操作。由动作抛出的异常被转发给调用者。（Since：1.8）

### 源码

对于 remove 操作，如果派生类中不进行重写，就会抛出异常；对于 forEachRemaining 操作，被转换为 while 循环进行处理。

```java
public interface Iterator<E> {

    boolean hasNext();

    E next();

    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
```

## Spliterator 接口

用于遍历和划分源元素的对象。Spliterator覆盖的元素的源可以是数组、集合、IO通道或生成器函数。

Spliterator（splitable iterator，可分割迭代器），自从 1.8 发布后，对于并行处理的能力大大增强，Spliterator 就是为了并行遍历元素而设计的一个迭代器。

### 方法

`boolean tryAdvance(Consumer<? super T> action)`：如果剩余元素存在，则对其执行给定的操作，返回 true；否则返回 false。如果这个 Spliterator 是 ORDERED 的，则按遇到的顺序对下一个元素执行操作。由动作抛出的异常被转发给调用者。

`default void forEachRemaining(Consumer<? super T> action)`：在当前线程中依次对每个剩余元素执行给定的操作，直到所有元素都被处理或操作抛出异常。如果这个 Spliterator 是 ORDERED，操作将按照遇到的顺序执行。由动作抛出的异常被转发给调用者。

`Spliterator<T> trySplit()`：如果可以对该 spliterator 进行分区，则返回一个覆盖元素的 spliterator，该元素在从该方法返回时将不会被该 spliterator 覆盖。

`long estimateSize()`：返回 forEachRemaining() 遍历将遇到的元素数量的估计，如果是无限的、未知的或过于昂贵而无法计算，则返回 Long.MAX_VALUE。

`default long getExactSizeIfKnown()`：如果 Spliterator 为 SIZED，则返回 estimateSize() 的方便方法，否则 -1。

`int characteristics()`：返回此 Spliterator 及其元素的一组特征。

`default boolean hasCharacteristics(int characteristics)`：如果 Spliterator 的 characters() 包含所有给定的特征，则返回 true。

`default Comparator<? super T> getComparator()`：如果这个 Spliterator 的源文件被一个 Comparator 排序，则返回该 Comparator。如果源按自然顺序排序，则返回 null。否则，如果源没有被排序，则抛出 IllegalStateException。

### 源码

```java
public interface Spliterator<T> {

	boolean tryAdvance(Consumer<? super T> action);
	
	default void forEachRemaining(Consumer<? super T> action) {
        // 对剩余元素执行 action 指定操作，直到所有元素被处理或者抛出异常
        do { } while (tryAdvance(action));
    }
	
    Spliterator<T> trySplit();

	long estimateSize();

    default long getExactSizeIfKnown() {
        return (characteristics() & SIZED) == 0 ? -1L : estimateSize();
    }

    int characteristics();

    default boolean hasCharacteristics(int characteristics) {
        return (characteristics() & characteristics) == characteristics;
    }

    default Comparator<? super T> getComparator() {
        throw new IllegalStateException();
    }

	// 省略了一些静态常量（特征）和内部接口
}
```

## ListIterator 接口

ListIterator 是一种用于 List 的迭代器，允许程序员从两个方向遍历列表，在迭代期间修改列表，并获取迭代器在列表中的当前位置。ListIterator 没有当前元素，它的光标位置总是位于调用 previous() 所返回的元素和调用 next() 所返回的元素之间。长度为 n 的列表的迭代器有 n+1 个可能的游标位置：

```
    Element(0)   Element(1)   Element(2)   ... Element(n-1)
  ^            ^            ^            ^                  ^
```

注意，remove() 和 set(Object) 方法不是根据光标位置定义的，它们被定义为对调用 next() 或 previous() 返回的最后一个元素进行操作。

### 方法

- **Iterator 实现**

  `boolean hasNext()`：

  `E next()`：返回列表中的下一个元素，并向前移动光标位置。此方法可以重复调用以遍历列表，或者与对 previous() 的调用混合在一起来来回调用。(请注意，交替调用 next 和 previous 将重复返回相同的元素。)

  `void remove()`：

`boolean hasPrevious()`：当反向遍历列表时，如果该列表迭代器有更多元素，则返回 true（在不抛出异常的情况下）。

`E previous()`：返回列表中的前一个元素并向后移动光标位置。此方法可以重复调用以向后遍历列表，或者与对 next() 的调用混合在一起来来回调用。(**请注意，交替调用 next 和 previous 将重复返回相同的元素。**)

`int nextIndex()`：返回后续调用 next() 将返回的元素的索引。（如果列表迭代器位于列表的末尾，则返回列表大小。）

`int previousIndex()`：返回后续调用 previous() 将返回的元素的索引。（如果列表迭代器位于列表的开头，则返回 -1。）

`void set(E e)`：将 next() 或 previous() 返回的最后一个元素替换为指定的元素（可选操作）。这个调用只能在 remove() 和 add(E) 都没有在上一个或下一个调用之后被调用的情况下进行。**（必须先调用 next/previous 操作后，才能调用 set 操作（否则抛出异常），而替换的元素为 next/previous 返回的那个元素）**

`void add(E e)`：将指定的元素插入到列表中（可选操作）。该元素被插入到 next() 返回的元素的前面（如果有的话），以及 previous() 返回的元素的后面（如果有的话）。（如果列表不包含元素，则新元素成为列表中唯一的元素。）新元素插入到隐式游标之前，对 next 的后续调用将不受影响，对 previous 的后续调用将返回新元素。（该调用将调用 nextIndex 或 previousIndex 返回的值增加 1。)

### 源码

在 ListIterator 接口中，值得注意的是 remove 操作，将基类 Iterator 接口中的 default 方法再次重写为抽象方法（也就意味着 ListIterator 的实现类中必须提供 remove 操作的具体实现）。

```java
public interface ListIterator<E> extends Iterator<E> {
	// Query Operations
	boolean hasNext();
	E next();
	boolean hasPrevious();
    E previous();
    int nextIndex();
    int previousIndex();

    // Modification Operations
    void remove();
    void set(E e);
    void add(E e);
}
```



### 使用示例

移步：

#### 参考资料

- Java 8 API：https://docs.oracle.com/javase/8/docs/api/

- Blog：

  https://blog.csdn.net/lh513828570/article/details/56673804

  https://www.cnblogs.com/nevermorewang/p/9368431.html