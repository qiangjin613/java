/*
【类型转换检测：instanceof】
直到现在，我们已知的 RTTI 类型包括：
    1.传统的类型转换
    如 “(Shape)”，由 RTTI 确保转换的正确性，如果执行了一个错误的类型转换，
    就会抛出一个 ClassCastException 异常。
    2.代表对象类型的 Class 对象
    通过查询 Class 对象可以获取运行时所需的信息.

RTTI 在 Java 中还有第三种形式，那就是关键字 instanceof。
    if (x instanceof Dog)
        Dog d = (Dog) x;
instanceof 有一个严格的限制：只可以将它与命名类型进行比较，而不能与 Class 对象作比较。

【一个动态 instanceof 函数】
classObj.isInstance() 方法提供了一种动态测试对象类型的方法。
因此，所有这些繁琐的 instanceof 语句都可以使用 Class.isInstance() 进行替换。
classObj.isInstance(Obj)
native boolean isInstance(Object obj)

【还有一个 instanceof 方法】
classObj.isAssignableFrom(otherClassObj)
native boolean isAssignableFrom(Class<?> cls)
 */

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Individual 度量所有对象
 */
class Individual {
    private String name;
    private int id;
    Individual() {}
    Individual(String name) {
        this.name = name;
    }

    public int id() {
        return id;
    }
    @Override
    public String toString() {
        return "Individual{" + "className='" + getClass().getSimpleName() + '\'';
    }
}
/**
 * 人
 */
class Person extends Individual {
    public Person(String name) {
        super(name);
    }
}
/**
 * 宠物
 */
class Pet extends Individual {
    public Pet() {
        super();
    }
    public Pet(String name) {
        super(name);
    }
}
class Dog extends Pet {
    public Dog() {
        super();
    }
    public Dog(String name) {
        super(name);
    }
}
class Mutt extends Dog {
    public Mutt() {
        super();
    }
    public Mutt(String name) {
        super(name);
    }
}
class Pug extends Dog {
    public Pug() {
        super();
    }
    public Pug(String name) {
        super(name);
    }
}
class Cat extends Pet {
    public Cat() {
        super();
    }
    public Cat(String name) {
        super(name);
    }
}
class EgyptianMau extends Cat {
    public EgyptianMau() {
        super();
    }
    public EgyptianMau(String name) {
        super(name);
    }
}
class Manx extends Cat {
    public Manx() {
        super();
    }
    public Manx(String name) {
        super(name);
    }
}
class Cymric extends Manx {
    public Cymric() {
        super();
    }
    public Cymric(String name) {
        super(name);
    }
}
/**
 * 一个随机创建不同类型的宠物的类，同时，还可以创建 宠物数组 和 宠物List
 * （为了使这个类更加普遍适用，将其定义为抽象类）
 * 数组和List为 types() 服务，而 types() 则为 get() 服务
 */
abstract class PetCreator implements Supplier<Pet> {
    private Random rand = new Random(47);
    // 要创建的不同类型的宠物列表：
    public abstract List<Class<? extends Pet>> types();
    // 随机创建一个宠物
    @Override
    public Pet get() {
        int n = rand.nextInt(types().size());
        try {
            return types().get(n).newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
/**
 * 创建 PetCreator 的子类
 */
class ForNameCreator extends PetCreator {
    private static List<Class<? extends Pet>> types = new ArrayList<>();
    private static String[] typeName = {
            "Mutt","Pug","EgyptianMau","Manx","Cymric"
    };

    static {
        loader();
    }

    // 将 loader() 方法抽出来，
    // 是因为 @SuppressWarnings 注解不能够直接放置在静态代码块里
    @SuppressWarnings("unchecked")
    private static void loader() {
        try {
            for (String name : typeName) {
                types.add((Class<? extends Pet>) Class.forName(name));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Class<? extends Pet>> types() {
        return types;
    }
}

/**
 * 关键字 instanceof 的使用：对 Pet 的实际对象进行计数
 */
class PetCount {
    // 根据名称进行计数 <类名, 数量>
    static class Counter extends HashMap<String, Integer> {
        public void count(String type) {
            Integer quantity = get(type);
            if (quantity == null) {
                put(type, 1);
            } else {
                put(type, quantity + 1);
            }
        }
    }

    public static void countPets(PetCreator creator) {
        Counter counter = new Counter();
        for (int i = 0; i < 20; i++) {
            // 使用 PetCretor 获取对象，
            Pet pet = creator.get();
            System.out.print(pet.getClass().getSimpleName() + " ");
            if (pet instanceof Pet) {
                counter.count("Pet");
            }
            if (pet instanceof Dog) {
                counter.count("Dog");
            }
            if (pet instanceof Mutt) {
                counter.count("Mutt");
            }
            if (pet instanceof Pug) {
                counter.count("Pug");
            }
            if (pet instanceof Cat) {
                counter.count("Cat");
            }
            if (pet instanceof EgyptianMau) {
                counter.count("EgyptianMau");
            }
            if (pet instanceof Manx) {
                counter.count("Manx");
            }
            if (pet instanceof Cymric) {
                counter.count("Cymric");
            }
        }
        // 显示数量：
        System.out.println();
        System.out.println(counter);
    }

    public static void main(String[] args) {
        countPets(new ForNameCreator());
    }
}


/**
 * 【使用类字面常量】
 * 使用类字面量重新实现 PetCreator 类
 * （其结果在很多方面都会更清晰，拥有使用类字面常量的诸多好处）
 */
class LiteralPetCreator extends PetCreator {
    public static final List<Class<? extends Pet>> ALL_TYPES =
            Collections.unmodifiableList(Arrays.asList(
                    Pet.class, Dog.class, Mutt.class,
                    Pug.class, Cat.class, EgyptianMau.class,
                    Manx.class, Cymric.class));

    private static final List<Class<? extends Pet>> TYPES =
            ALL_TYPES.subList(ALL_TYPES.indexOf(EgyptianMau.class),
                    ALL_TYPES.size());

    @Override
    public List<Class<? extends Pet>> types() {
        return TYPES;
    }

    public static void main(String[] args) {
        System.out.println(TYPES);
    }
}
/**
 * 创建一个使用 LiteralPetCreator 的外观模式，用于创建多个 Pet 对象，比较时依旧使用 instanceof
 */
class Pets {
    public static final PetCreator CREATOR = new LiteralPetCreator();

    public static Pet get() {
        System.out.println("使用 Pets.get()");
        return CREATOR.get();
    }

    public static Pet[] array(int size) {
        Pet[] result = new Pet[size];
        for (int i = 0; i < size; i++) {
            result[i] = CREATOR.get();
        }
        return result;
    }
    public static List<Pet> list(int size) {
        List<Pet> result = new ArrayList<>(size);
        Collections.addAll(result, array(size));
        return result;
    }
    public static Stream<Pet> stream() {
        return Stream.generate(CREATOR);
    }
}

/**
 * 通过外观模式测试 LiteralPetCreator
 */
class PetCount2 {
    public static void main(String[] args) {
        // 生成的 Pet 对象将是 Pets 的 get() 生成的
        // 使用了多态
        PetCount.countPets(Pets.CREATOR);
    }
}


/**
 * 【一个动态 instanceof 函数】
 * Class.isInstance() 方法提供了一种动态测试对象类型的方法。
 * （所有繁琐的 instnceof 语句都可以使用 类字面常量.isInstance() 替换）
 */
class PetCount3 {
    static class Counter extends
            LinkedHashMap<Class<? extends Pet>, Integer> {
        Counter() {
            // 预先加载所有不同的 Pet 类
            super(load());
        }

        public static Map<Class<? extends Pet>, Integer> load(){
            Map<Class<? extends Pet>, Integer> map = new LinkedHashMap<>();
            for (Class<? extends Pet> type : LiteralPetCreator.ALL_TYPES) {
                map.put(type, 0);
            }
            return map;
        }

        public void count(Pet pet) {
            entrySet().stream()
                    .filter(pair -> pair.getKey().isInstance(pet))
                    .forEach(pair -> put(pair.getKey(), pair.getValue() + 1));
        }

        @Override
        public String toString() {
            String result = entrySet().stream()
                    .map(pair -> String.format("%s=%s",
                            pair.getKey().getSimpleName(),
                            pair.getValue()))
                    .collect(Collectors.joining(", "));
            return "{" + result + "}";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter petCount = new Counter();
        Pets.stream()
                .limit(20)
                .peek(petCount::count)
                .forEach(p -> System.out.print(p.getClass().getSimpleName() + " "));
        System.out.println("n" + petCount);
     }
}


/**
 * 【递归计数】
 * 上述代码预先加载了所有不同的 Pet 类。
 * 现在使用 Class.isAssignableFrom() 创建一个不限于计数 Pet 的通用工具。
 */
class TypeCounter extends HashMap<Class<?>, Integer> {
    private Class<?> baseType;
    public TypeCounter(Class<?> baseType) {
        this.baseType = baseType;
    }
    public void count(Object obj) {
        Class<?> type = obj.getClass();
        if (!baseType.isAssignableFrom(type)) {
            throw new RuntimeException(
                    obj + " incorrect type: " + type +
                    ", should be type or subtype of " + baseType);
        }
        countClass(type);
    }
    private void countClass(Class<?> type) {
        Integer quantity = get(type);
        put(type, quantity == null ? 1 : quantity + 1);
        Class<?> superClass = type.getSuperclass();
        if (superClass != null && baseType.isAssignableFrom(superClass)) {
            countClass(superClass);
        }
    }

    @Override
    public String toString() {
        String result = entrySet().stream()
                .map(pair -> String.format("%s=%s",
                        pair.getKey().getSimpleName(),
                        pair.getValue()))
                .collect(Collectors.joining(", "));
        return "{" + result + "}";
    }
}
class PetCount4 {
    public static void main(String[] args) {
        TypeCounter counter = new TypeCounter(Pet.class);
        Pets.stream()
                .limit(10)
                .peek(counter::count)
                .forEach(p -> System.out.print(p.getClass().getSimpleName() + " "));
        System.out.println("\nn" + counter);
    }
}
/*
A.isAssignableFrom(B)
确定一个类(B)是不是继承来自于另一个父类(A)，一个接口(A)是不是实现了另外一个接口(B)，或者两个类相同。
主要，这里比较的维度不是实例对象，而是类本身，因为这个方法本身就是Class类的方法，判断的肯定是和类信息相关的。

也就是说，A 是 B 的基类、父接口时返回 true，否则返回 false。
 */
