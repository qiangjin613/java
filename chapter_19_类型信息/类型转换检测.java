/**
 * instanceof 的使用
 */

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Individual 度量所有对象
 */
class Individual {
    private String name;
    Individual() {}
    Individual(String name) {
        this.name = name;
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
 *
 */
class PetCount {
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
        for (Pet pet : Pets.array(20)) {
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
 * （其结果在很多方面都会更清晰）
 */
class LiteralPetCreator extends PetCreator {
    public static final List<Class<? extends Pet>> ALL_TYPES =
            Collections.unmodifiableList(Arrays.asList(
                    Pet.class, Dog.class, Mutt.class,
                    Pug.class, Cat.class, EgyptianMau.class,
                    Manx.class, Cymric.class));

    private static final List<Class<? extends Pet>> TYPES =
            ALL_TYPES.subList(ALL_TYPES.indexOf(Mutt.class),
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
 * 创建一个使用 LiteralPetCreator 的外观模式
 */
class Pets {
    public static final PetCreator CREATOR = new LiteralPetCreator();

    public static Pet get() {
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
        PetCount.countPets(Pets.CREATOR);
    }
}










