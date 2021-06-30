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

/**
 * 当需要实现一个不是 Collection 的外部类时，
 * 由于让它去实现 Collection 接口可能非常困难或麻烦，
 * 因此使用 Iterator 就会变得非常吸引人。
 *
 * 例如，如果我们通过继承一个持有 Pet 对象的类来创建一个 Collection 的实现，
 * 那么我们必须实现 Collection 所有的方法，
 * 即使我们不在 display() 方法中使用它们，也必须这样做。
 * 虽然这可以通过继承 AbstractCollection 而很容易地实现，
 * 但是无论如何还是要被强制去实现 iterator() 和 size() 方法，
 * 这些方法 AbstractCollection 没有实现，
 * 但是 AbstractCollection 中的其它方法会用到：
 * （下面继承一个 AbstractCollection，并实现 iterator() 和 size() 方法）
 */
class CollectionSequence extends AbstractCollection<Pet>{
    private Pet[] pets = Pets.array(8);

    @Override
    public Iterator<Pet> iterator() {
        return new Iterator<Pet>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < pets.length;
            }

            @Override
            public Pet next() {
                return pets[index++];
            }

            // remove() 方法是一个“可选操作”，这里可以不必实现它，如果你调用它，它将抛出异常。
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int size() {
        return pets.length;
    }

    public static void main(String[] args) {
        CollectionSequence c = new CollectionSequence();
        InterfaceVsIterator.display(c);
        InterfaceVsIterator.display(c.iterator());
    }
}
/*
这个例子表明，如果实现了 Collection ，就必须实现 iterator()，
并且拿只实现 iterator() 与继承 AbstractCollection 相比，花费的代价只有略微减少。

但是，如果类已经继承了其他的类，那么就不能继承再 AbstractCollection 了。
在这种情况下，要实现 Collection ，就必须实现该接口中的所有方法（是比较麻烦的）。
此时，继承并且提供创建迭代器的能力要比实现 Collection 容易得多：
 */
class PetSequence {
    protected Pet[] pets = Pets.array(8);
}
// 一个不是 Collection 的类继承了其他类，这时想要提供迭代器可以使用如下方法：
class NonCollectionSequence extends PetSequence {
    public Iterator<Pet> iterator() {
        return new Iterator<Pet>() {
            private int index = 0;
            @Override
            public boolean hasNext() {
                return index < pets.length;
            }

            @Override
            public Pet next() {
                return pets[index++];
            }
        };
    }

    public static void main(String[] args) {
        NonCollectionSequence nc = new NonCollectionSequence();
        InterfaceVsIterator.display(nc.iterator());
    }
}
/*
生成 Iterator 是将序列与消费该序列的方法连接在一起耦合度最小的方式，
并且与实现 Collection 相比，它在序列类上所施加的约束也少得多。
 */
