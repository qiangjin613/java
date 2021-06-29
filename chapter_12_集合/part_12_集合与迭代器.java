import java.util.*;

/*
Collection 是所有序列集合共有的根接口。
它可能会被认为是一种“附属接口”（incidental interface），
即因为要表示其他若干个接口的共性而出现的接口。

此外，java.util.AbstractCollection 类提供了 Collection 的默认实现，
使得你可以创建 AbstractCollection 的子类型，而其中没有不必要的代码重复。

使用接口描述的一个理由是它可以使我们创建更通用的代码。
通过针对接口而非具体实现来编写代码，我们的代码可以应用于更多类型的对象。
因此，如果所编写的方法接受一个 Collection，
那么该方法可以应用于任何实现了 Collection 的类，
这也就使得一个新类可以选择去实现 Collection 接口，以便该方法可以使用它。

标准 C++ 类库中的集合并没有共同的基类，集合之间的所有共性都是通过迭代器实现的。
在 Java 中，遵循 C++ 的方式看起来似乎很明智，
即用迭代器而不是 Collection 来表示集合之间的共性。

但是，这两种方法绑定在了一起，
因为实现 Collection 就意味着需要提供 iterator() 方法。
（interface Collection<E> extends Iterable<E>）
 */

class InterfaceVsIterator {
    public static void display(Iterator<Pet> it) {
        while (it.hasNext()) {
            Pet p = it.next();
            System.out.println(p.id() + ": " + p + " ");
        }
        System.out.println();
    }
    public static void display(Collection<Pet> pets) {
        for (Pet p : pets) {
            System.out.println(p.id() + ": " + p + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        List<Pet> petList = Pets.list(8);
        HashSet<Pet> petSet = new HashSet<>(petList);
        display(petList);
        display(petSet);

        display(petList.iterator());
        display(petSet.iterator());


        Map<String, Pet> petMap = new LinkedHashMap<>();
        String[] names = ("Ralph, Eric, Robin, Lacey, " +
                "Britney, Sam, Spot, Fluffy").split(", ");
        for (int i = 0; i < names.length; i++) {
            petMap.put(names[i], petList.get(i));
        }
        System.out.println(petMap);
        System.out.println(petMap.keySet());
        display(petMap.values());

        display(petMap.values().iterator());
    }
}
/*
两个版本的 display() 方法都可以使用 Map 或 Collection 的子类型来工作。
而且Collection 接口和 Iterator 都将 display() 方法与低层集合的特定实现解耦。

在本例中，这两种方式都可以奏效。
事实上， Collection 要更方便一点，
因为它是 Iterable 类型，因此在 display(Collection) 的实现中可以使用 for-in 构造，
这使得代码更加清晰。
 */
















