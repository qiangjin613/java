/*
无法从 enum 继承子类有时很令人沮丧。
继承 enum 的需求
有时源自我们希望扩展原 enum 中的元素，
有时是因为我们希望使用子类将一个 enum 中的元素进行分组。

Q：如何实现将枚举分组？
A：在一个接口的内部，
    创建实现该接口的枚举，以此将元素进行分组，
    可以达到将枚举元素分类组织的目的。
 */
interface Food {
    enum Appetizer implements Food {
        SALAD, SOUP, SPRING_ROLLS;
    }
    enum MainCourse implements Food {
        LASAGNE, BURRITO, PAD_THAI,
        LENTILS, HUMMOUS, VINDALOO;
    }
}
/*
对于 enum 而言，实现接口是使其子类化的唯一办法，
所以嵌入在 Food 中的每个 enum 都实现了 Food 接口。

在上述被分组的 enum 中，可以说
“所有东西都是某种类型的 Food”
 */

/**
 * 如果 enum 类型实现了 Food 接口，
 * 那么我们就可以将其实例向上转型为 Food
 */
class TypeOfFood {
    public static void main(String[] args) {
        Food food = Food.Appetizer.SALAD;
        System.out.println(food);

        food = Food.MainCourse.BURRITO;
        System.out.println(food);
    }
}

/*
当需要和一大堆类型打交道时，接口就不如 enum 好用了。

下面再将枚举进行包装：
 */
enum Course {
    APPETIZER(Food.Appetizer.class),
    MAINCOURSE(Food.MainCourse.class);

    private Food[] values;
    private Course(Class<? extends Food> kind) {
        values = kind.getEnumConstants();
    }

    public Food randomSelection() {
        return Enums.random(values);
    }
}
class Meal2 {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            for (Course course : Course.values()) {
                Food food = course.randomSelection();
                System.out.println(food);
            }
            System.out.println("-------");
        }
    }
}



/*
此外，还有一种更简洁的管理枚举的办法，
就是将一个 enum 嵌套在另一个 enum 内。

如下：
 */
enum SecurityCategory {
    STOCK(Security.Stock.class),
    BOND(Security.Bond.class);

    Security[] values;
    SecurityCategory(Class<? extends Security> kind) {
        values = kind.getEnumConstants();
    }
    public Security randomSelection() {
        return Enums.random(values);
    }

    interface Security {
        enum Stock implements Security {
            SHORT, LONG, MARGIN
        }
        enum Bond implements Security {
            MUNICIPAL, JUNK
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            SecurityCategory category = Enums.random(SecurityCategory.class);
            System.out.println(category + ": " +
                    category.randomSelection());
        }
    }
}
/*
上述代码中，
Security 接口的作用是将其所包含的 enum 组合成一个公共类型，
这一点是有必要的。
然后，
SecurityCategory 才能将 Security 中的 enum 作为其构造器的参数使用，
以起到组织的效果。
 */

/**
 * 如果我们将这种方式应用于 Food 的例子：
 */
enum Meal3 {
    APPETIZER(Food.Appetizer.class),
    MAINCOURSE(Food.MainCourse.class);

    private Food[] values;
    private Meal3(Class<? extends Food> kind) {
        values = kind.getEnumConstants();
    }

    public interface Food {
        enum Appetizer implements Food {
            SALAD, SOUP, SPRING_ROLLS;
        }
        enum MainCourse implements Food {
            LASAGNE, BURRITO, PAD_THAI,
            LENTILS, HUMMOUS, VINDALOO;
        }
    }

    public Food randomSelection() {
        return Enums.random(values);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            for (Meal3 meal : Meal3.values()) {
                Food food = meal.randomSelection();
                System.out.println(food);
            }
            System.out.println("------");
        }
    }
}
/*
其实，这仅仅是重新组织了一下代码，
不过多数情况下，
这种方式使你的代码具有更清晰的结构。
 */
